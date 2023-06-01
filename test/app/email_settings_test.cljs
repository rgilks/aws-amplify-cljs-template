(ns app.email-settings-test
  (:require
   [app.email-settings :as comp]
   [cljs.test :refer [deftest is]]
   [app.user :as user]
   [app.datastore :as datastore]
   [uix.core :refer [$]]
   ["@testing-library/react" :as rtl]
   [refx.alpha :as refx]))

(refx/reg-event-fx
 ::update-unsubscribed
 (fn [{:keys [db]} [_ value]]
   {::user/update-user-att [(:user db) {"custom:unsubscribed" (str value)}]
    :db              (assoc db :unsubscribed value)}))

(deftest email-settings-subscribe
  (refx/reg-fx
   ::user/update-user-att
   (fn [[user att]]
     (is (= "the-user" user))
     (is (= {"custom:unsubscribed" "true"} att))))

  (refx/dispatch [::datastore/init {:unsubscribed false :user "the-user"}])

  (let [comp     (rtl/render ($ comp/view))
        checkbox (.getByLabelText comp "Unsubscribe from all emails")]
    (is (false? (.-checked checkbox)))
    (.click rtl/fireEvent checkbox)
    (is (true? (.-checked checkbox))))
  (rtl/cleanup))

