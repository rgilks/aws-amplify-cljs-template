(ns app.email-settings
  (:require
   ["@mui/material" :as mui]
   [app.user :as user]
   [refx.alpha :as refx]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(refx/reg-sub
 ::unsubscribed
 (fn [db] (:unsubscribed db)))

(refx/reg-event-fx
 ::update-unsubscribed
 (fn [{:keys [db]} [_ value]]
   {::user/update-user-att [(:user db) {"custom:unsubscribed" (str value)}]
    :db              (assoc db :unsubscribed value)}))

(defui view []
  (let [unsubscribed (refx/use-sub [::unsubscribed])]
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
               #(refx/dispatch
                 [::update-unsubscribed
                  (-> % .-target .-checked)])})}))))


