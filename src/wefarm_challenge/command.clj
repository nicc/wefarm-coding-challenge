(ns wefarm-challenge.command
  (:require [clojure.string :as str]
            [wefarm-challenge.image :as img]
            [wefarm-challenge.operation :as op]))

(defn- param->int
  "Convert an input param to an integer"
  [p]
  (if
    (re-matches #"^[0-9]$" p)
    (Integer/parseInt  p)
    (throw (AssertionError. (str "Could not convert '" p "' to integer.")))))

; "Convert an input param to a position index"
(def param->idx (comp dec param->int))

(def directory
  {:I (fn [_ size]
        (img/init (map param->int size)))

   :C (fn [[_ size] & _]
        (img/init size))

   :L (fn [img-state [x y colour]]
        (img/apply-pixel
         img-state
         [(param->idx x) (param->idx y) (keyword colour)]))

   :V (fn [img-state [column start end colour]]
        (img/apply-changes
         img-state
         (op/vertical-line
          (param->idx column)
          (param->idx start)
          (param->idx end)
          (keyword colour))))

   :H (fn [img-state [start end row colour]]
        (img/apply-changes
         img-state
         (op/horizontal-line
          (param->idx row)
          (param->idx start)
          (param->idx end)
          (keyword colour))))

   :F (fn [img-state [x y colour]]
        (img/apply-changes
         img-state
         (op/fill
          img-state
          [(param->idx x) (param->idx y)]
          (keyword colour))))

   :S (fn [img-state & _] (img/display img-state))

   :X (fn [_] (println "TODO: EXIT! ")) })

(defn execute
  "Invokes the appropriate function for a given input command"
  [img-state input]
  (let [[cmd & args] (map str (seq input))
        cmd-key      (keyword (str/upper-case cmd))]
    (apply
      (directory cmd-key)
      (concat [img-state] [args]))))
