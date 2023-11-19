(ns main.example
  (:require [reagent.core :as r]
            [main.firebase.config :as firebase]
            ["firebase/auth" :as firebase-auth]
            ["firebase/firestore" :as firebase-firestore]
            ["firebase/functions" :as firebase-functions]))


(defonce ^:private *app-state (r/atom {:logged-in? false
                                       :items {}}))


;; DB
(defonce ^:private *collection-unsub (atom nil))

(def ^:private items-collection (firebase-firestore/collection firebase/firestore "items"))


(defn- update-from-sub [change]
  (let [change-type (.-type change)
        id (.. change -doc -id)]
    (cond
      (contains? #{"added" "modified"} change-type)
      (swap! *app-state assoc-in [:items id] (js->clj (.data (.-doc change))))

      (= "removed" change-type)
      (swap! *app-state update :items dissoc id))))


(defn- subscribe! []
  (let [q     (firebase-firestore/query items-collection)
        unsub (firebase-firestore/onSnapshot
                q
                (fn [snapshot]
                  (doseq [change (.docChanges snapshot)]
                    (update-from-sub change))))]
    (reset! *collection-unsub unsub)))


(defn- unsubscribe! []
  (when-let [unsub @*collection-unsub]
    (prn "unsub")
    (unsub)
    (reset! *collection-unsub nil)))


(defn- insert-random-item! []
  (firebase-firestore/addDoc items-collection (clj->js {:value (rand-int 10000)})))

(defn- remove-item! [id]
  (firebase-firestore/deleteDoc
    (firebase-firestore/doc firebase/firestore "items" id)))

;; FUNCTIONS

(def ^:private echo-message (firebase-functions/httpsCallable firebase/functions "echo"))

;; EVENTS

(defn- log-in! []
  (-> (firebase-auth/signInAnonymously firebase/auth)
      (.then (fn []
               (subscribe!)
               (swap! *app-state assoc :logged-in? true)))
      (.catch #(js/alert "Make sure firebase emulators are running"))))


(defn- log-out! []
  (-> (firebase-auth/signOut firebase/auth)
      (.then (fn []
               (unsubscribe!)
               (swap! *app-state assoc :logged-in? false)))))


(defn- add-entity! []
  (-> (insert-random-item!)
      (.catch #(js/alert "Make sure firebase emulators are running"))))

(defn- remove-entity! [id]
  (-> (remove-item! id)
      (.catch #(js/alert "Make sure firebase emulators are running"))))


(defn- call-function! []
  (-> (echo-message {:foo "bar"})
      (.then #(js/alert (str "Function call success on: " (.. % -data -time))))
      (.catch #(js/alert "Make sure firebase emulators are running"))))

;; VIEWS

(defn- logged-in-view [app-state]
  [:div
   [:h3 "Welcome"]
   [:button.paper-btn.btn-primary {:on-click add-entity!} "Add random entity"]
   [:button.paper-btn.btn-primary {:on-click call-function!} "Echo function"]
   [:button {:on-click log-out!} "Log Out"]
   [:ul
    (->> (:items app-state)
         (map (fn [[id item]]
                [:li {:key id}
                 (get item "value")
                 [:a {:on-click #(remove-entity! id)}" (remove)"]])))]])

(def ^:private logged-out-view
  [:div
   [:h3 "Click button to log in"]
   [:button {:on-click log-in!} "Log In"]])

(defn- main-page-view [app-state]
  [:div.paper.container
   (if (:logged-in? app-state)
     [logged-in-view app-state]
     logged-out-view)
   ])


(defn example-app []
  [main-page-view @*app-state])
