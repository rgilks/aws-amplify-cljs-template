(ns app.core
  (:require
   ["aws-exports" :default aws-exports]
   ["aws-amplify" :as amplify]
   ["models" :as models]
   [app.view :as view]
   [promesa.core :as p]
   [uix.core :refer [$]]
   [app.ut :as ut]
   [uix.dom]
   [refx.alpha :as refx]))

(def debug? ^boolean goog.DEBUG)

(defn- dev-setup []
  (when debug?
    (enable-console-print!)))

(refx/reg-event-db
 ::init-db
 (fn [_ [_ init]] init))

(refx/reg-event-db
 ::update-models-db
 (fn [db [_ model-key data]]
   (assoc db model-key data)))

(refx/reg-fx
 :get-items
 (fn [models]
   (doseq [[key model] models]
     (p/let [result (.query amplify/DataStore model)
             data (ut/obj->clj result)
             keyed-data (reduce #(assoc %1 (:id %2) %2) {} data)]
       (refx/dispatch [::update-models-db key keyed-data])))))

(refx/reg-event-fx
 ::datastore-ready
 (fn
   [{:keys [db]} [_]]
   (println "Datastore ready")
   (let [models  [[:games models/Todo]]]
     {:get-items models
      :subscribe models
      :db (assoc db :datastore-ready true)})))

(refx/reg-fx
 :datastore-configure
 (fn [[game-id username]]
   (println "Datastore configure - username:" username " game-id:" game-id)
  ;;  (when username
  ;;    (datastore/configure game-id username))
   ))

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

(defn init-hub-listeners! []
  (-> amplify/Hub
      (.listen "datastore"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
                   (println "datastore event" event)
                   (when (= event "ready")
                     (refx/dispatch [::datastore-ready]))))))

  (-> amplify/Hub
      (.listen "auth"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
                   (when (= event "signIn")
                     (refx/dispatch [::user-get])))))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "app")))

(defn init []
  (refx/clear-subscription-cache!)
  (-> amplify/Amplify (.configure aws-exports))
  (refx/dispatch-sync
   [::init-db
    {:page-visible    true
     :current-route   nil
     :datastore-ready false
     :user            nil
     :slug            nil
     :games           nil}])
  (refx/dispatch-sync [::user-get])
  ;; (refx/dispatch-sync [::event/init-listeners])
  (dev-setup)
  ;; (routing/init-routes!)
  (init-hub-listeners!)
  (uix.dom/render-root ($ view/main) root))

(defn ^:export main []
  (init))

;; (defn ^:dev/after-load clear-cache-and-render!
;;   []
;;   (refx/clear-subscription-cache!)
;;   (init))
