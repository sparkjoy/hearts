(ns hearts.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Welcome to Hearts in Clojure! I don't do much yet..."))

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

