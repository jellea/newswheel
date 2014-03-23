(ns newswheel.components.circleview
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [strokes :refer [d3]]))

(strokes/bootstrap)

(defn get-radians [inet]
         (map #(/ (* (* 2 Math/PI) %) inet)
           (range inet)))

(defn get-points [inet x0 y0 r]
        (map (fn [radian] [(+ x0 (* r (Math/cos radian)))
               (+ y0 (* r (Math/sin radian)))])
          (get-radians inet)))

(defn get-rotates [inet]
  (map (fn [i] (+ 0 (* i (/ 360 inet))))
    (range inet)))

(defn item [state owner {:keys [coord rotates x y r] :as opts}]
  (om/component
    (html [:rect.spot {:fill "#ff0000" :x 0 :y 0 :transform (str "translate(" (first (:coord state)) " " (second (:coord state)) "), rotate(" (:rotates state) ")") :width "80px" :height "7px"}])))

(defn circle [state owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      (let [player (. js/document (getElementById "mask"))]
        (.setAttribute player "xlink:href" "http://lorempixel.com/output/abstract-q-c-640-480-3.jpg"))
    )
    om/IRender
    (render [_]


      (let [circle-data (second (first (:articles state)))
            viewport {:vwidth (.-innerWidth js/window)
                      :vheight (.-innerHeight js/window)}
            r (/ (if (< (:vwidth viewport) (:vheight viewport)) (:vwidth viewport) (:vheight viewport)) 3.5)
            x (/ (- (int (:vwidth viewport)) 485) 2)
            y (/ (:vheight viewport) 2)
            points (get-points (count circle-data) x y r)
            rotates (get-rotates (count circle-data))
            num-circ (map-indexed #(assoc %2 :coord (nth points %1) :rotates (nth rotates %1)) circle-data)]

      ;(-> d3 (.select "#circletext")
        ;(.attr {:text-align "justified"
         ;:line-height "125%" :wrap-margin "25"
         ;:shape-inside "url(#mask)"}))

      (html
        [:div#grr
          [:div#circletext
            [:p.title "A Chinese satellite has spotted a large object floating in the Indian Ocean"]
            [:p.subtitle "@mashable on twitter"]
            [:p.clicktoread "click to read"]]
          [:svg#graph
            [:circle#mask {:fill "transparent" :stroke-width "3px" :cx x :cy y :r r}]
            [:g (om/build-all item num-circ {:opts {:x x :y y :r r}})]]])))))

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
