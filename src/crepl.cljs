(ns crepl.svg.fractal
  (:require [reagent.core :as r]
            crepl.atom-sync))

(def max-depth (crepl.atom-sync/atom-sync 10))

(def PI (aget js/Math "PI"))
(def sin (aget js/Math "sin"))
(def cos (aget js/Math "cos"))

(defn radians
  "Convert degrees to radians"
  [degrees]
  (* (/ PI 180) degrees))

(defn draw-tree [angle x y length branch-angle depth tree]
  (if (> depth 0)
    (let [new-x (- x (* length (sin (radians angle))))
          new-y (- y (*  length (cos (radians angle))))
          new-length (fn [] (* length (+ 0.75 (rand 0.1))))
          new-angle  (fn [op] (op angle (* branch-angle (+ 0.95 (rand)))))]
      (swap! tree conj [:line {:x1 x :y1 y :x2 new-x :y2 new-y :stroke "black"}])
      (draw-tree (new-angle +) new-x new-y (new-length) branch-angle (- depth 1) tree)
      (draw-tree (new-angle -) new-x new-y (new-length) branch-angle (- depth 1) tree))))

(defn fractal []
  (let [tree (atom [:g])
        w 500
        h 500
        init-length ( / (min w h) 6)
        branch-angle (* 9 (/ w h))
        max-depth @max-depth]
    [:svg {:x 0 :y 0 :width w :height h}
     (draw-tree 0.0 (/ w 2) h init-length branch-angle max-depth tree)
     @tree]))

(defn slider []
  [:input {:type "range" :default-value @max-depth :min 0 :max 14
           :style {:width "50%"}
           :on-change (fn [e]
                        (reset! max-depth 
                                (int (.-target.value e))))}])

(defn svg-component []
  [:div
   [fractal]
   [slider]])

(r/render-component [svg-component]
                    (.getElementById js/document "app"))(comment ;; eval'd by Guest 68733 =>
#object[t [object Object]]
)

