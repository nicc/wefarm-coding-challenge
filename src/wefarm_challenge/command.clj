(ns wefarm-challenge.command
  (:require [clojure.string :as str]
            [wefarm-challenge.image :as img]
            [wefarm-challenge.operation :as op]))

(defn- exit-program
  "Wrapped exit so that tests can stub/redef"
  []
  (println "Okay bye! Thanks for playing.")
  (System/exit 0))

(defn- assert-arity
  "Asserts argument arity"
  [cmd arity args]
  (if-not
    (= arity (count args))
    (throw (AssertionError. (str "Command '" (name cmd) "' expects " arity " arguments.")))))

(defn- param->int
  "Convert an input param to an integer"
  [p]
  (if
    (re-matches #"^[0-9]$" p)
    (Integer/parseInt  p)
    (throw (AssertionError. (str "Expected '" p "' to be an integer.")))))

; "Convert a numeric input param to a position index"
(def param->idx (comp dec param->int))

(defn- unhandled-cmd
  "Default command that responds to unhandled inputs"
  [img-state & _]
  (println "Command not recognised. Please try again.")
  img-state)

(def directory
  {:I (fn [_ size]
        (assert-arity :I 2 size)
        (img/init (map param->int size)))

   :C (fn [[_ size] args]
        (assert-arity :C 0 args)
        (img/init size))

   :L (fn [img-state [x y colour :as args]]
        (assert-arity :L 3 args)
        (img/apply-pixel
         img-state
         [(param->idx x) (param->idx y) (keyword colour)]))

   :V (fn [img-state [column start end colour :as args]]
        (assert-arity :V 4 args)
        (img/apply-changes
         img-state
         (op/vertical-line
          (param->idx column)
          (param->idx start)
          (param->idx end)
          (keyword colour))))

   :H (fn [img-state [start end row colour :as args]]
        (assert-arity :H 4 args)
        (img/apply-changes
         img-state
         (op/horizontal-line
          (param->idx row)
          (param->idx start)
          (param->idx end)
          (keyword colour))))

   :F (fn [img-state [x y colour :as args]]
        (assert-arity :F 3 args)
        (img/apply-changes
         img-state
         (op/fill
          img-state
          [(param->idx x) (param->idx y)]
          (keyword colour))))

   :S (fn [img-state args]
        (assert-arity :S 0 args)
        (img/display img-state))

   :X (fn [_ args]
        (assert-arity :X 0 args)
        (exit-program)) })

(defn- safe-invoke
  "Safely invokes a command to provide feedback on input errors"
  [cmd args]
  (try
    (let [f-invoke  (fnil apply unhandled-cmd)]
      (f-invoke cmd args))
    (catch AssertionError e
      (println (str (.getMessage e) " Please try again."))
      (first args)))) ; return img-state

(defn execute
  "Safely executes the appropriate function (or unhandled) for a given input command"
  [img-state input]
  (if-not (str/blank? input)
    (let [stripped-input  (str/replace input " " "")
          [cmd & args]    (map str (seq stripped-input))
          cmd-key         (keyword (str/upper-case cmd))]

      (safe-invoke
       (directory cmd-key)
       (concat [img-state] [args])))
    img-state))
