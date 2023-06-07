(ns app.core-test
  (:require
   ["aws-amplify" :as amplify]
   [app.core :as core]
   [cljs.test :refer [deftest is]]
   [refx.alpha :as refx]))

(deftest init-hub-listeners-test
  (let [event-dispatched (atom nil)]
    (with-redefs [refx/dispatch #(reset! event-dispatched %)]
      (core/init-hub-listeners!
       [["test-channel" "test-event-1" ::test-event-1]])
      (is (nil? @event-dispatched))
      (-> amplify/Hub
          (.dispatch "test-channel" (clj->js {:event "X"})))
      (is (nil? @event-dispatched))
      (-> amplify/Hub
          (.dispatch "X" (clj->js {:event "test-event-1"})))
      (is (nil? @event-dispatched))
      (-> amplify/Hub
          (.dispatch "test-channel" (clj->js {:event "test-event-1"})))
      (is (= @event-dispatched [::test-event-1])))))
