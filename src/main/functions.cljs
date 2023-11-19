(ns main.functions
  (:require ["firebase-admin/app" :as firebase-app]
            ["firebase-functions/v2" :as functions]
            ["firebase-functions/v2/https" :as functions-https]))


(defonce app (firebase-app/initializeApp))


(defn echo
  "Function callable from the front end"
  [req]
  (functions/logger.log "hello")
  (clj->js {:time (.toString (js/Date.))}))


(def exports
  #js {:echo (functions-https/onCall echo)})
