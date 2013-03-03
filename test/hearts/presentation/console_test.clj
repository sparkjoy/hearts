(ns hearts.presentation.console-test
  (:use clojure.test
        hearts.presentation.console))

(deftest invalid-card-returns-nil
  (is (= nil (parse-input-card "invalid"))))

(deftest parse-two-of-clubs
  (is (= {:rank 0, :suit :c}
         (parse-input-card "2c"))))

(deftest parse-ace-of-hearts
  (is (= {:rank 12, :suit :h}
         (parse-input-card "Ah"))))
