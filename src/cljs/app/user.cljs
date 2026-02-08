(ns app.user
  (:require
   ["aws-amplify/auth" :refer [getCurrentUser fetchUserAttributes updateUserAttributes]]
   [app.datastore :as datastore]
   [promesa.core :as p]
   [refx.alpha :as refx]))

(defn is-unsubscribed? [attributes]
  (= "true" (get attributes "custom:unsubscribed")))

(refx/reg-event-fx
 ::update
 (fn [{:keys [db]} [_ username attributes]]
   (let [unsubscribed (is-unsubscribed? attributes)
         fx {:db (assoc db
                        :username username
                        :attributes attributes
                        :unsubscribed unsubscribed)}]
     (println "Update user" username)
     (merge fx {:dispatch [::datastore/configure "UNKNOWN"]}))))

(refx/reg-fx
 ::get-user
 (fn []
   (-> (p/let [user (getCurrentUser)
               username (.-username user)
               attrs (fetchUserAttributes)
               attributes (js->clj attrs)]
         (refx/dispatch [::update username attributes]))
       (p/catch #(println "Get user" %)))))

(refx/reg-fx
 ::update-user-att
 (fn [[_ att]]
   (updateUserAttributes
    (clj->js {:userAttributes att}))))

(refx/reg-event-fx
 ::get
 (fn [_ [_]]
   {::get-user []}))
