(ns dev.firebase.core
  (:require [dev.firebase.config :as dev-config]
            [main.firebase.config :as config]
            ["firebase/auth" :as firebase-auth]
            ["firebase/firestore" :as firebase-firestore]))


(goog-define use-auth-emulator? false)
(goog-define use-firestore-emulator? false)


(defn- init-auth-emulator! []
  (let [[host port] (dev-config/auth-host-port)
        location    (str "http://" host ":" port)]

    (firebase-auth/connectAuthEmulator config/auth location)
    (prn (str "Using Auth Emulator On: " location))))


(defn- init-firestore-emulator! []
  (let [[host port] (dev-config/firestore-host-port)
        location    (str "http://" host ":" port)]
    (firebase-firestore/connectFirestoreEmulator config/firestore host port)
    (prn (str "Using Firestore Emulator On: " location))))


(defn init! []
  (when use-auth-emulator? (init-auth-emulator!))
  (when use-firestore-emulator? (init-firestore-emulator!)))
