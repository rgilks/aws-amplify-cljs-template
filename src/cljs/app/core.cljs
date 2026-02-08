(ns app.core
  (:require
   ["aws-amplify" :refer [Amplify]]
   ["aws-amplify/utils" :refer [Hub]]
   ["aws-exports" :default aws-exports]
   [app.datastore :as datastore]
   [app.routing :as routing]
   [app.user :as user]
   [app.view :as view]
   [refx.alpha :as refx]
   [uix.core :refer [$]]
   [uix.dom]))

(defn init-hub-listeners! [hub-listeners]
  (doseq [[channel target-event re-frame-event] hub-listeners]
    (.listen Hub
             channel
             (fn [^js data]
               (let [event (-> data .-payload .-event)]
                 (println channel event)
                 (when (= event target-event)
                   (refx/dispatch [re-frame-event])))))))

(defonce root (atom nil))

(defn render []
  (when-not @root
    (reset! root (uix.dom/create-root (js/document.getElementById "app"))))
  (uix.dom/render-root ($ view/main) @root))

(defn ^:export main []
  (refx/clear-subscription-cache!)
  (.configure Amplify (clj->js aws-exports))
  (refx/dispatch-sync
   [::datastore/init
    {:current-route   nil
     :datastore-ready false
     :user            nil
     :slug            nil
     :games           nil}])
  (refx/dispatch-sync [::user/get])
  (routing/init-routes!)
  (init-hub-listeners!
   [["datastore" "ready"  ::datastore/ready]
    ["auth"      "signIn" ::user/get]])
  (render))

(defn ^:dev/after-load on-reload []
  (refx/clear-subscription-cache!)
  (render))
