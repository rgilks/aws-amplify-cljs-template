(ns app.theme)

(def theme
  {:opacity    0.8
   :typography {:body2 {:color "#88FF66"}
                :h1    {:fontFamily "Orbitron"}
                :h2    {:fontFamily "Orbitron"}
                :h3    {:fontFamily "Orbitron"}
                :h4    {:fontFamily "Orbitron"}
                :h5    {:fontFamily "Orbitron"
                        :color      "#88FF66"
                        :fontSize   "1rem"}
                :h6    {:fontFamily "Orbitron"}}

   :palette    {:mode       "dark"
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

   :components
   {;;"MuiPaper"            {:styleOverrides {:root {:border "1px solid #88FF66" :margin "0px" :padding "0px"}}}
    "MuiTextField"
    {:defaultProps {:variant "standard"}}
    "MuiFormControlLabel"
    {:styleOverrides {:label {:fontSize "14px"}}}
    "MuiButton"
    {:styleOverrides {:root {:margin "1px"}}}
    "MuiDivider"
    {:styleOverrides
     {:root {:marginBottom "4px"}}}
    "MuiCard"
    {:styleOverrides
     {:root {:backgroundColor "black"
             :border          "1px solid #00FF00"
             :marginBottom    "5px"}}}
    "MuiCardContent"
    {:styleOverrides
     {:root {:padding "5px",
             :margin  "0px"}}}
    "MuiCardActions"
    {:styleOverrides
     {:root {:padding "3px"
             :margin  "0px"}}}
    "MuiDialog"
    {:styleOverrides
     {:container   {:justifyContent "left"}
      :scrollPaper {:alignItems "start"}
      :paper       {:border "1px solid #00FF00"}}}
    "MuiDialogTitle"
    {:styleOverrides
     {:root {:padding         "0px"
             :paddingLeft     "10px"
             :paddingRight    "10px"
             :margin          "0px"
             :backgroundColor "#88FF66"
             :color           "#000"}}}
    "MuiDialogContent"
    {:styleOverrides
     {:root {:backgroundColor "black"
             :color           "#ffffed"
             :maxWidth        "400px"
             :maxHeight       "700px"}}}
    "MuiDialogActions"
    {:styleOverrides
     {:root {:backgroundColor "black"
             :color           "#ffffed"}}}
    "MuiTab"
    {:styleOverrides
     {:root {:minWidth                   "80px"
             :minHeight                  "40px"
             :padding                    "0px"
             "@media (min-width: 80px)"  {:minWidth "80px"}
             "@media (min-height: 40px)" {:minHeight "40px"}}}}
    "MuiAccordion"
    {:styleOverrides
     {:root {:backgroundColor "black"}}}
    "MuiAccordionSummary"
    {:styleOverrides
     {:root    {:width           "100%"
                :margin          "0px"
                :padding         "0px"
                :backgroundColor "black"
                :borderBottom    "1px dotted #008800"
                :&$expanded      {:margin    "0px"
                                  :padding   "0px"
                                  :minHeight 0}}
      :content {:width      "100%"
                :margin     "9px"
                :padding    "0px"
                :&$expanded {:margin "9px", :padding "0px"}}}}
    "MuiAccordionDetails"
    {:styleOverrides
     {:root {:margin          "0px"
             :padding         "0px"
             :backgroundColor "black"
             :borderBottom    "1px dotted #008800"}}}
    "MuiTableRow"
    {:styleOverrides
     {:hover {:&:hover {:backgroundColor "green !important"}}}}
    "MuiChip"
    {:styleOverrides
     {:labelSmall {:margin  "2px !important"
                   :padding "2px !important"}
      :root       {:borderRadius "0px"
                   :margin       "0px !important"
                   :padding      "0px !important"
                   :marginTop    "-1px !important"
                   :marginLeft   "-1px !important"}}}}})
