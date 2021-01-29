(ns wefarm-challenge.image-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.image :refer :all]))

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

(deftest applying-a-set-of-changes
  (testing "for lines"
    (testing "when horiztonal"
      (let [expected  [[:0 :0 :0 :0 :0]
                       [:0 :T :T :T :T]
                       [:0 :0 :0 :0 :0]
                       [:0 :0 :0 :0 :0]
                       [:0 :0 :0 :0 :0]]]
        (is (= expected (apply-changes img-fixture horiz-line-fixture)))))

    (testing "when vertical"
      (let [expected  [[:0 :0 :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :T :0 :0 :0]
                       [:0 :0 :0 :0 :0]]]
        (is (= expected (apply-changes img-fixture vert-line-fixture)))))))

(deftest applying-one-change
  (testing "for a single pixel"
    (let [expected  [[:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :0 :0]
                     [:0 :0 :0 :T :0]]]
      (is (= expected (apply-pixel img-fixture [4 3 :T]))))))
