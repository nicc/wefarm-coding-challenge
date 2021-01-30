(ns wefarm-challenge.image-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.image :as image]))

(def img-fixture
  [[:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]])

(def img-state-fixture
  [img-fixture [5 5]])

(def horiz-line-fixture
  ; "pixel delta for a horizontal line from [1 1] to [1 4] in T colour"
  [[1 1 :T] [2 1 :T] [3 1 :T] [4 1 :T]])

(def vert-line-fixture
  ; "pixel delta for a horizontal line from [1 1] to [3 1] in T colour"
  [[1 1 :T]
   [1 2 :T]
   [1 3 :T]])

(deftest initialising
  (is (= img-state-fixture (image/init [5 5])))
  (is (= (take 3 img-fixture) (first (image/init [5 3]))))
  (is (= (map (partial take 3) img-fixture) (first (image/init [3 5])))))

(deftest retrieving-a-pixel
  (let [img  [[:A :B :C]
              [:D :E :F]
              [:G :H :I]]]
    (is (= :B (image/get-pixel img [1 0])))
    (is (= :F (image/get-pixel img [2 1])))
    (is (= :G (image/get-pixel img [0 2])))))

(deftest getting-neighbours
  (testing "for a pixel"

    (testing "in the middle"
      (is (= [[1 2] [2 1] [2 3] [3 2]] (sort (image/get-neighbours [5 5] [2 2])))))

    (testing "in the top left corner"
      (is (= [[0 1] [1 0]] (sort (image/get-neighbours [5 5] [0 0])))))

    (testing "in the bottom right corner"
      (is (= [[3 4] [4 3]] (sort (image/get-neighbours [5 5] [4 4])))))

    (testing "on the right margin"
      (is (= [[3 2] [4 1] [4 3]] (sort (image/get-neighbours [5 5] [4 2])))))))

(deftest applying-a-set-of-changes
  (testing "for lines"
    (testing "when horizontal"
      (let [expected  [[[:0 :0 :0 :0 :0]
                        [:0 :T :T :T :T]
                        [:0 :0 :0 :0 :0]
                        [:0 :0 :0 :0 :0]
                        [:0 :0 :0 :0 :0]]
                       [5 5]]]
        (is (= expected (image/apply-changes img-state-fixture horiz-line-fixture)))))

    (testing "when vertical"
      (let [expected  [[[:0 :0 :0 :0 :0]
                        [:0 :T :0 :0 :0]
                        [:0 :T :0 :0 :0]
                        [:0 :T :0 :0 :0]
                        [:0 :0 :0 :0 :0]]
                       [5 5]]]
        (is (= expected (image/apply-changes img-state-fixture vert-line-fixture)))))))

(deftest applying-one-change
  (testing "to a pixel"
    (let [expected  [[[:0 :0 :0 :0 :0]
                      [:0 :0 :0 :0 :0]
                      [:0 :0 :0 :0 :0]
                      [:0 :0 :0 :0 :0]
                      [:0 :0 :0 :T :0]]
                     [5 5]]]
      (is (= expected (image/apply-pixel img-state-fixture [3 4 :T]))))))

(deftest displaying-an-image
  (is (=
       (str "00000\n"
            "00000\n"
            "00000\n"
            "00000\n"
            "00000\n")
       (with-out-str
         (image/display img-state-fixture)))))
