(ns hearts.core-test
  (:use clojure.test
        hearts.core))

(deftest cant-lead-with-heart-on-first-trick
  (with-redefs [playing-first-trick (constantly true)]
    (is (not (can-lead-with-heart nil)))))

(deftest cant-lead-with-heart-when-hearts-not-broken
  (with-redefs [playing-first-trick (constantly false)
                hearts-broken (constantly false)]
    (is (not (can-lead-with-heart nil)))))

(deftest can-lead-with-hearts-once-hearts-broken-and-past-first-trick
  (with-redefs [playing-first-trick (constantly false)
                hearts-broken (constantly true)]
    (is (can-lead-with-heart nil))))

(deftest fourth-play-is-still-first-trick
  (is (playing-first-trick [[:card :card :card]])))

(deftest fifth-play-is-no-longer-first-trick
  (is (not (playing-first-trick [[:card :card :card :card]]))))

(deftest hearts-not-broken-if-none-yet-played
  (is (not (hearts-broken [[{:suit :c}]]))))

(deftest hearts-broken-when-one-played
  (is (hearts-broken [[{:suit :h}]])))

(deftest pos-zero-plays-first
  (is (zero? (next-player-pos {:tricks []}))))

(deftest pos-one-plays-next
  (is (= 1 (next-player-pos {:tricks [[{:suit :c}]]}))))

(deftest pos-zero-plays-first-on-second-trick
  (is (zero? (next-player-pos {:tricks [[{}{}{}{}]]}))))

;(deftest next-player-pos-answers-nil-at-game-end
 ; (is (nil? (next-player-pos {:tricks (vec (repeat 13 [{}{}{}{}]))}))))

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
