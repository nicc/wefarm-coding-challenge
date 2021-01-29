(ns wefarm-challenge.core)

(defn -prompt
  "Displays input prompt"
  []
  (print "> ")
  (flush))

; TODO: hook this up, obvs. just proving we can fold through input on GUI state. note ctrl+d hangs
(defn -main
  "Starts the program console input loop"
  []
  (-prompt)
    (loop [input      (read-line)
           image-state  []]
      (let [new-image-state  (conj image-state input)]
        (println new-image-state)
        (-prompt)
        (recur
          (read-line)
          new-image-state))))
