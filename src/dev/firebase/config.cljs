(ns dev.firebase.config
  (:require [main.core]
            [shadow.resource :as rc]))

(def config (js->clj (js/JSON.parse (rc/inline "firebase.json"))))


(defn auth-host-port []
  [(get-in config ["emulators" "auth" "host"])
   (get-in config ["emulators" "auth" "port"])])
