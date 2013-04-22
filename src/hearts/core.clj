(ns hearts.core
  (:gen-class))

(def queen-score 13)
(def queen-of-spades {:suit :s, :rank 11})
(def two-of-clubs {:suit :c, :rank 0})

(defn player-projection
  [game player-idx]
  (let [project-player (fn [idx player]
                         (let [ks (if (= idx player-idx)
                                    [:name :dealt-cards]
                                    [:name])]
                           (select-keys player ks)))]
  {:players (vec (map-indexed project-player (:players game)))
   :tricks (:tricks game)}))

(defn led-suit
  [trick]
  (:suit (first trick)))

(defn winning-card
  [trick]
  (let [required-suit (led-suit trick)
	suited-cards (filter #(= required-suit (:suit %)) trick)
	ordered-by-rank (sort-by :rank > suited-cards)]
    (first ordered-by-rank)))

(defn card-score
  [card]
  (let [queen-rank (:rank queen-of-spades)]
    (case (:suit card)
      :h 1
      :s (if (= queen-rank (:rank card)) queen-score 0)
      0)))

(defn make-deck
  []
  {:post [(= 52 (count %))]}
  (for [suit [:s :c :d :h] rank (range 0 13)] {:suit suit :rank rank}))

(defn make-game
  [deck]
  (let [names ["Alice" "Bob" "Mallory" "John"]
        make-player
        (fn [pos name] 
          {:pos pos
           :name name
           :dealt-cards (-> (partition 13 deck) (nth pos) vec)})]
    {:players (vec (map make-player (range) names))
     :tricks []}))

(defn is-heart
  "Returns card if a heart, otherwise nil"
  [card]
  (when (= :h (:suit card))
    card))

(defn playing-first-trick?
  [tricks]
  (< (count (first tricks)) 4))

(defn hearts-broken
  "Hearts are broken if a heart has been played"
  [tricks]
  (some #(some is-heart %) tricks))

(defn can-lead-with-heart?
  "You can't lead with a heart on the first trick, or if a heart hasn't yet been played"
  [tricks]
  (and ((complement playing-first-trick?) tricks)
       (hearts-broken tricks)))

(defn first-player 
  "The player with the two of clubs plays first."
  [players]
  (some (fn [player]
          (when (some #(= two-of-clubs %)
                      (:dealt-cards player))
            player))
        players))

(defn trick-in-play 
  "A trick is in play if someone has lead, but all cards have not yet been played"
  [ts]
  (when-let [last-trick (last ts)]
    (let [num-cards-played (count last-trick)]
      (when (and (> num-cards-played 0) (< num-cards-played 4))
        last-trick))))

(defn next-player-pos 
  "This determines who is up to play"
  [game]
  (let [ts (:tricks game)]
    (if (zero? (count ts))
      (-> game first-player :pos)
      (if-let [tip (trick-in-play ts)]
        (mod (-> tip last :pos inc) 4)
        (-> ts last winning-card :pos)))))

(defn new-game 
  "Shuffles a deck and deals a new set of cards to all four players"
  []
  (-> (make-deck) shuffle make-game))

(defn play-card [game card]
  (assoc game :tricks
         (let [tricks (:tricks game)]
           (if-let [tip (trick-in-play (:tricks game))]
             (conj (butlast tricks) (conj tip card))
             (conj tricks [card])))))
             
