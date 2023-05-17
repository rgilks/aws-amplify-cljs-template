(ns app.event
  (:require
   ["models" :as models]
   [app.cofx :as cofx]
   [re-frame.core :as rf]))


(rf/reg-event-db
 ::init-db
 (fn [_ [_ init]] init))


(rf/reg-event-fx
 ::play-sound
 (fn [_ [_ k]] {:play-sound k}))
