(ns app.view-test
  (:require [cljs.test :refer [deftest is testing async]]
            [app.view :as comp]
            [uix.core :refer [$]]
            ["@testing-library/react" :as rtl]
            [promesa.core :as p]))

(deftest todos-test
  (async
   done
   (rtl/render ($ comp/todos {}))
   (is (nil? (.queryByTestId rtl/screen "todos")))
   (p/do
     (rtl/waitFor #(.getByTestId rtl/screen "todos"))
     (is (= "[{:id \"1\", :name \"name\"}]"
            (.-textContent (.getByTestId rtl/screen "todos"))))
     (rtl/cleanup)
     (done))))
