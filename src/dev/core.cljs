(ns dev.core
  (:require [main.core]
            [dev.firebase.config :as firebase]))


(defn init! []
  (prn "dev.core")
  (prn firebase/config)
  (main.core/init!))
