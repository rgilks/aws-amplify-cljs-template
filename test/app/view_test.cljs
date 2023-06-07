(ns app.view-test
  (:require
   ["@testing-library/react" :as rtl]
   [app.datastore :as datastore]
   [app.test :as test]
   [app.view :as comp]
   [cljs.test :refer [deftest is async]]
   [promesa.core :as p]
   [refx.alpha :as refx]
   [uix.core :refer [$]]))

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
