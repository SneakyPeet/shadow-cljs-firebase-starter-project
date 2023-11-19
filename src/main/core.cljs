(ns main.core
  (:require [reagent.core :as r]
            [goog.dom :as gdom]
            ["react-dom/client" :refer [createRoot]]
            [main.example :as example-app]))

(def ^:private root-element-id "root")

(defonce root (createRoot (gdom/getElement root-element-id)))


(defn render! []
  (.render root (r/as-element [example-app/example-app])))


(defn init! []
  (render!))


(defn ^:dev/after-load re-render!
  []
  (render!))
