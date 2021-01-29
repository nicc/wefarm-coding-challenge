(ns wefarm-challenge.operation)

; TODO: pre (and post?) conditions to assert valid ranges

(defn- line
  "Creates a set of changes depicting a generic line.
   Currently only supports straight ones."
  [f-mutator start end]
  (map f-mutator (range start (inc end))))

(defn horizontal-line
  "Creates a set of changes depicting a horizontal line"
  [row start end colour]
  (letfn [(f-mutator [x] [x row colour])]
    (line f-mutator start end)))

(defn vertical-line
  "Creates a set of changes depicting a vertical line"
  [column start end colour]
  (letfn [(f-mutator [y] [column y colour])]
    (line f-mutator start end)))
