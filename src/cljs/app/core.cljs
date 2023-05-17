(ns app.core
  (:require
    ["aws-exports" :default aws-exports]
    ["aws-amplify" :refer [Amplify]]
    [re-frame.core :as re-frame]
    [reagent.dom :as reagent]
    [reitit.coercion.spec :as reitit-spec]
    [reitit.frontend :as reitit]
    [reitit.frontend.easy :as reitit-easy]
    [app.views.main :refer [router-root]]
    [app.events :as events]
    [app.subs]
    ))

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
    (re-frame/dispatch [::events/navigated new-match])))

(def routes
  [])

(def router
  (reitit/router
    routes
    {:data {:coercion reitit-spec/coercion}}))

(defn init-routes! []
  (reitit-easy/start!
    router
    on-navigate
    {:use-fragment false}))

(def debug? ^boolean goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defn init []
  (re-frame/clear-subscription-cache!)
  (-> Amplify (.configure aws-exports))
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (init-routes!)
  (re-frame/dispatch [::events/get :characters])
  (re-frame/dispatch-sync [::events/subscribe])
  (reagent/render [router-root {:router router}]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (init))

(defn ^:dev/after-load reload! []
  (init))




