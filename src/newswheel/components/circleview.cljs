(ns newswheel.components.circleview
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.string :as gstring] 
            [clojure.string :as string] 
            [goog.string.format :as gformat]
            [strokes :refer [d3]]))

(strokes/bootstrap)

(def colors [{:hex "34495e" :bright false} {:hex "16a085" :bright false} {:hex "27ae60"  :bright false}
             {:hex "2980b9" :bright false} {:hex "8e44ad" :bright false} {:hex "2c3e50" :bright false}
             {:hex "f1c40f"} {:hex "e67e22"} {:hex "e74c3c" :bright false}
             {:hex "ecf0f1"} {:hex "95a5a6"} {:hex "f39c12"}
             {:hex "d35400" :bright false} {:hex "c0392b" :bright false} {:hex "bdc3c7"}
             {:hex "7f8c8d" :bright false} {:hex "1abc9c"} {:hex "2ecc71"}
             {:hex "3498db"} {:hex "9b59b6"}])

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

(defn update-current-hover [{:keys [title subtitle]} state]
  (om/transact! state (fn [st] (assoc st :hover-article {:title title
                                                :subtitle subtitle}))))

(defn item [{:keys [coord rotates title subtitle x y r]} owner
            {:keys [state] :as opts}]
  (om/component
    (html [:rect.spot {:onMouseOver #(update-current-hover {:title title :subtitle subtitle} state)
                       :fill "#ff0000" :x 0 :y 0
                       :transform (str "translate(" (first coord) " " (second coord) "), rotate(" rotates ")") 
                       :width "80px" :height "7px"}])))

(defn circle [state owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      (let [player (. js/document (getElementById "mask"))]
        (.setAttribute player "xlink:href" "http://lorempixel.com/output/abstract-q-c-640-480-3.jpg"))
    )
    om/IRender
    (render [_]
      (let [circle-data
             (map #(assoc % :color (:hex (rand-nth color)))
               (sort-by #(:perspective %) (second (first (:articles state)))))
            viewport {:vwidth (.-innerWidth js/window)
                      :vheight (.-innerHeight js/window)}

            r (/ (if (< (:vwidth viewport) (:vheight viewport)) (:vwidth viewport) (:vheight viewport)) 3.2)
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
            [:p.title (get-in state [:hover-article :title])]
            [:p.subtitle (get-in state [:hover-article :subtitle])]
            [:p.clicktoread (if (= (get-in state [:hover-article :title]) "") "" "click to read")]
          ]
          [:svg#graph
            [:circle#mask {:fill "transparent" :stroke-width "3px" :cx x :cy y :r r}]
            [:g (om/build-all item num-circ {:opts {:x x :y y :r r :state state}})]]])))))

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
