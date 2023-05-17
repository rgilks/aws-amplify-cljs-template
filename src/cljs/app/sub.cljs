(ns app.sub
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub
 ::current-route
 (fn [db]
   (:current-route db)))
