(ns app.routing
  (:require
   [app.email-settings :as email-settings]
   [app.event :as event]
   [app.view :as view]
   [re-frame.core :as rf]
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
    (rf/dispatch [::navigated new-match])))


(def routes
  [
   ["/email-settings"
    {:name :email-settings
     :view email-settings/view}]

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


(rf/reg-fx
  ::push-state-fx
  (fn [route]
    (apply reitit-easy/push-state route)))


(rf/reg-event-fx
  ::push-state
  (fn [_ [_ & route]]
    {::push-state-fx route}))


(rf/reg-event-db
  ::navigated
  (fn [db [_ new-match]]
    (let [old-match   (:current-route db)
          controllers (reitit-controllers/apply-controllers
                        (:controllers old-match) new-match)]
      (assoc db :current-route (assoc new-match :controllers controllers)))))
