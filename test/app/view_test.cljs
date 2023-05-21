(ns app.view-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.string :as str]
            [app.view :as comp]
            [uix.core :refer [$]]
            ["@testing-library/react" :as rtl]))

(deftest view-test
  (testing "task feedback shows the cefr score if the quality is good"
    (let [result (rtl/render ($ comp/widget {}))
          widget (.getByTestId result "widget")]
      (is (= "WIDGET" (.-textContent widget))))
    (rtl/cleanup)))