(ns app.games-test
  (:require
   ["@testing-library/react" :as rtl]
   ["models" :as models]
   [app.cofx :as cofx]
   [app.games :as games]
   [app.schema :as schema]
   [app.util :as util]
   [clojure.test :refer-macros [deftest is testing use-fixtures]]
   [malli.generator :as mg]
   [refx.alpha :as refx]
   [uix.core :refer [$]]))

;; (deftest new-game
;;   (rf-test/run-test-sync
;;     (rf/reg-cofx
;;       ::cofx/uuid
;;       #(assoc % :uuid "916a7ede-ff97-4d3f-ba31-b498dcf1f498"))
;;     (rf/reg-fx
;;       ::fx/new-item
;;       (fn [[model item]]
;;         (is (= models/Game model))
;;         (is (= item
;;               {:slug                   "916a7ede-ff97-4d3f-ba31-b498dcf1f498"
;;                :maxCharactersPerPlayer 1
;;                :allowCharacterImport   false
;;                :rulesetID              "cepheus-engine-srd"
;;                :name                   "New"
;;                :allowCharacterDelete   false
;;                :restrictMovement       false
;;                :owner                  "username"
;;                :allowSpectators        false}))))
;;     (rf/dispatch [::event/init-db {:username "username"}])
;;     (rf/dispatch [::games/new-game])))

;; (deftest new-game-click
;;   (rf-test/run-test-sync
;;     (rf/reg-cofx
;;       ::cofx/uuid
;;       #(assoc % :uuid "916a7ede-ff97-4d3f-ba31-b498dcf1f498"))
;;     (rf/reg-fx
;;       ::fx/new-item
;;       (fn [[model item]]
;;         (is (= models/Game model))
;;         (is (= item
;;               {:slug                   "916a7ede-ff97-4d3f-ba31-b498dcf1f498"
;;                :maxCharactersPerPlayer 1
;;                :allowCharacterImport   false
;;                :rulesetID              "cepheus-engine-srd"
;;                :name                   "New"
;;                :allowCharacterDelete   false
;;                :restrictMovement       false
;;                :owner                  "username"
;;                :allowSpectators        false}))))
;;     (rf/dispatch [::event/init-db {:username "username"}])
;;     (let [comp    (test/render ($ games/view))
;;           add-btn (.getByTestId comp "add-game-button")]
;;       (.click rtl/fireEvent add-btn)
;;       ;; check appears in list?
;;       (.unmount comp))))

;; (deftest delete-game
;;   (rf-test/run-test-sync
;;     (rf/reg-fx
;;       ::fx/delete-item
;;       (fn [[model id]]
;;         (is (= models/Game model))
;;         (is (= "game-id" id))))
;;     (let [game (mg/generate schema/game)]
;;       (rf/dispatch [::event/init-db {::games/selected game}])
;;       (is (= game @(rf/subscribe [::games/selected])))
;;       (rf/dispatch [::games/delete "game-id"])
;;       (is (nil? @(rf/subscribe [::games/selected]))))))

;; (deftest displays-game-loader
;;   (rf-test/run-test-sync
;;     (rf/dispatch [::event/init-db {:games nil}])
;;     (let [comp   (test/render ($ games/view))
;;           loader (.getByTestId comp "loading-games")]
;;       (is (some? loader))
;;       (.unmount comp))))

;; (deftest game-selected
;;   (rf-test/run-test-sync
;;     (let [game (assoc (mg/generate schema/game) :name "Game A")]
;;       (rf/dispatch [::event/init-db {::games/selected game}])
;;       (let [comp     (test/render ($ games/delete-modal {:name "Game"}))
;;             get-text #(-> comp (.getByTestId %) .-textContent)
;;             title    (get-text "delete-confirmation-title")
;;             content  (get-text "delete-confirmation-msg")]
;;         (is (= "Delete Game" title))
;;         (is (= "Delete the 'Game A' game and all associated data?" content))
;;         (.unmount comp)))))

;; (deftest delete-selected-game-click
;;   (rf-test/run-test-sync
;;     (let [game (assoc (mg/generate schema/game) :id "the-id")]
;;       (rf/reg-fx
;;         ::fx/delete-item
;;         (fn [[model id]]
;;           (is (= models/Game model))
;;           (is (= "the-id" id))))
;;       (rf/dispatch [::event/init-db {::games/selected game}])
;;       (is (= game @(rf/subscribe [::games/selected])))
;;       (let [comp       (test/render ($ games/delete-modal {:name "Game"}))
;;             delete-btn (.getByTestId comp "game-delete-button")]
;;         (.click rtl/fireEvent delete-btn)
;;         (is (nil? @(rf/subscribe [::games/selected])))
;;         (.unmount comp)))))

;; (deftest game-not-selected
;;   (rf/dispatch [::event/init-db {::games/selected nil}])
;;   (let [comp   (test/render ($ games/delete-modal {:name "Game"}))
;;         title? (-> comp (.queryAllByText "Delete Game") .-length pos?)]
;;     (is (false? title?))
;;     (.unmount comp)))

;; (deftest is-game-owner
;;   (rf-test/run-test-sync
;;     (let [game (assoc (mg/generate schema/game)
;;                  :id "458094ac-f654-4ca9-b1e5-6f044cf8f118"
;;                  :slug "test-game-a"
;;                  :name "Game A"
;;                  :owner "username"
;;                  :allowSpectators false)
;;           comp (test/render ($ games/list-games
;;                               {:games [game] :username "username"}))
;;           link (-> comp (.getByTestId "game-link"))]
;;       (is (= "/game/test-game-a" (.getAttribute link "href")))
;;       (is (test/text? comp "Game A"))
;;       (is (test/text? comp "Owner: username"))
;;       (is (true?
;;             (-> comp (.queryAllByTestId "game-link-button") .-length pos?))))))

;; (deftest is-not-game-owner
;;   (rf-test/run-test-sync
;;     (let [game (mg/generate schema/game)
;;           comp (test/render ($ games/list-games
;;                               {:games [game] :username "fred"}))]
;;       (is (false?
;;             (-> comp (.queryAllByTestId "game-link-button") .-length pos?))))))

;; (deftest spectators-allowed
;;   (rf-test/run-test-sync
;;     (let [game (assoc (mg/generate schema/game)
;;                  :allowSpectators true
;;                  :owner "username")
;;           comp (test/render ($ games/list-games
;;                               {:games [game] :username "username"}))]
;;       (is (test/text? comp "Owner: username | Spectators welcome")))))

;; (deftest no-games
;;   (rf-test/run-test-sync
;;     (rf/dispatch [::event/init-db
;;                   {:games   {}
;;                    :players {} :username "jimmy"}])
;;     (let [visible-games @(rf/subscribe [::games/visible-games])]
;;       (is (= [] visible-games)))))

;; (deftest no-games-with-spectators
;;   (rf-test/run-test-sync
;;     (let [game-a (assoc (mg/generate schema/game)
;;                    :username "jimmy"
;;                    :allowSpectators false)]
;;       (rf/dispatch [::event/init-db
;;                     {:games   (ut/idx game-a)
;;                      :players {} :username "jimmy"}])
;;       (let [visible-games @(rf/subscribe [::games/visible-games])]
;;         (is (= [] visible-games))))))

;; (deftest one-game-with-spectators
;;   (rf-test/run-test-sync
;;     (let [game-a (assoc (mg/generate schema/game)
;;                    :username "jimmy"
;;                    :allowSpectators true)]
;;       (rf/dispatch [::event/init-db
;;                     {:games   (ut/idx game-a)
;;                      :players {} :username "jimmy"}])
;;       (let [visible-games @(rf/subscribe [::games/visible-games])]
;;         (is (= [game-a] visible-games))))))

;; (deftest one-game-with-spectators-and-one-without
;;   (rf-test/run-test-sync
;;     (testing
;;      (let [game-a (assoc (mg/generate schema/game)
;;                     :username "jimmy"
;;                     :allowSpectators false)
;;            game-b (assoc (mg/generate schema/game)
;;                     :allowSpectators true)]
;;        (rf/dispatch [::event/init-db
;;                      {:games   (ut/idx game-a game-b)
;;                       :players {} :username "jimmy"}])
;;        (let [visible-games @(rf/subscribe [::games/visible-games])]
;;          (is (= [game-b] visible-games)))))))
