(ns hearts.presentation.console
  [:require [clansi :as ansi]])

(defn parse-input-card [s]
  (let [m (re-matcher #"^([2-9]|10|J|Q|K|A)([hdcs])$" s)]
    (if-not (re-find m)
      nil
      (let [[_ rank suit] (re-groups m)
            rank (case rank
                   "J" 9
                   "Q" 10
                   "K" 11
                   "A" 12
                   (- (read-string rank) 2))]
        {:rank rank, :suit (keyword suit)}))))

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
