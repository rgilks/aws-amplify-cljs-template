(ns app.view-test
  (:require
   [cljs.test :refer [deftest is async]]
   [app.view :as comp]
   [app.test :as test]
   [app.datastore :as datastore]
   [uix.core :refer [$]]
   ["@testing-library/react" :as rtl]
   [promesa.core :as p]
   [refx.alpha :as refx]))

(deftest games-test
  (async
   done
   (rtl/render ($ comp/games))
   (is (some? (.queryByTestId rtl/screen "games-title")))
   (is (nil? (.queryByTestId rtl/screen "games")))
   (p/do
     (refx/dispatch-sync [::datastore/init {:games [{:id "10"}]}])
     (rtl/waitFor #(.getByTestId rtl/screen "games"))
    ;;  TODO: Fix up this test
    ;;  (is (= "[{:id \"10\"}]" (test/has-text? "games")))
     (rtl/cleanup)
     (done))))
