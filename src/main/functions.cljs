(ns main.functions
  (:require ["firebase-admin/app" :as firebase-app]
            ["firebase-functions/v2" :as functions]
            ["firebase-functions/v2/https" :as functions-https]))


(defonce app (firebase-app/initializeApp))


(defn echo
  "Echo the passed in query parameters merged with the current time"
  [req res]
  (functions/logger.log "hello")
  (.send res (clj->js {:data {:time  (.toString (js/Date.))}})))


(def exports
  #js {:echo (functions-https/onRequest echo)})
