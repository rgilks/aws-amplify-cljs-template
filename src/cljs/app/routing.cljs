(ns app.routing
  (:require
   [app.datastore :as datastore]
   [app.email-settings :as email-settings]
   [app.games :as games]
   [refx.alpha :as refx]
   [reitit.coercion.spec :as reitit-spec]
   [reitit.frontend :as reitit]
   [reitit.frontend.controllers :as reitit-controllers]
   [reitit.frontend.easy :as reitit-easy]))

(defn href
  "Return relative url for given route. Url can be used in HTML links."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (reitit-easy/href k params query)))

(defn on-navigate [new-match]
  (when new-match
    (refx/dispatch [::navigated new-match])))

(refx/reg-event-fx
 ::check-access-by-slug
 (fn [{:keys [db]} [_ slug]]
   (let [username (:username db)]
     (println "CHECK ACCESS" slug username)
     {:db           (assoc db :slug slug)
      :check-access [slug username]})))

(def routes
  [["/games"
    {:name :games
     :view games/view
     :controllers
     [{:start (fn []
                (println "games controller")
                (refx/dispatch [::datastore/configure "UNKNOWN"]))}]}]

   ["/email-settings"
    {:name :email-settings
     :view email-settings/view}]

  ;;  ["/game/:slug"
  ;;   {:name :game
  ;;    :view view/game-view
  ;;    :controllers
  ;;    [{:parameters {:path [:slug]}
  ;;      :start      #(refx/dispatch [::check-access-by-slug (-> % :path :slug)])}]}]
   ])
(def router
  (reitit/router
   routes
   {:data {:coercion reitit-spec/coercion}}))

(defn init-routes! []
  (reitit-easy/start!
   router
   on-navigate
   {:use-fragment false}))

(refx/reg-fx
 ::push-state-fx
 (fn [route]
   (apply reitit-easy/push-state route)))

(refx/reg-event-fx
 ::push-state
 (fn [_ [_ & route]]
   {::push-state-fx route}))

(refx/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))

(refx/reg-event-db
 ::navigated
 (fn [db [_ new-match]]
   (let [old-match   (:current-route db)
         controllers (reitit-controllers/apply-controllers
                      (:controllers old-match) new-match)]
     (assoc db :current-route (assoc new-match :controllers controllers)))))
