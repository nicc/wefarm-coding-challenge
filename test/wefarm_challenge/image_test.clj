(ns wefarm-challenge.image-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.image :as image]))

(def img-fixture
  ; "an empty 5x5 test image"
  [[:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]
   [:0 :0 :0 :0 :0]])

(def horiz-line-fixture
  ; "pixel delta for a horizontal line from [1 1] to [1 4] in T colour"
  [[1 1 :T] [1 2 :T] [1 3 :T] [1 4 :T]])

(def vert-line-fixture
  ; "pixel delta for a horizontal line from [1 1] to [3 1] in T colour"
  [[1 1 :T]
   [2 1 :T]
   [3 1 :T]])

(deftest initialising
  (is (= img-fixture (image/init 5 5)))
  (is (= (take 3 img-fixture) (image/init 5 3)))
  (is (= (map (partial take 3) img-fixture) (image/init 3 5))))

(deftest applying-a-set-of-changes
  (testing "for lines"
    (testing "when horizontal"
      (let [expected  [[:0 :0 :0 :0 :0]
                       [:0 :T :T :T :T]
                       [:0 :0 :0 :0 :0]
                       [:0 :0 :0 :0 :0]
                       [:0 :0 :0 :0 :0]]]
        (is (= expected (image/apply-changes img-fixture horiz-line-fixture)))))

    (testing "when vertical"
      (let [expected  [[:0 :0 :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :0 :0 :0 :0]]]
        (is (= expected (image/apply-changes img-fixture vert-line-fixture)))))))

(deftest applying-one-change
  (testing "to a single pixel"
    (let [expected  [[:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :T :0]]]
      (is (= expected (image/apply-pixel img-fixture [4 3 :T]))))))
