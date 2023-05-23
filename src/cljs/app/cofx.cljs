(ns app.cofx
  (:require [re-frame.core :as rf]))

(rf/reg-cofx
  ::uuid
  (fn [cofx _]
    (assoc cofx :uuid (str (random-uuid)))))

(rf/reg-cofx
  ::time
  (fn [cofx _]
    (assoc cofx :time (.getTime (js/Date.)))))
