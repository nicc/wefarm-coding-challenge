(ns wefarm-challenge.image)

(defn apply-pixel
  "Applies a single pixel change to an existing image"
  [img [x y colour]]
  (assoc-in img [x y] colour))

(defn apply-changes
  "Applies a delta of changes to an existing image"
  [img changes]
  (reduce apply-pixel img changes))
