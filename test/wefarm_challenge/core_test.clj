(ns wefarm-challenge.core-test
  (:require [clojure.test :refer :all]
            [wefarm-challenge.core :as core]))

; with more time I would get this working
; (deftest accepts-user-input
;   (is (=
;        "\n"
;        (with-out-str
;          (with-in-str "I11\nS"
;            (with-redefs [cmd/exit-program (fn [] "Okay we're done.")]
;              (core/-main)))))))
