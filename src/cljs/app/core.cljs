(ns app.core
  (:require
   ["aws-exports" :default aws-exports]
   ["aws-amplify" :as amplify]
   [app.view :as view]
   [app.datastore :as ds]
   [uix.core :refer [$]]
   [uix.dom]
   [refx.alpha :as refx]))

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
   [::ds/init-db
    {:page-visible    true
     :current-route   nil
     :datastore-ready false
     :user            nil
     :slug            nil
     :games           nil}])
  (refx/dispatch-sync [::ds/user-get])
  ;; (refx/dispatch-sync [::event/init-listeners])
  ;; (routing/init-routes!)
  (init-hub-listeners!
   [["datastore" "ready"  ::ds/datastore-ready]
    ["auth"      "signIn" ::ds/user-get]])
  (uix.dom/render-root
   ($ view/main)
   (uix.dom/create-root (js/document.getElementById "app"))))

(defn ^:export main []
  (init))

;; (defn ^:dev/after-load clear-cache-and-render!
;;   []
;;   (refx/clear-subscription-cache!)
;;   (init))
