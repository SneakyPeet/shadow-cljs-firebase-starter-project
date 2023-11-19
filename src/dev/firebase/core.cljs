(ns dev.firebase.core
  (:require [dev.firebase.config :as config]))

(goog-define use-auth-emulator? false)
(goog-define use-db-emulator? false)


(defn- init-auth! []
  (when use-auth-emulator?
    (prn (str "Hosting Auth Emulator On: " (config/auth-host-port)))))


(defn init! []
  (init-auth!))
