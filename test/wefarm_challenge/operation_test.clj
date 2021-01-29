(ns wefarm-challenge.operation-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.operation :refer :all]))

(deftest lines
  (testing "when horizontal"
    (let [row      4
          start    2
          end      5
          colour   :B

          expected [[2 4 :B] [3 4 :B] [4 4 :B] [5 4 :B]]]
      (is (= expected (horizontal-line row start end colour)))))

  (testing "when vertical"
    (let [column   4
          start    1
          end      3
          colour   :G

          expected [[4 1 :G] [4 2 :G] [4 3 :G]]]
      (is (= expected (vertical-line column start end colour))))))
