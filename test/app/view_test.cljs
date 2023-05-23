(ns app.view-test
  (:require
   [cljs.test :refer [deftest is testing async]]
   [app.view :as comp]
   [app.datastore :as ds]
   [uix.core :refer [$]]
   ["@testing-library/react" :as rtl]
   [promesa.core :as p]
   [refx.alpha :as refx]))

(deftest games-test
  (testing "games"
    (async
     done
     (rtl/render ($ comp/games))
     (is (some? (.queryByTestId rtl/screen "games-title")))
     (is (nil? (.queryByTestId rtl/screen "games")))

     (p/do
       (refx/dispatch-sync [::ds/init-db {:games [{:id "10"}]}])
       (rtl/waitFor #(.getByTestId rtl/screen "games"))
       (is (= "[{:id \"10\"}]"
              (.-textContent (.getByTestId rtl/screen "games"))))
       (rtl/cleanup)
       (done)))))
