(ns wefarm-challenge.command
  (:require [clojure.string :as str]
            [wefarm-challenge.image :as img]
            [wefarm-challenge.operation :as op]))

(def directory
  {:I (fn [_ size]
        (img/init size))
   :C (fn [[_ size] & _]
        (img/init size))
   :L (fn [img-state [x y colour]]
        (img/apply-pixel img-state [x y colour]))
   :V (fn [img-state [column start end colour]]
        (img/apply-changes
         img-state
         (op/vertical-line column start end colour)))
   :H (fn [img-state [start end row colour]]
        (img/apply-changes
         img-state
         (op/horizontal-line row start end colour)))
   :F (fn [img-state [x y colour]]
        (img/apply-changes
         img-state
         (op/fill img-state [x y] colour)))
   :S (fn [img-state & _] (img/display img-state))
   :X (fn [_] (println "TODO: EXIT! ")) })

(defn execute
  "Invokes the appropriate function for a given input command"
  [img-state inpt]
  (let [[cmd & args] inpt
        cmd-key      (keyword (str/upper-case cmd))]
    (apply
      (directory cmd-key)
      (concat [img-state] [args]))))
; TODO: each img command returns an image but they should all return full img-state to allow easy threading
