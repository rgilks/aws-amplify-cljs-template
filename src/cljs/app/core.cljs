(ns app.core
  (:require
   ["aws-exports" :default aws-exports]
   ["@sentry/react" :as sentry-react]
   ["@sentry/tracing" :as sentry-tracing]
   ["aws-amplify" :as amplify]
   [app.event :as event]
   [app.fx]
   [app.routing :as routing]
   [app.view :as view]
   [re-frame.core :as rf]
   [re-frame.interop :as rfinterop]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(set! rfinterop/debug-enabled? false)

(def debug? ^boolean goog.DEBUG)


(defn dev-setup []
  (when debug?
    (enable-console-print!)))


(def sentry-dsn
  "https://b52931d379be4eeb83d6d19ef14a61f4@o359295.ingest.sentry.io/4504603809480704")


(defn init-sentry! []
  (.init sentry-react
         (clj->js
          {:dsn              sentry-dsn
           :integrations     [(sentry-tracing/BrowserTracing.)]
           :tracesSampleRate 1.0})))


(defn init-hub-listeners! []
  (-> amplify/Hub
      (.listen "datastore"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
          ;;(println "datastore event" event)
                   (when (= event "ready")
                     (rf/dispatch [::event/datastore-ready]))))))

  (-> amplify/Hub
      (.listen "auth"
               (fn [^js data]
                 (let [event (-> data .-payload .-event)]
                   (when (= event "signIn")
                     (rf/dispatch [::event/user-get])))))))


(defonce root
  (uix.dom/create-root (js/document.getElementById "app")))


(defn init []
  (init-sentry!)
  (rf/clear-subscription-cache!)
  (-> amplify/Amplify (.configure aws-exports))
  (rf/dispatch-sync
   [::event/init-db {:page-visible    true
                     :current-route   nil
                     :datastore-ready false
                     :user            nil
                     :slug            nil
                     :games           nil}])
  (rf/dispatch-sync [::event/user-get])
  (rf/dispatch-sync [::event/init-listeners])
  (dev-setup)
  (routing/init-routes!)
  (init-hub-listeners!)
  (uix.dom/render-root ($ view/main) root))


(defn ^:export main []
  (init))

;; TODO: consider what should be inited on reload
;;(defn ^:dev/after-load reload! []
;;  (init))
