(ns app.view
  (:require
   [uix.core :refer [$ defui]]
   [uix.dom]
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["react-div-100vh" :default Div100vh]
   [refx.alpha :as refx]))

(refx/reg-sub
 ::games
 (fn [db] (:games db)))

(defui games []
  (let [games (refx/use-sub [::games])
        _ (println "games" games)]
    ($ :div
       ($ :div
          {:data-testid "games-title"
           :style {:color "white"}}
          "GAMES LIST")
       (when games
         ($ :div
            {:data-testid "games"
             :style {:color "white"}}
            (str games))))))

(defui main []
  ($ :<>
     ($ amplify-ui/Authenticator
        ($ Div100vh
           ($ :div
              ($ :div
                 {:style {:color "white"}}
                 "YOU ARE LOGGED IN!")
              ($ games))))))
