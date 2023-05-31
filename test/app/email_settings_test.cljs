(ns app.email-settings-test
  (:require
   [app.email-settings :as email-settings]
   [clojure.test :refer-macros [deftest testing is use-fixtures]]
   [refx.alpha :as refx]
   [uix.core :refer [$]]
   [uix.dom]
   ["@testing-library/react" :as rtl]))

;; (use-fixtures :each
;;   (test/fixture-re-frame)
;;   {:after rtl/cleanup})


;; (deftest email-settings
;;   (rf-test/run-test-sync

;;     (testing "Unsubscribe Checkbox:"


;;       (testing "If the user is subscribed and checks the checkbox,
;;                 the user should be unsubscribed."

;;         (rf/reg-fx
;;           :update-user-att
;;           (fn [[user att]]
;;             (is (= "the-user" user))
;;             (is (= {"custom:unsubscribed" "true"} att))))

;;         (rf/dispatch [::event/init-db {:unsubscribed false :user "the-user"}])

;;         (let [comp     (test/render ($ email-settings/view))
;;               checkbox (.getByLabelText comp "Unsubscribe from all emails")]
;;           (is (false? @(rf/subscribe [::sub/unsubscribed])))
;;           (is (not (.-checked checkbox)))
;;           (.click rtl/fireEvent checkbox)
;;           (is (true? @(rf/subscribe [::sub/unsubscribed])))
;;           (.unmount comp)))


;;       (testing "If the user is unsubscribed and unchecks the checkbox,
;;                 the user should be subscribed."

;;         (rf/reg-fx
;;           :update-user-att
;;           (fn [[user att]]
;;             (is (= "the-user" user))
;;             (is (= {"custom:unsubscribed" "false"} att))))

;;         (rf/dispatch [::event/init-db {:unsubscribed true :user "the-user"}])

;;         (let [comp     (test/render ($ email-settings/view))
;;               checkbox (.getByLabelText comp "Unsubscribe from all emails")]
;;           (is (true? @(rf/subscribe [::sub/unsubscribed])))
;;           (is (.-checked checkbox))
;;           (.click rtl/fireEvent checkbox)
;;           (is (false? @(rf/subscribe [::sub/unsubscribed])))
;;           (.unmount comp))

;;         ))))
