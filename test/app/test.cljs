(ns app.test
  (:require
   ["@testing-library/react" :as rtl]))

(defn has-text? [test-id]
  (.-textContent (.getByTestId rtl/screen test-id)))

