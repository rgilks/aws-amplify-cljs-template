(ns app.schema
  (:require
   [clojure.test.check.generators :as gen]
   [malli.generator :as mg]
   [malli.json-schema :as json-schema]
   [miner.strgen :as sg]))

(def game
  [:map
   {:title "Game"}
   [:id {:title "Id"} uuid?]
   [:slug {:title   "Slug"
           :gen/gen (sg/string-generator #"^[0-9a-z]{1}[-0-9a-z]{2,35}$")
           :json-schema/errorMessage
           #js {:type      "Must be a string."
                :minLength "Must be at least 3 characters long."
                :maxLength "Must be shorter than 36 characters."
                :pattern   "Must be all lowercase separated by dashes."}}
    [:and [:string {:min 3, :max 36}] [:re #"^[0-9a-z](-?[0-9a-z])*$"]]]
   [:name {:title "Name"} string?]
   [:owner {:title "Owner"} string?]
   [:allowSpectators {:title "Allow Spectators"} boolean?]])

(def player
  [:map
   {:title "Player"}
   [:id {:title "Id"} uuid?]
   [:owner {:title "Owner"} string?]
   [:gameID {:title "Game ID"} uuid?]])

(comment
  (mg/generate player)
  (json-schema/transform player)

  (mg/generate game)
  (json-schema/transform game)

  (gen/sample (sg/string-generator #"^[0-9a-z](-?[0-9a-z])*$"))
  (gen/sample (sg/string-generator #"^[0-9a-z]{1}[-0-9a-z]{2,35}$")))