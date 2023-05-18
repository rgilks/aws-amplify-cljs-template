(ns app.core
  (:require
   ["aws-exports" :default aws-exports]
   ["aws-amplify" :as amplify]
   [app.view :as view]
   [uix.core :refer [$]]
   [uix.dom]))

(def debug? ^boolean goog.DEBUG)

(defn- dev-setup []
  (when debug?
    (enable-console-print!)))

(defn- init-hub-listeners! []
  (-> amplify/Hub
      (.listen
       "datastore"
       (fn [^js data]
         (let [event (-> data .-payload .-event)]
           (println "datastore event" event)))))

  (-> amplify/Hub
      (.listen
       "auth"
       (fn [^js data]
         (let [event (-> data .-payload .-event)]
           (println "auth event" event))))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "app")))

(defn init []
  (-> amplify/Amplify (.configure aws-exports))
  (dev-setup)
  (init-hub-listeners!)
  (uix.dom/render-root ($ view/main) root))

(defn ^:export main []
  (init))

;; TODO: consider what should be inited on reload
;;(defn ^:dev/after-load reload! []
;;  (init))
