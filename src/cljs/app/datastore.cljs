(ns app.datastore
  (:require
   ["aws-amplify" :as amplify]
   [promesa.core :as p] 
   [app.ut :as ut]
   ["models" :as models]
   [refx.alpha :as refx]))

(defn sync-expressions [game-id username]
  [;;(amplify/syncExpression models/Game
   ;;  (fn [] (fn [^js i] (-> i .-name (.ne "New")))))
   ])

(defn configure [game-id username]
  (p/do
    (.configure amplify/DataStore
                (clj->js {:amplify/syncExpressions (sync-expressions game-id username)}))
    (.stop amplify/DataStore))
  (js/setTimeout #(.start amplify/DataStore) 1000))


(refx/reg-event-db
 ::init-db
 (fn [_ [_ init]] init))

(refx/reg-event-db
 ::update-models-db
 (fn [db [_ model-key data]]
   (let [updated (assoc db model-key data)]
     (println "updated" updated)
     updated)))

(refx/reg-fx
 :get-items
 (fn [models]
   (println "GET ITEMS")
   (doseq [[key model] models]
     (p/let [result (.query amplify/DataStore model)
             data (ut/obj->clj result)
             _ (println "GET ITEMS" key data)
             keyed-data (reduce #(assoc %1 (:id %2) %2) {} data)]
       (refx/dispatch [::update-models-db key keyed-data])))))

(refx/reg-event-fx
 ::datastore-ready
 (fn
   [{:keys [db]} [_]]
   (println "Datastore ready")
   (let [models  [[:todos models/Todo]]]
     {:get-items models
      :subscribe models
      :db (assoc db :datastore-ready true)})))

(refx/reg-fx
 :datastore-configure
 (fn [[game-id username]]
   (println "Datastore configure - username:" username " game-id:" game-id)
   (when username
     (configure game-id username))))

(refx/reg-event-db
 ::delete-model
 (fn [db [_ model-key id]]
   (println "DELETE MODEL" model-key id)
   (update db model-key dissoc id)))


(refx/reg-event-db
 ::update-model
 (fn [db [_ model-key id data]]
   (println "UPDATE MODEL" model-key id data)
   (when (not (get-in db [:timeout-ids id]))
     (assoc-in db [model-key id] data))))

(defn- handle-subs [model-key ^js msg]
  (let [element (.-element msg)
        id      (.-id element)
        opType  (.-opType msg)]
    (if (= opType "DELETE")
      (refx/dispatch [::delete-model model-key id])
      (refx/dispatch [::update-model model-key id (ut/obj->clj element)]))))

(refx/reg-fx
 :subscribe
 (fn [models]
   (doseq [[key model] models]
     (.subscribe
      (.observe amplify/DataStore model) #(handle-subs key %)))))

(refx/reg-event-fx
 ::datastore-configure
 (fn
   [{:keys [db]} [_ game-id]]
   (let [username (:username db)]
     {:datastore-configure [game-id username]
      :db                  (assoc db :datastore-ready false)})))

(refx/reg-event-fx
 ::user-update
 (fn [{:keys [db]} [_ user]]
   (let [username
         (.-username user)
         unsubscribed
         (= "true"
            (get (js->clj (.-attributes user)) "custom:unsubscribed"))
         fx {:db (assoc db
                        :user user
                        :username username
                        :unsubscribed unsubscribed)}]
     (println "UPDATE USER" username)
     (merge fx {:dispatch [::datastore-configure "UNKNOWN"]}))))

(refx/reg-fx
 :get-user
 (fn []
   (-> (p/let [user (.currentAuthenticatedUser amplify/Auth)]
         (refx/dispatch [::user-update user]))
       (p/catch #(println "GET-USER: " %)))))

(refx/reg-event-fx
 ::user-get
 (fn [_ [_]]
   {:get-user []}))
