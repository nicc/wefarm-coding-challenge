(ns wefarm-challenge.image)

(def default-background :0) ; TODO: consider passing this in (dep injecting the default)

(defn valid-pixel?
  "Checks if a pixel is within valid range for the given image dimensions"
  [[max-x max-y] [pix-x pix-y]]
  (and
   (< pix-x max-x)
   (< pix-y max-y)
   (<= 0 pix-x)
   (<= 0 pix-y)))

(defn get-pixel
  "Returns the colour of a given pixel"
  [img [x y]]
  (get-in img [y x]))  ; x y are reversed here since a nested seq addresses rows first

(defn get-neighbours
  "Return a vector of coordinates for the neighbours of a given pixel"
  [img-size pixel]
  (let  [fn-maybe-neighbours  (juxt
                               (fn [[x y]] [(inc x) y])
                               (fn [[x y]] [(dec x) y])
                               (fn [[x y]] [x (inc y)])
                               (fn [[x y]] [x (dec y)]))]
    (->>
     pixel
     (fn-maybe-neighbours)
     (filter (partial valid-pixel? img-size)))))

(defn- init-row
  "Initialises a single row"
  [n-cols]
  (take n-cols (repeat default-background)))

(defn init
  "Initialises an image"
  [[n-cols n-rows]]
  (take n-rows (repeat (init-row n-cols))))

(defn apply-pixel
  "Applies a single pixel change to an existing image"
  [img [x y colour]]
  (assoc-in img [y x] colour)) ; x y are reversed here since a nested seq addresses rows first

(defn apply-changes
  "Applies a delta of changes to an existing image"
  [img changes]
  (reduce apply-pixel img changes))
