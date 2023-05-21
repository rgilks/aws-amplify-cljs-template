(ns app.view
  (:require
   [uix.core :refer [$ defui use-state use-effect]]
   ["models" :as models]
   [uix.dom]
   [app.ds]
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["react-div-100vh" :default Div100vh]))

(defui todos [{:keys [store] :as _props}]
  (let [[todos set-todos] (use-state nil)
        {:keys [query-all]} store]
    (use-effect #(query-all models/Todo set-todos) [query-all])
    ($ :div
       ($ :div
          {:style {:color "white"}}
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
              ($ todos {:store app.ds/store}))))))
