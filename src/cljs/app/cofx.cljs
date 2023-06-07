(ns app.cofx
  (:require [refx.alpha :as refx]))

(refx/reg-cofx
 ::uuid
 (fn [cofx _]
   (assoc cofx :uuid (str (random-uuid)))))

(refx/reg-cofx
 ::time
 (fn [cofx _]
   (assoc cofx :time (.getTime (js/Date.)))))
