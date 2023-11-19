(ns dev.core
  (:require [main.core]
            [dev.firebase.core :as firebase-dev]))


(defn init! []
  (prn "dev.core")
  (firebase-dev/init!)
  (main.core/init!))
