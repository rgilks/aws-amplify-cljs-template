(ns app.sound
  (:require ["howler" :refer [Howl]]))

(def sounds
  {
   :button "/mp3/cursor_click_06.mp3"
   })

(defn sound [k]
  (let [sound (get sounds k)]
    (when sound
      (js/console.log "sound: " sound)
      (let [howl (Howl. (clj->js {:src sound}))]
        (.play howl)))))