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

;; (defui not-a-player []
;;   ($ mui/Box {:style {:padding "10px"}}
;;      ($ mui/Typography {:variant "h5"} "This game is not open to spectators, you are not a player.")))

;; (defui sync-data []
;;   ($ mui/Box {:style {:padding "10px"}}
;;      ($ mui/Typography {:variant "h5"} "Syncing data...")))

;; (defui game [{:keys [id]}]
;;   (let [player   (first (filter #(= (:owner %) username) (vals players)))
;;         is-owner (= (:owner game) username)
;;         can-view (or is-owner (some? player) (:allowSpectators game))]
;;     (when-not (and game can-view)
;;       [not-a-player])
;;     (when (and game (or is-owner player (:allowSpectators game)))
;;       ($ :<>  "THE GAME VIEW"))))

;; (defui game-view []
;;   (let [datastore-ready? (refx/use-sub [::sub/datastore-ready])
;;         game-id          (refx/use-sub [::sub/game-id])
;;         is-player        (refx/use-sub [::sub/is-player])]
;;     (when game-id
;;       (if is-player
;;         (if datastore-ready?
;;           ($ game {:id game-id})
;;           ($ sync-data))
;;         ($ not-a-player)))))

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

;; (defui main []
;;   ($ :<>
;;      ($ amplify-ui/Authenticator
;;         ($ Div100vh
;;            ($ mui-styles/ThemeProvider
;;               {:theme (mui-styles/createTheme (clj->js theme))}
;;               ($ mui/CssBaseline)
;;               ($ :div
;;                  {:style {:position   "absolute"
;;                           :text-align "right"
;;                           :z-index    9999
;;                           :bottom     -5
;;                           :right      10
;;                           :margin     0
;;                           :padding    0}}
;;                  ($ :a {:href "/games"}
;;                     ($ :img
;;                        {:style {:width         60
;;                                 :height        60
;;                                 :opacity       0.8
;;                                 :margin        30
;;                                 :margin-bottom 0
;;                                 :padding       0}
;;                         :src   "/logo.png"
;;                         :alt   "APP TEMPLATE"})))
;;               ($ router-component))))))
