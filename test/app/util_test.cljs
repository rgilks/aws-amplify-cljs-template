(ns app.util-test
  (:require
   [app.util :as ut]
   [cljs.test :refer [deftest is]]))

(deftest iso-time-test
  (let [result (ut/iso-time)]
    (is (= (count result) 24) "ISO 8601 strings are 24 characters long.")))

(deftest obj->clj-test
  (let [result (ut/obj->clj (js-obj "key" "value"))]
    (is (= {:key "value"} result))))

(deftest idx-test
  (let [result (ut/idx {:id "1" :val "one"} {:id "2" :val "two"})]
    (is (= {"1" {:id "1" :val "one"} "2" {:id "2" :val "two"}} result))))
