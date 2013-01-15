(ns hearts.core
  (:gen-class))

(def queen-rank 11)
(def queen-score 13)
(def queen-of-spades {:suit :s, :rank queen-rank})

(defn led-suit [trick]
  (:suit (first trick)))

(defn trick-winner [trick]
  (let [required-suit (led-suit trick)
	indexed-trick (map #(assoc %1 :pos %2) trick (iterate inc 0))
	suited-cards (filter #(= required-suit (:suit %)) indexed-trick)
	ordered-by-rank (sort-by :rank > suited-cards)]
    (:pos (first ordered-by-rank))))

(defn card-score [card]
  (case (:suit card)
	 :h 1
	 :s (if (= queen-rank (:rank card)) queen-score 0)
	 0))

(defn shuffled-deck []
  {:post [(= 52 (count %))]}
  (shuffle (for [suit [:s :c :d :h] rank (range 0 13)] {:suit suit :rank rank})))

(defn deal [deck]
  {:post [(= 4 (count %))]}
  (letfn [(player-cards [pos] (vec (take 13 (take-nth 4 (drop pos deck)))))]
    (vec (map player-cards (range 4)))))

(defn make-game []
  (vec (for [dealt-cards (deal (shuffled-deck))]
		  {
		   :dealt-cards dealt-cards
		   :play-order []
		   })))

(defn -main
  [& args]
  (def game (ref (make-game)))
  println game)