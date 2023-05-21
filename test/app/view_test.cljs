(ns app.view-test
  (:require [cljs.test :refer [deftest is testing async]]
            [app.view :as comp]
            [uix.core :refer [$]]
            ["@testing-library/react" :as rtl]))

(deftest todos-test
  (testing "display todos"
    (let [store {:query-all #(%2 [{:id "10" :name "name"}])}]
      (rtl/render ($ comp/todos {:store store}))
      (is (= "[{:id \"10\", :name \"name\"}]"
             (.-textContent (.getByTestId rtl/screen "todos"))))))
  (rtl/cleanup))
