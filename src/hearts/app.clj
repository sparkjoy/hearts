(ns hearts.app
  [:require [clansi :as ansi]])
(use 'hearts.core)

(def pretty-suits
  {:h 9825
   :d 9826
   :c 9831
   :s 9828})

(def ansi-styles
  {:h :magenta
   :d :cyan
   :c :green
   :s :white})

(defn pretty-rank [rank]
  (if (< rank 9)
    (+ 2 rank)
    (case rank
      9 "J"
      10 "Q"
      11 "K"
      12 "A")))

(defn pretty-card [card]
  (let [suit (:suit card)
        rank (:rank card)
        pretty-suit (char (pretty-suits suit))
        style (ansi-styles suit)]
    (ansi/style (str (pretty-rank rank) pretty-suit) style)))

(defn format-hand [cards]
  (let [sorted-cards (sort-by (juxt :suit :rank) cards)]
    (apply str (interpose " " (map pretty-card sorted-cards)))))

(defn -main [& args]
  (let [game (new-game)
        first-player-name (-> game :players first-player :name)]
    (doseq [player (:players game)]
      (println (:name player) (-> player :dealt-cards format-hand)))
    (println first-player-name "plays first.")))

      
