(ns app.user
  (:require
   ["aws-amplify" :as amplify]
   [promesa.core :as p]
   [app.datastore :as datastore]
   [refx.alpha :as refx]))

(defn is-unsubscribed? [user]
  (= "true" (get (js->clj (.-attributes user)) "custom:unsubscribed")))

(refx/reg-event-fx
 ::update
 (fn [{:keys [db]} [_ user]]
   (let [username (.-username user)
         unsubscribed (is-unsubscribed? user)
         fx {:db (assoc db
                        :user user
                        :username username
                        :unsubscribed unsubscribed)}]
     (println "Update user" username)
     (merge fx {:dispatch [::datastore/configure "UNKNOWN"]}))))

(refx/reg-fx
 :get-user
 (fn []
   (-> (p/let [user (.currentAuthenticatedUser amplify/Auth)]
         (refx/dispatch [::update user]))
       (p/catch #(println "Get user" %)))))

(refx/reg-event-fx
 ::get
 (fn [_ [_]]
   {:get-user []}))