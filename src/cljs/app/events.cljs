(ns app.events
  (:require
    [re-frame.core :refer [reg-event-db reg-fx reg-event-fx dispatch]]
    ["aws-amplify" :refer [DataStore Auth]]
    [reitit.frontend.controllers :as reitit-controllers]
    [reitit.frontend.easy :as reitit-easy]
    [app.db :refer [default-db]]
    ["models" :refer [Todo]]
    [promesa.core :as p]
    [app.sound :refer [sound]]
    [goog.object :as gobj]
    ))


(def TIMEOUT 300)

(defn sanitize [item]
  (dissoc item :_version :_lastChangedAt :_deleted :_lastChangedBy))

(defn refresh-session []
  (p/let
    [^js cognito-user (.currentAuthenticatedUser Auth)
     ^js current-session (.currentSession Auth)]
    (p/create
      (fn [resolve reject]
        (.refreshSession
          cognito-user
          (.getRefreshToken current-session)
          (fn [err]
            (when err (println "CANNOT_REFRESH_TOKEN" err) (reject err))
            (resolve cognito-user)))))))

(reg-event-db
  ::set-timeout-id
  (fn
    [db [_ id timeout-id]]
    (assoc-in db [:timeout-ids id] timeout-id)))

(reg-event-db
  ::set-user-db
  (fn
    [db [_ username groups]]
    (assoc db :username username :groups groups)))

(defn get-groups [^js cognito-user]
  (-> cognito-user
      .-signInUserSession
      .getAccessToken
      .-payload
      (js->clj :keywordize-keys true)
      :cognito:groups))

(reg-fx
  ::init-fx
  (fn []
    (p/let [cognito-user (refresh-session)
            username (.-username cognito-user)
            groups (get-groups cognito-user)]
           (dispatch [::set-user-db username groups]))))

(reg-event-fx
  ::init-username-and-groups
  (fn
    [{:keys [db]} [_ _]]
    (when (not (:username db)) {::init-fx []})))

;; ----
;; Util
(defn obj->clj [obj]
  (js->clj (-> obj js/JSON.stringify js/JSON.parse) :keywordize-keys true))

;; -------------
;; Subscriptions

(reg-event-db
  ::update-entity-db
  (fn
    [db [_ entity-key id data]]
    (when (not (get-in db [:timeout-ids id]))
      (assoc-in db [entity-key id] data))
    ))

(defn handle-subs [entity-key ^js msg]
  (let [element (.-element msg)
        id      (.-id element)
        opType  (.-opType msg)]
    (if (= opType "DELETE")
      (dispatch [::delete-entity entity-key id])
      (dispatch [::update-entity-db entity-key id (obj->clj element)]))))

(reg-event-fx
  ::subscribe
  (fn
    [_ [_ _]]
    {}))

;; ------
;; Sounds
(reg-event-fx
  ::play-sound
  (fn [_ [_ k]] (sound k) {}))

;; --------
;; Database
(reg-event-db
  ::initialize-db
  (fn [_ _] default-db))

;; ----------
;; Navigation
(reg-fx
  ::push-state
  (fn [route]
    (apply reitit-easy/push-state route)))

(reg-event-fx
  ::push-state
  (fn
    [_ [_ & route]]
    {::push-state route}))

(reg-event-db
  ::navigated
  (fn
    [db [_ new-match]]
    (let [old-controllers (get-in db [:current-route :controllers])
          controllers     (reitit-controllers/apply-controllers
                            old-controllers
                            new-match)]
      (assoc db :current-route (assoc new-match :controllers controllers)))))


(reg-event-db
  ::update-entities-db
  (fn
    [db [_ entity-key data]]
    (assoc db entity-key data)))

(def entity-map
  {:todos Todo
   :todo  Todo
   })

(reg-fx
  ::get-entities
  (fn [entity-key]
    (p/let [result (.query DataStore (entity-key entity-map))
            data (obj->clj result)
            keyed-data (reduce #(assoc %1 (:id %2) %2) {} data)]
           (dispatch [::update-entities-db entity-key keyed-data]))))

(reg-event-fx
  ::get
  (fn
    [_ [_ entity-key]]
    {::get-entities entity-key}))

(def ignore-keys
  #{"_version" "_lastChangedAt" "_deleted" "updatedAt"})

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn update-entity [entity-key id data]
  (p/let [^js entity (entity-key entity-map)
          result (.query DataStore entity id)]
         (.save DataStore
                (.copyOf entity result
                         #(gobj/forEach data
                                        (fn [v k _]
                                          (when (not (in? ignore-keys k))
                                            (gobj/set % k v))))))
         (dispatch [::clear-timeout id])))

(reg-event-db
  ::clear-timeout
  (fn
    [db [_ id]]
    (js/clearTimeout (get-in db [:timeout-ids id]))
    (update db :timeout-ids dissoc id)
    ))

(reg-event-db
  ::update
  (fn
    [db [_ entity-key id data]]
    (js/clearTimeout (get-in db [:timeout-ids id]))
    (assoc-in db [:timeout-ids id]
              (js/setTimeout #(update-entity entity-key id data) TIMEOUT))
    ))


