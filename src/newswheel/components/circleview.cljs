(ns newswheel.components.circleview
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [strokes :refer [d3]]))

(strokes/bootstrap)

(defn get-radians [inet]
             (map #(/ (*(* 2 Math/pi) %) inet)
             (range inet)))

(defn get-points [inet, [x0, y0]]
            map #([(+ x0 (* r (cos %))),
                   (+ y0 (* r (sin %)))])
            (get-radians inet))

(defn item [state owner {:keys [x y r] :as opts}]
  (om/component
    (html [:circle {:stroke "#777777" :fill "transparent" :stroke-width "3px" :cx x :cy y :r r}])))

(defn circle [state owner]
  (let [circle-data (second (first (:articles state)))
        viewport {:vwidth (.-innerWidth js/window)
                  :vheight (.-innerHeight js/window)}
        r (/ (if (< (:vwidth viewport) (:vheight viewport)) (:vwidth viewport) (:vheight viewport)) 3)
        x (/ (- (int (:vwidth viewport)) 485) 2)
        y (/ (:vheight viewport) 2)]

    (om/component
      (html [:svg.graph
              [:circle ""]
              [:text.knockout {:x (- x  r) :y y :width (str r "px") :fill "white"} "A Chinese satellite has spotted a large object floating in the Indian Ocean"]
              [:g (om/build-all item circle-data {:opts {:x x :y y :r r}})]]))))

(defn nav [state owner]
  (om/component
    (html [:div.menu
            [:svg.logo {:width "37px" :height "37px"}
              [:circle {:stroke "#777777" :fill "transparent" :stroke-width "3px" :cx "18" :cy "18" :r"15"} ""]]
            [:h3.circletitle "Crimea"]
            [:div.nav
              [:a.backbtn "back"]
              [:a.nextbtn "next"]
              [:span.title (:next-topic state)]]])))

(defn main [state owner]
  (om/component
    (html [:div.circle-view
            (om/build nav state {})
            (om/build circle state {})])))
