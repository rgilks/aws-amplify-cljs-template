(ns app.sound
  (:require ["howler" :refer [Howl]]))

(def sounds
  {:round-start      {:src "/mp3/scanner.mp3" :volume 0.2}
   :add-character    {:src "/mp3/cursor_selection_07.mp3" :volume 0.2}
   :select-character {:src "/mp3/beep_electric.mp3" :volume 0.2}})

(defn play [k]
  (when-let [sound (get sounds k)]
    (js/console.log "sound: " sound)
    (let [howl (Howl. (clj->js sound))]
      (.play howl))))