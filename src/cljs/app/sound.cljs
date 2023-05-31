(ns app.sound
  (:require
   ["howler" :refer [Howl]]
   [refx.alpha :as refx]))

(def sounds
  {:button-press {:src "cursor_click_06.mp3" :volume 0.2}})

(defn play [k]
  (when-let [sound (get sounds k)]
    (js/console.log "sound: " sound)
    (let [howl (Howl. (clj->js sound))]
      (.play howl))))

(refx/reg-fx
 :play-sound
 (fn [sound]
   (play sound)))
