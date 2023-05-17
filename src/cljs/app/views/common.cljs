(ns app.views.common
  (:require
    [re-frame.core :refer [dispatch]]
    ["@mui/material" :refer [Button]]
    [app.events :as events]))

(defn sml-button [on-click label]
  [:> Button
   {:size    "small"
    :variant "contained"
    :color   "primary"
    :onClick #(do (dispatch [::events/play-sound :button]) (on-click))
    } label])






