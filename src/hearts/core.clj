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

(defn make-deck []
  {:post [(= 52 (count %))]}
  (for [suit [:s :c :d :h] rank (range 0 13)] {:suit suit :rank rank}))

(defn make-game [deck]
  (let [names ["Alice" "Bob" "Mallory" "John"]
        make-player
        (fn [pos name] 
          {:pos pos
           :name name
           :dealt-cards (-> (partition 13 deck) (nth pos) vec)})]
    {:players (vec (map make-player (range) names))
     :tricks []}))

(defn is-heart [card] (= :h (:suit card)))

(defn hearts-broken [tricks]
  (some #(some is-heart %) tricks))

(defn playing-first-trick [tricks]
  (< (count (first tricks)) 4))

(defn can-lead-with-heart [tricks]
  (and ((complement playing-first-trick) tricks)
       (hearts-broken tricks)))

(defn next-player-pos [game]
  (let [tricks (:tricks game)
        cards-in-last-trick (count (last tricks))
        num-tricks (count tricks)]
    (if (= 4 cards-in-last-trick)
      (if (= 13 num-tricks)
        nil
        0)
      cards-in-last-trick)))

(defn new-game []
  (-> (make-deck) shuffle make-game))
