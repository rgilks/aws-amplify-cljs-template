(ns app.ds
  (:require
   ["aws-amplify" :as amplify]))

(defn obj->clj
  "Convert a JS object to a Clojure map."
  [obj]
  (js->clj (-> obj js/JSON.stringify js/JSON.parse) :keywordize-keys true))

(defn query-all [model on-success]
  (-> (.query amplify/DataStore model)
      (.then #(on-success (obj->clj %)))
      (.catch #(println "Error:" %))))

(def store
  {:query-all query-all})

