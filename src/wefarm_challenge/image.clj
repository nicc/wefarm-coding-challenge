(ns wefarm-challenge.image)

(def default-background :0)

(defn- init-row
  "Initialise a single row"
  [n-cols]
  (take n-cols (repeat default-background)))

(defn init
  "Initialise an image"
  [n-cols n-rows]
  (take n-rows (repeat (init-row n-cols))))

(defn apply-pixel
  "Applies a single pixel change to an existing image"
  [img [x y colour]]
  (assoc-in img [x y] colour))

(defn apply-changes
  "Applies a delta of changes to an existing image"
  [img changes]
  (reduce apply-pixel img changes))
