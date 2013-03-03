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

(defn pretty-print-cards [cards]
  (let [sorted-cards (sort-by (juxt :suit :rank) cards)]
    (println (apply str (interpose " " (map pretty-card sorted-cards))))))

(defn -main [& args]
  (let [cards (-> (new-game) :players first :dealt-cards)]
    (pretty-print-cards cards)))

    
  
  
