(ns wefarm-challenge.operation
  (:require [wefarm-challenge.image :as img]
            [clojure.set :as set-ops]))

; TODO: pre (and post?) conditions to assert valid ranges

(defn- line
  "Calculates a set of changes depicting a generic line.
   Currently only supports straight ones."
  [f-mutator start end]
  (map f-mutator (range start (inc end))))

(defn horizontal-line
  "Calculates a set of changes depicting a horizontal line"
  [row start end colour]
  (letfn  [(f-mutator [x] [x row colour])]
    (line f-mutator start end)))

(defn vertical-line
  "Calculates a set of changes depicting a vertical line"
  [column start end colour]
  (letfn  [(f-mutator [y] [column y colour])]
    (line f-mutator start end)))

(defn fill
  "Calculates a set of changes to fill a region"
  [[img size] pixel colour]
  (let [region-colour   (img/get-pixel img pixel)
        fn-in-region?   (fn [px]
                          (= region-colour (img/get-pixel img px)))
        fn-add-colour   (fn [px]
                          (conj px colour))]

    (loop [result    #{pixel}
           to-check  [pixel]]

      (let [neighbours      (mapcat (partial img/get-neighbours size) to-check)
            new-neighbours  (set-ops/difference (set neighbours) result)
            new-region      (filter fn-in-region? new-neighbours)]

        ; @WF: Okay there's some stuff going on here. Maybe we can discuss further on a call.
        ; A wide block of lets as above usually indicates an imperative style. Do this, then that, etc.
        ; This could be pipelined as below but then we have that weird (#( ___ )) thing from using
        ; a lambda in arrows just to swap argument order (-> vs ->>).
        ; Something like Haskell's flip function would allow us to pipeline this more elegantly.
        ; Just a note to say I realise the above is a bit imperative but it seemed the best choice.
        ; [new-region      (->>
        ;                   to-check
        ;                   (mapcat (partial img/get-neighbours img-size))
        ;                   (#(set-ops/difference (set %) result))
        ;                   (filter fn-in-region?))]

        (if (empty? new-region)
          (map fn-add-colour result)
          (recur
            (into result new-region)
            new-region))))))
