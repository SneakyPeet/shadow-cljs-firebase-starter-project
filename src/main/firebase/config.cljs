(ns main.firebase.config
  (:require [shadow.resource :as rc]
            ["firebase/app" :as firebase-app]
            ["firebase/auth" :as firebase-auth]
            ["firebase/firestore" :as firebase-firestore]))

(def firebase-config  (js/JSON.parse (rc/inline "config.json")))

(def app (firebase-app/initializeApp firebase-config))

(def auth (firebase-auth/getAuth))

(def firestore (firebase-firestore/getFirestore))
