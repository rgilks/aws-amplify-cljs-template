(ns app.games
  (:require
   ["@mui/icons-material" :as icon]
   ["@mui/material" :as mui]
   ["models" :as models]
   [app.cofx :as cofx]
   [clojure.string :as str]
   [refx.alpha :as refx]
   [uix.core :refer [$ defui]]))

(refx/reg-sub
 ::games
 (fn [db] (:games db)))

(refx/reg-sub
 ::players
 (fn [db] (:players db)))

(refx/reg-sub
 ::username
 (fn [db] (:username db)))

(refx/reg-sub
 ::visible-games
 :<- [::games]
 :<- [::players]
 :<- [::username]
 (fn [[games players username] _]
   (when (some? games)
     (->> games vals
          (filter
           (fn [{:keys [id owner allowSpectators]}]
             (or
            ;;   HACK: allowSpectators is not yet implemented
              (= true true)
            ;;   (= true allowSpectators)
              (= owner username)
              (->> players vals
                   (some
                    (fn [{:keys [owner gameID]}]
                      (and
                       (= owner username)
                       (= gameID id))))))))
          (sort-by :name)))))

(refx/reg-sub
 ::selected
 (fn [db] (::selected db)))

(refx/reg-event-db
 ::select
 (fn [db [_ item]] (assoc db ::selected item)))

(refx/reg-event-fx
 ::new-game
 [(refx/inject-cofx ::cofx/uuid)]
 (fn [{:keys [db uuid]} [_]]
   {:new-item
    [models/Game
     {:owner                  (:username db)
      :name                   "New"
      :slug                   uuid
      :rulesetID              "cepheus-engine-srd"
      :maxCharactersPerPlayer 1
      :allowCharacterDelete   false
      :allowCharacterImport   false
      :allowSpectators        false
      :restrictMovement       false}]}))

(refx/reg-event-fx
 ::delete
 (fn [{:keys [db]} [_ id]]
   {:delete-item [models/Game id]
    :db          (assoc db ::selected nil)}))

(defui delete-modal [{:keys [name]}]
  (let [selected (refx/use-sub [::selected])
        conf-msg (str "Delete the '" (:name selected) "' "
                      (str/lower-case name) " and all associated data?")]
    ($ mui/Dialog
       {:data-testid "delete-confirmation"
        :open        (some? selected)
        :onClose     #(refx/dispatch [::select nil])}
       ($ mui/DialogTitle
          {:data-testid "delete-confirmation-title"
           :title       (str "Delete " name)
           :onClose     #(refx/dispatch [::select nil])})
       ($ mui/DialogContent
          ($ mui/Typography
             {:data-testid "delete-confirmation-msg"
              :style       {:margin-bottom "10px"}} conf-msg)
          ($ mui/Button {:data-testid "game-delete-button"
                         :onClick     #(refx/dispatch [::delete (:id selected)])} "YES")
          ($ mui/Button {:onClick #(refx/dispatch [::select nil])} "NO")))))

(defui list-games [{:keys [games username]}]
  ($ mui/List
     (for [{:keys [id slug name owner allowSpectators] :as game} games]
       ($ mui/ListItem
          {:data-testid "game-link"
           :key         (str "game-" id)
           :button      true
           :component   "a"
           :href        (str "/game/" slug)}
          ($ mui/ListItemText
             {:primary name
              :secondary
              (str "Owner: " owner
                   (when allowSpectators " | Spectators welcome"))})
          (when (= owner username)
            ($ mui/ListItemSecondaryAction
               ($ mui/IconButton
                  {:data-testid "game-link-button"
                   :edge        "end"
                   :onClick     #(refx/dispatch [::select game])
                   :size        "large"}
                  ($ icon/Delete))))))))

(defui view []
  (let [games    (refx/use-sub [::visible-games])
        username (refx/use-sub [::username])]
    ($ mui/Box
       {:style {:height     "100%"
                :padding    "10px"
                :overflow-y "auto"
                :overflow-x "hidden"}}
       ($ delete-modal {:name "Game"})
       ($ mui/Box
          {:style {:margin         "0 auto"
                   :max-width      "374px"
                   :padding        "10px"
                   :padding-bottom "75px"}}
          ($ mui/Typography
             {:data-testid "games-title"
              :variant "h4"
              :style   {:text-align     "center"
                        :padding-bottom "40px"}}
             "APP TITLE")
          ($ mui/Card
             {:data-testid "games-list"}
             ($ mui/CardContent
                {:style {:margin "5px"}}
                ($ :div
                   ($ mui/Typography
                      {:variant "h5" :style {:float "left"}} "Games")
                   ($ mui/Button
                      {:data-testid "add-game-button"
                       :style       {:float "right"}
                       :start-icon  ($ icon/Add)
                       :onClick
                       #(refx/dispatch [::new-game])}
                      "Add New Game")))
             ($ mui/Box
                {:style {:clear       "both"
                         :padding-top "20px"}}
                (if (some? games)
                  ($ list-games {:games games :username username})
                  ($ mui/LinearProgress
                     {:data-testid "loading-games"}))))))))
