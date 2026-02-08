(ns app.datastore
  (:require
   ["aws-amplify/datastore" :refer [DataStore]]
   ["models" :as models]
   [app.util :as util]
   [goog.object :as gobj]
   [promesa.core :as p]
   [refx.alpha :as refx]))

(defn- handle-subs [model-key ^js msg]
  (let [element (.-element msg)
        id      (.-id element)
        opType  (.-opType msg)]
    (if (= opType "DELETE")
      (refx/dispatch [::delete-model model-key id])
      (refx/dispatch [::update-model model-key id (util/obj->clj element)]))))

(refx/reg-fx
 :configure
 (fn [[game-id username]]
   (println "Datastore configure - username:" username " game-id:" game-id)
   (when username
     (.start DataStore))))

(refx/reg-fx
 :subscribe
 (fn [models]
   (doseq [[key model] models]
     (.subscribe
      (.observe DataStore model) #(handle-subs key %)))))

(refx/reg-fx
 :get-items
 (fn [models]
   (doseq [[key model] models]
     (p/let [result (.query DataStore model)
             data (util/obj->clj result)
             keyed-data (reduce #(assoc %1 (:id %2) %2) {} data)]
       (refx/dispatch [::update-models key keyed-data])))))

(refx/reg-event-db
 ::init
 (fn [_ [_ init]] init))

(refx/reg-event-db
 ::update-models
 (fn [db [_ model-key data]]
   (assoc db model-key data)))

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

(refx/reg-event-fx
 ::configure
 (fn
   [{:keys [db]} [_ game-id]]
   (let [username (:username db)]
     {:configure [game-id username]
      :db (assoc db :datastore-ready false)})))

(refx/reg-event-fx
 ::ready
 (fn
   [{:keys [db]} [_]]
   (let [models  [[:games models/Game]]]
     {:get-items models
      :subscribe models
      :db (assoc db :datastore-ready true)})))

(refx/reg-fx
 :delete-item
 (fn [[model id]]
   (p/let [item (.query DataStore model id)]
     (.delete DataStore item))))

(refx/reg-fx
 :new-item
 (fn [[model item]]
   (.save DataStore
          (model. (clj->js item)))))

(def ignore-keys
  #{"_version" "_lastChangedAt" "_deleted" "updatedAt"})

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(refx/reg-fx
 :update-item
 (fn [[model item]]
   (p/let [result (.query DataStore model (:id item))
           clone ^js/object (.copyOf
                             model
                             result
                             #(gobj/forEach
                               item
                               (fn [v k _]
                                 (when (not (in? ignore-keys k))
                                   (gobj/set % k v)))))]
     (println "UPDATE ITEM" (util/obj->clj result))
     (println "CLONE" clone)
     (.save DataStore clone))))
