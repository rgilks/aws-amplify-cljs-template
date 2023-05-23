(ns app.view-test
  (:require
   [cljs.test :refer [deftest is testing async]]
   [app.view :as comp]
   [app.datastore :as ds]
   [uix.core :refer [$]]
   ["@testing-library/react" :as rtl]
   [promesa.core :as p]
   [refx.alpha :as refx]))

(deftest todos-test
  (testing "todos"
    (async
     done
     (rtl/render ($ comp/todos))
     (is (some? (.queryByTestId rtl/screen "todos-title")))
     (is (nil? (.queryByTestId rtl/screen "todos")))

     (p/do
       (refx/dispatch-sync [::ds/init-db {:todos [{:id "10"}]}])
       (rtl/waitFor #(.getByTestId rtl/screen "todos"))
       (is (= "[{:id \"10\"}]"
              (.-textContent (.getByTestId rtl/screen "todos"))))
       (rtl/cleanup)
       (done)))))
