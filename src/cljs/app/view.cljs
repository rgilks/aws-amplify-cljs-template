(ns app.view
  (:require
   [app.config :as config]
   [app.hooks :refer [use-subscribe]]
   [app.sub :as sub]
   [app.theme :refer [theme]]
   [re-frame.core :as rf]
   [uix.core :refer [$ defui]]
   [uix.dom]
   ["@mui/material/styles" :as mui-styles]
   ["@mui/material" :as mui]
   ["@aws-amplify/ui-react" :as amplify-ui]
   ["react-ios-pwa-prompt" :default PWAPrompt]
   ["react-div-100vh" :default Div100vh]))


(defui router-component []
  (let [current-route (use-subscribe [::sub/current-route])]
    (when (and current-route)
      ($ (-> current-route :data :view)))))


(defui main []
  ($ :<>
     ($ amplify-ui/Authenticator
        ($ Div100vh
           ($ mui-styles/ThemeProvider
              {:theme (mui-styles/createTheme (clj->js theme))}
              ($ mui/CssBaseline)
              ($ :div "YOU ARE LOGGED IN!")
              ($ router-component))))
     ($ PWAPrompt)))





