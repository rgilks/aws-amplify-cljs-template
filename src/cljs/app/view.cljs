(ns app.view
  (:require
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["@mui/material" :as mui]
   ["@mui/material/styles" :as mui-styles]
   ["react-div-100vh" :default Div100vh]
   [app.routing :as routing]
   [app.theme :refer [theme]]
   [refx.alpha :as refx]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(refx/reg-sub
 ::games
 (fn [db] (:games db)))

(defui router-component []
  (let [current-route (refx/use-sub [::routing/current-route])]
    (when current-route
      ($ (-> current-route :data :view)))))

(defui games []
  (let [games (refx/use-sub [::games])]
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
           ($ mui-styles/ThemeProvider
              {:theme (mui-styles/createTheme (clj->js theme))}
              ($ mui/CssBaseline)
              ($ :div
                 ($ :div
                    {:data-testid "logged-in"
                     :style {:color "white"}}
                    "YOU ARE LOGGED IN!")
                 ($ router-component)))))))
