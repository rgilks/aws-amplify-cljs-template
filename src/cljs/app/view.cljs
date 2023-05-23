(ns app.view
  (:require
   [uix.core :refer [$ defui]]
   ["models" :as models]
   [uix.dom]
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["react-div-100vh" :default Div100vh]
   [refx.alpha :as refx]))

(refx/reg-sub
 ::todos
 (fn [db] (:todos db)))

(defui todos []
  (let [todos (refx/use-sub [::todos])
        _ (println "todos" todos)]
    ($ :div
       ($ :div
          {:data-testid "todos-title"
           :style {:color "white"}}
          "TODO LIST")
       (when todos
         ($ :div
            {:data-testid "todos"
             :style {:color "white"}}
            (str todos))))))

(defui main []
  ($ :<>
     ($ amplify-ui/Authenticator
        ($ Div100vh
           ($ :div
              ($ :div
                 {:style {:color "white"}}
                 "YOU ARE LOGGED IN!")
              ($ todos))))))
