(ns app.views.main
  (:require
    [re-frame.core :refer [subscribe]]
    ["@aws-amplify/ui-react" :refer [Authenticator]]
    ["react-ios-pwa-prompt" :default PWAPrompt]
    ["react-div-100vh" :default Div100vh]
    ["@mui/material/styles" :refer [ThemeProvider createTheme]]
    ["@mui/material" :refer [CssBaseline]]
    [app.subs :as subs]
    [app.views.theme :refer [theme]]
    ))

(defn router-root []
  (let [current-route @(subscribe [::subs/current-route])]
    [:<>
     [:> Authenticator
      [:> Div100vh
       [:> ThemeProvider {:theme (createTheme (clj->js theme))}
        [:> CssBaseline]
        (when current-route
          [(-> current-route :data :view)])]]]]
    ;;[:> PWAPrompt]
    ))


