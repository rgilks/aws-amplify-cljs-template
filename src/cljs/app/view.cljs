(ns app.view
  (:require
   [uix.core :refer [$ defui use-state use-effect]]
   ["aws-amplify" :as amplify]
   ["models" :as models]
   [uix.dom]
   [promesa.core :as p]
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["react-div-100vh" :default Div100vh]))

(defn obj->clj
  "Convert a JS object to a Clojure map."
  [obj]
  (js->clj (-> obj js/JSON.stringify js/JSON.parse) :keywordize-keys true))


(defui widget []
  ($ :div
     {:data-testid "widget"
      :style {:color "white"}} "WIDGET"))

(defui todos []
  (let [[todos set-todos] (use-state [])]
    (use-effect
     #(p/let [results (.query amplify/DataStore models/Todo)
              data (obj->clj results)]
        (println "results" data)
        (set-todos data)) [])
    ($ :div
       ($ :div
          {:style {:color "white"}}
          "TODO LIST")
       ($ :div
          {:style {:color "white"}}
          (str todos)))))

(defui main []
  ($ :<>
     ($ amplify-ui/Authenticator
        ($ Div100vh
           ($ :div
              ($ :div
                 {:style {:color "white"}}
                 "YOU ARE LOGGED IN!")
              ($ todos))))))
