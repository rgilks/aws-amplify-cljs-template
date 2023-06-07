(ns app.core
  (:require
   ["aws-amplify" :as amplify]
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
    (-> amplify/Hub
        (.listen
         channel
         (fn [^js data]
           (let [event (-> data .-payload .-event)]
             (println channel event)
             (when (= event target-event)
               (refx/dispatch [re-frame-event]))))))))

(defn init []
  (refx/clear-subscription-cache!)
  (-> amplify/Amplify (.configure aws-exports))
  (refx/dispatch-sync
   [::datastore/init
    {:current-route   nil
     :datastore-ready false
     :user            nil
     :slug            nil
     :games           nil}])
  (refx/dispatch-sync [::user/get])
  ;; (refx/dispatch-sync [::event/init-listeners])
  (routing/init-routes!)
  (init-hub-listeners!
   [["datastore" "ready"  ::datastore/ready]
    ["auth"      "signIn" ::user/get]])
  (uix.dom/render-root
   ($ view/main)
   (uix.dom/create-root (js/document.getElementById "app"))))

(defn ^:export main []
  (init))

;; (defn ^:dev/after-load clear-cache-and-render!
;;   []
;;   (refx/clear-subscription-cache!)
;;   (init))
