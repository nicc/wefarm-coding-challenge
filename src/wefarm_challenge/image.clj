(ns wefarm-challenge.image
  (:require [clojure.string :as str]))

(def default-background :0)

(defn valid-pixel?
  "Checks if a pixel is within valid range for the given image dimensions"
  [[max-x max-y] [pix-x pix-y]]
  (and
   (< pix-x max-x)
   (< pix-y max-y)
   (<= 0 pix-x)
   (<= 0 pix-y)))

(defn- assert-valid-colour
  "Asserts that an input param is a valid colour"
  [p]
  (let [s (name p)]
    (if-not
      (re-matches #"^[A-Z]$" s)
      (throw (AssertionError. (str "Expected '" s "' to be a capital letter."))))))

(defn- assert-valid-range
  "Asserts that an input param is a valid colour"
  [size pixel]
  (if-not
    (valid-pixel? size pixel)
    (throw (AssertionError. "The requested operation exceeds image size."))))

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
  (->>
   (repeat default-background)
   (take n-cols)
   (vec)))

(defn init
  "Initialises an image"
  [[n-cols n-rows]]
  (->
   (take n-rows (repeat (init-row n-cols)))
   (vec)
   (cons [[n-cols n-rows]])))

(defn apply-pixel
  "Applies a single pixel change to an existing image state"
  [img-state [x y colour]]
  (let [size     (second img-state)
        img-idx  0]

    ; could use a :pre condition here but giving nice feedback gets fiddly
    (assert-valid-colour colour)
    (assert-valid-range size [x y])

    (assoc-in img-state [img-idx y x] colour))) ; x y reversed since nested seq addresses rows first

(defn apply-changes
  "Applies a delta of changes to an existing image state"
  [img-state changes]
  (reduce apply-pixel img-state changes))

(defn- row->string
  "Converts a row to a string for display"
  [row]
  (->>
   row
   (map name)
   (str/join)))

(defn display
  "Prints an image to stdout"
  [[img size]]
  (->>
   img
   (map row->string)
   (str/join "\n")
   (println))
  [img size])
