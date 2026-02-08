(ns app.view-test
  (:require
   ["@testing-library/react" :as rtl]
   [app.view :as comp]
   [cljs.test :refer [deftest is]]
   [uix.core :refer [$]]))

(deftest games-initial-render-test
  (rtl/render ($ comp/games))
  (is (some? (.queryByTestId rtl/screen "games-title")))
  (is (nil? (.queryByTestId rtl/screen "games")))
  (rtl/cleanup))
