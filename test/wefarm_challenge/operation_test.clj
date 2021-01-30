(ns wefarm-challenge.operation-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.operation :as op]))

(deftest drawing-lines
  (testing "when horizontal"
    (let [row      4
          start    2
          end      5
          colour   :B

          expected [[2 4 :B] [3 4 :B] [4 4 :B] [5 4 :B]]]
      (is (= expected (op/horizontal-line row start end colour)))))

  (testing "when vertical"
    (let [column   4
          start    1
          end      3
          colour   :G

          expected [[4 1 :G] [4 2 :G] [4 3 :G]]]
      (is (= expected (op/vertical-line column start end colour))))))

(deftest filling
  (let [fillable  [[:0 :0 :B :0 :0]
                   [:0 :T :B :T :T]
                   [:0 :T :B :T :0]
                   [:0 :0 :B :0 :0]
                   [:0 :0 :0 :0 :0]]
        img-state [fillable [5 5]]]

    (testing "a horizontal row"
      (is (=
           [[3 0 :F] [4 0 :F]]
           (sort (op/fill img-state [3 0] :F)))))

    (testing "a vertical row"
      (is (=
           [[2 0 :F]
            [2 1 :F]
            [2 2 :F]
            [2 3 :F]]
           (sort (op/fill img-state [2 3] :F)))))

    (testing "an angle"
      (is (=
           (sort [[3 1 :F] [4 1 :F]
                  [3 2 :F]])
           (sort (op/fill img-state [4 1] :F)))))

    (testing "a complex shape"
      (is (=
           (sort [[0 0 :F] [1 0 :F]
                  [0 1 :F]
                  [0 2 :F] [4 2 :F]
                  [0 3 :F] [1 3 :F] [3 3 :F] [4 3 :F]
                  [0 4 :F] [1 4 :F] [2 4 :F] [3 4 :F] [4 4 :F]])
           (sort (op/fill img-state [0 4] :F)))))))
