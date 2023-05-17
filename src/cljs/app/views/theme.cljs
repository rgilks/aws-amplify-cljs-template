(ns app.views.theme)

(def theme
  {:typography {:body2 {:color "#88FF66"}
                :h1    {:fontFamily "Orbitron"}
                :h2    {:fontFamily "Orbitron"}
                :h3    {:fontFamily "Orbitron"}
                :h4    {:fontFamily "Orbitron"}
                :h5    {:fontFamily "Orbitron"
                        :color      "#88FF66"
                        :fontSize   "1rem"}
                :h6    {:fontFamily "Orbitron"}}

   :palette    {:type       "dark"
                :background {:default "black"
                             :paper   "black"}
                :text       {:primary   "#FFF"
                             :secondary "#88FF66"
                             :disabled  "#FFF"}
                :primary    {:light        "#FFF"
                             :main         "#88FF66"
                             :dark         "#00FF00"
                             :contrastText "#000"}
                :secondary  {:light        "#ffffff"
                             :main         "#ffffed"
                             :dark         "#ecd5bb"
                             :contrastText "#000000"}}

   :components {"MuiPaper"
                {:styleOverrides
                 {:root {:border  "1px solid #88FF66"
                         :margin  "0px"
                         :padding "0px"}}}

                }})