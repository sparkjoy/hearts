(ns hearts.core-test
  (:use clojure.test
        hearts.core))

(deftest led-suit-answers-first-suit-in-trick
  (is (= :c (led-suit [{:suit :c} {:suit :d}]))))

(deftest highest-rank-in-led-suit-wins-trick
  (is (= 1 (trick-winner [
    {:suit :d :rank 4}
    {:suit :d :rank 5}
    {:suit :c :rank 6}
    {:suit :s :rank 7}]))))

(deftest led-suit-wins-trick-if-nobody-else-plays-that-suit
  (is (= 0 (trick-winner [
    {:suit :h :rank 4}
    {:suit :d :rank 5}
    {:suit :c :rank 6}
    {:suit :s :rank 7}]))))

(deftest card-score-is-one-for-a-heart
  (is (= 1 (card-score {:suit :h}))))

(deftest card-score-is-zero-for-a-club
  (is (= 0 (card-score {:suit :c}))))

(deftest card-score-for-queen-of-spades-is-thirteen
  (is (= 13 (card-score queen-of-spades))))
