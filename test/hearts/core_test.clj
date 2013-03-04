(ns hearts.core-test
  (:use clojure.test
        hearts.core))

(deftest next-player-at-start-holds-two-clubs
  (with-redefs [first-player (constantly {:pos 2})]
    (is (= 2 (next-player-pos {})))))

(deftest next-player-after-lead-is-determined-by-pos-of-last-card-played
  (is (= 2 (next-player-pos {:tricks [[{:pos 1}]]}))))

(deftest next-player-to-lead-is-determined-by-winner-of-last-trick
  (with-redefs [winning-card (constantly {:pos 3})]
    (is (= 0 (next-player-pos {:tricks [[:card :card :card :card]]})))))

(deftest test-player-projection-for-john
  (let [game {:players [{:name "Alice", :pos 0}
                        {:name "Bob", :pos 1}
                        {:name "Mallory", :pos 2}
                        {:name "John", :pos 3, :dealt-cards [:card]}]
              :tricks [[{:rank 9, :suit :c, :pos 2}]]}]
    (is (= {:players [{:name "Alice"}
                      {:name "Bob"}
                      {:name "Mallory"}
                      {:name "John", :dealt-cards [:card]}]
            :tricks [[{:rank 9, :suit :c, :pos 2}]]}
           (player-projection game 3)))))

(deftest first-player-has-the-two-of-clubs
  (let [players [{}
                 {:name "Bob"
                  :dealt-cards [two-of-clubs]}]]
    (is (= "Bob" (:name (first-player players))))))

(deftest cant-lead-with-heart-on-first-trick
  (with-redefs [playing-first-trick? (constantly true)]
    (is (not (can-lead-with-heart? nil)))))

(deftest cant-lead-with-heart-when-hearts-not-broken
  (with-redefs [playing-first-trick? (constantly false)
                hearts-broken (constantly false)]
    (is (not (can-lead-with-heart? nil)))))

(deftest can-lead-with-hearts-once-hearts-broken-and-past-first-trick
  (with-redefs [playing-first-trick? (constantly false)
                hearts-broken (constantly true)]
    (is (can-lead-with-heart? nil))))

(deftest fourth-play-is-still-first-trick
  (is (playing-first-trick? [[:card :card :card]])))

(deftest fifth-play-is-no-longer-first-trick
  (is (not (playing-first-trick? [[:card :card :card :card]]))))

(deftest hearts-not-broken-if-none-yet-played
  (is (not (hearts-broken [[{:suit :c}]]))))

(deftest hearts-broken-when-one-played
  (is (hearts-broken [[{:suit :h}]])))

(deftest led-suit-answers-first-suit-in-trick
  (is (= :c (led-suit [{:suit :c} {:suit :d}]))))

(deftest highest-rank-in-led-suit-wins-trick
  (is (= 3 (:pos (winning-card [
    {:suit :d, :rank 4, :pos 2}
    {:suit :d, :rank 5, :pos 3}
    {:suit :c, :rank 6, :pos 0}
    {:suit :s, :rank 7, :pos 1}])))))

(deftest led-suit-wins-trick-if-nobody-else-plays-that-suit
  (is (= 3 (:pos (winning-card [
    {:suit :h, :rank 4, :pos 3}
    {:suit :d, :rank 5, :pos 0}
    {:suit :c, :rank 6, :pos 1}
    {:suit :s, :rank 7, :pos 2}])))))

(deftest card-score-is-one-for-a-heart
  (is (= 1 (card-score {:suit :h}))))

(deftest card-score-is-zero-for-a-club
  (is (= 0 (card-score {:suit :c}))))

(deftest card-score-for-queen-of-spades-is-thirteen
  (is (= 13 (card-score queen-of-spades))))
