(ns hearts.app
  (:require [hearts.core :as core]))

(defn show-player-projection [game]
  (let [pos (core/next-player-pos game)]
    (core/player-projection game pos)))

(def game (atom nil))

(defn new-game []
  (swap! game (fn [_] (core/new-game)))
  (show-player-projection @game))
