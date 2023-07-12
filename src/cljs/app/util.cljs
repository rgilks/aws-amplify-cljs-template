(ns app.util
  (:require [clojure.string :as str]))

(defn iso-time
  "Return the current time in ISO format."
  [] (.toISOString (js/Date.)))

;; TODO: Figure out a better way to do this.
(defn obj->clj
  "Convert a JS object to a Clojure map."
  [obj]
  (js->clj (-> obj js/JSON.stringify js/JSON.parse) :keywordize-keys true))

(defn idx
  "Create a map of id -> item from an array of items."
  [& arr] (reduce (fn [m v] (assoc m (:id v) v)) {} arr))

(defn kebab-case
  "Convert a string to kebab-case."
  [s]
  (when s
    (-> s
        (str/replace " " "-")
        (str/lower-case))))

(defn parse-int
  "Parse an integer from a string."
  [s]
  (js/parseInt s 10))

