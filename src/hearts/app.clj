(ns hearts.app
  (:require [hearts.core :as core])
  (:require [hearts.presentation.console :as prs]))

(defn show-player-projection [game]
  (let [pos (core/next-player-pos game)]
    (core/player-projection game pos)))

(def game (atom nil))

(defn play [s]
  (swap! game core/play-card (prs/parse-input-card s))
  (show-player-projection @game))

(defn new-game []
  (swap! game (fn [_] (core/new-game)))
  (show-player-projection @game))

(defn main- []
  nil)
