(ns main.example
  (:require [reagent.core :as r]
            [main.firebase.config :as firebase]
            ["firebase/auth" :as firebase-auth]))


(defonce ^:private *app-state (r/atom {:logged-in? false}))

;; EVENTS

(defn- log-in! []
  (-> (firebase-auth/signInAnonymously firebase/auth)
      (.then #(swap! *app-state assoc :logged-in? true))
      (.catch #(js/alert "Make sure firebase emulators are running"))))


(defn- log-out! []
  (-> (firebase-auth/signOut firebase/auth)
      (.then #(swap! *app-state assoc :logged-in? false))))

;; VIEWS

(defn- logged-in-view []
  [:div
   [:h3 "Welcome"]
   [:button {:on-click log-out!} "Log Out"]])

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
