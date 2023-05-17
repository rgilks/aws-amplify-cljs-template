(ns app.fx
  (:require
   [app.datastore :as datastore]
   [app.event :as event]
   [app.sound :as sound]
   [app.ut :as ut]
   [goog.object :as gobj]
   [promesa.core :as p]
   ["aws-amplify" :as amplify]
   ["models" :as models]
   [re-frame.core :as rf]))


(rf/reg-fx
  :play-sound
  (fn [sound]
    (sound/play sound)))
