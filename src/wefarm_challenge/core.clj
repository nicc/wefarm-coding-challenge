(ns wefarm-challenge.core
  (:require [wefarm-challenge.command :as cmd]))

(def initial-img-state [[] [0 0]])

(defn- prompt
  "Displays input prompt"
  []
  (print "> ")
  (flush))

(defn -main
  "Starts the console input loop"
  []
  (prompt)
  (loop [input      (read-line)
         img-state  initial-img-state]
    (let [new-img-state  (cmd/execute img-state input)]
      (prompt)
      (recur (read-line) new-img-state))))
