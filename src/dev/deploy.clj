(ns dev.deploy
  (:require [clojure.string :as string]))

(def index-main-module "main.js")
(def index-html-path "firebase/public/index.html")
(def manifest-path "firebase/public/js/manifest.edn")

(defn get-js-output-name
  {:dev/future "This assumes a single output module"}
  []
  (->> (slurp manifest-path)
       read-string
       first
       :output-name))


(defn replace-js-module-name
  {:dev/tech-debt "This name is not great"}
  []
  (let [output-name (get-js-output-name)
        html        (slurp index-html-path)
        new-html    (string/replace-first
                   html
                   index-main-module
                   output-name)]
    (spit index-html-path new-html)))


(defn app-post-build-release-hook
  {:shadow.build/stage :flush}
  [build-state & args]
  (prn "Updating js  module path in index.html")
  (replace-js-module-name)
  build-state)
