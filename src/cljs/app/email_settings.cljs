(ns app.email-settings
  (:require
   [app.sub :as sub]
   [re-frame.core :as rf]
   ["@mui/material" :as mui]
   [uix.core :refer [$ defui]]
   [app.hooks :refer [use-subscribe]]
   [uix.dom]))


(rf/reg-event-fx
  ::update-unsubscribed
  (fn [{:keys [db]} [_ value]]
    {:update-user-att [(:user db) {"custom:unsubscribed" (str value)}]
     :db              (assoc db :unsubscribed value)}))


(defui view []
  (let [unsubscribed (use-subscribe [::sub/unsubscribed])]
    ($ mui/Box
      {:style {:margin "20px"}}
      ($ mui/Typography
        {:data-testid "email-settings-title"
         :variant     "h4"} "Email Settings")
      ($ mui/FormControlLabel
        {:label "Unsubscribe from all emails"
         :control
         ($ mui/Checkbox
           {:checked unsubscribed
            :onChange
            #(rf/dispatch
               [::update-unsubscribed
                (-> % .-target .-checked)])})}))))


