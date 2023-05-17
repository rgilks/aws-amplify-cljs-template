(ns app.datastore
  (:require
   ["aws-amplify" :as amplify]
   [promesa.core :as p]
   ["models" :as models]))


(defn sync-expressions [game-id username]
  [;;(amplify/syncExpression models/Game
   ;;  (fn [] (fn [^js i] (-> i .-name (.ne "New")))))
   ])


(defn configure [game-id username]
  (p/do
    (.configure amplify/DataStore
                (clj->js
                 {:amplify/syncExpressions
                  (sync-expressions game-id username)}))
    (.stop amplify/DataStore))
  (js/setTimeout #(.start amplify/DataStore) 1000))

