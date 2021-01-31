(ns wefarm-challenge.command-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.command :as cmd]))

(def img-state-fixture
  (let [img  [[:0 :0 :B :0 :0]
              [:0 :T :B :T :T]
              [:0 :T :B :T :0]
              [:0 :0 :B :0 :0]
              [:0 :0 :0 :0 :0]]]
    [img [5 5]]))

(deftest initialising-an-image
  (is (=
       [[[:0 :0 :0]
         [:0 :0 :0]
         [:0 :0 :0]
         [:0 :0 :0]]
        [3 4]]
       (cmd/execute nil "I34"))))

(deftest clearing-an-image
  (is (=
       [[[:0 :0 :0 :0 :0]
         [:0 :0 :0 :0 :0]
         [:0 :0 :0 :0 :0]
         [:0 :0 :0 :0 :0]
         [:0 :0 :0 :0 :0]]
        [5 5]]
       (cmd/execute img-state-fixture "C"))))

(deftest filling-a-pixel
  (is (=
       [[[:0 :0 :B :0 :0]
         [:0 :T :B :T :T]
         [:0 :T :B :T :0]
         [:0 :P :B :0 :0]
         [:0 :0 :0 :0 :0]]
        [5 5]]
       (cmd/execute img-state-fixture "L24P"))))

(deftest filling-a-region
  (testing "on a horizontal row"
    (is (=
         [[[:0 :0 :B :F :F]
           [:0 :T :B :T :T]
           [:0 :T :B :T :0]
           [:0 :0 :B :0 :0]
           [:0 :0 :0 :0 :0]]
          [5 5]]
         (cmd/execute img-state-fixture "F41F"))))

  (testing "on a vertical row"
    (is (=
         [[[:0 :0 :F :0 :0]
           [:0 :T :F :T :T]
           [:0 :T :F :T :0]
           [:0 :0 :F :0 :0]
           [:0 :0 :0 :0 :0]]
          [5 5]]
         (cmd/execute img-state-fixture "F34F"))))

  (testing "forming a complex shape"
    (is (=
         [[[:F :F :B :0 :0]
           [:F :T :B :T :T]
           [:F :T :B :T :F]
           [:F :F :B :F :F]
           [:F :F :F :F :F]]
          [5 5]]
         (cmd/execute img-state-fixture "F15F")))))

(deftest drawing-a-horizontal-line
  (is (=
       [[[:0 :0 :B :0 :0]
         [:0 :H :H :H :T]
         [:0 :T :B :T :0]
         [:0 :0 :B :0 :0]
         [:0 :0 :0 :0 :0]]
        [5 5]]
       (cmd/execute img-state-fixture "H242H"))))

(deftest drawing-a-vertical-line
  (is (=
       [[[:0 :0 :B :0 :0]
         [:0 :T :B :T :V]
         [:0 :T :B :T :V]
         [:0 :0 :B :0 :V]
         [:0 :0 :0 :0 :0]]
        [5 5]]
       (cmd/execute img-state-fixture "V524V"))))

(deftest input-sanitising
  (testing "with whitespace"
    (is (=
         [[[:0 :0 :B :0 :0]
           [:0 :T :B :T :V]
           [:0 :T :B :T :V]
           [:0 :0 :B :0 :V]
           [:0 :0 :0 :0 :0]]
          [5 5]]
         (cmd/execute img-state-fixture " V  5 2 4  V ")))))

(deftest displaying-an-image
  (testing "stdout"
    (is (=
         (str "00B00\n"
              "0TBTT\n"
              "0TBT0\n"
              "00B00\n"
              "00000\n")
         (with-out-str
           (cmd/execute img-state-fixture "S")))))

  (testing "return value"
    (is (with-out-str
          (=
           img-state-fixture
           (cmd/execute img-state-fixture "S"))))))

(deftest bad-input
  (testing "when command not recognised"
    (is (=
         "Command not recognised. Please try again.\n"
         (with-out-str
           (cmd/execute img-state-fixture "E"))))

    (with-out-str
      (is (=
           img-state-fixture
           (cmd/execute img-state-fixture "E")))))

  (testing "when command arity is wrong"
    (is (=
         "Command 'I' expects 2 arguments. Please try again.\n"
         (with-out-str
           (cmd/execute img-state-fixture "I3")))))

  (testing "when param type is wrong"
    (is (=
         "Expected 'I' to be an integer. Please try again.\n"
         (with-out-str
           (cmd/execute img-state-fixture "II3"))))

    (is (=
         "Expected '-' to be a capital letter. Please try again.\n"
         (with-out-str
           (cmd/execute img-state-fixture "L12-"))))

    (with-out-str
      (is (=
           img-state-fixture
           (cmd/execute img-state-fixture "L12-")))))

  (testing "when drawing outside the lines"
    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "single pixel - y > height"
           (cmd/execute img-state-fixture "L16O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "single pixel - x > width"
           (cmd/execute img-state-fixture "L61O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "vertical line - column > width"
           (cmd/execute img-state-fixture "V613O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "vertical line - start > height"
           (cmd/execute img-state-fixture "V162O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "vertical line - end > height"
           (cmd/execute img-state-fixture "V116O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "horizontal line - row > height"
           (cmd/execute img-state-fixture "H136O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "vertical line - start > height"
           (cmd/execute img-state-fixture "H621O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "vertical line - end > height"
           (cmd/execute img-state-fixture "H161O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "fill - y > height"
           (cmd/execute img-state-fixture "F16O"))))

    (is (=
         "The requested operation exceeds image size. Please try again.\n"
         (with-out-str
           ; "fill - x > width"
           (cmd/execute img-state-fixture "F61O"))))

    (with-out-str
      (is (=
           img-state-fixture
           ; "single pixel - y > height"
           (cmd/execute img-state-fixture "L16O"))))))
