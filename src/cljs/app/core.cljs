(ns app.core
  (:require
   ["aws-exports" :default aws-exports]
   ["aws-amplify" :as amplify]
   ["models" :as models]
   [app.view :as view]
   [app.datastore :as ds]
   [uix.core :refer [$]]
   [uix.dom]
   [refx.alpha :as refx]))

(def debug? ^boolean goog.DEBUG)

(defn- dev-setup []
  (when debug?
    (enable-console-print!)))

(defn init-hub-listeners! []
  (-> amplify/Hub
      (.listen "datastore"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
                   (println "datastore event" event)
                   (when (= event "ready")
                     (refx/dispatch [::ds/datastore-ready]))))))

  (-> amplify/Hub
      (.listen "auth"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
                   (when (= event "signIn")
                     (refx/dispatch [::ds/user-get])))))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "app")))

(defn init []
  (refx/clear-subscription-cache!)
  (-> amplify/Amplify (.configure aws-exports))
  (refx/dispatch-sync
   [::ds/init-db
    {:page-visible    true
     :current-route   nil
     :datastore-ready false
     :user            nil
     :slug            nil
     :games           nil}])
  (refx/dispatch-sync [::ds/user-get])
  ;; (refx/dispatch-sync [::event/init-listeners])
  (dev-setup)
  ;; (routing/init-routes!)
  (init-hub-listeners!)
  (uix.dom/render-root ($ view/main) root))

(defn ^:export main []
  (init))

;; (defn ^:dev/after-load clear-cache-and-render!
;;   []
;;   (refx/clear-subscription-cache!)
;;   (init))
