(ns newswheel.components.circleview
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [strokes :refer [d3]]))

(strokes/bootstrap)

(defn item [state owner {:keys [viewport] :as opts}]
  (prn viewport)
  (om/component
    (html [:circle {:stroke "#777777" :fill "transparent" :stroke-width "3px" :cx "400" :cy "200" :r"190"}])))

(defn circle [state owner]
  (let [circle-data (second (first (:articles state)))
        viewport {:vwidth (.-innerWidth js/window)
                  :vheight (.-innerHeight js/window)}
        circlemiddle {:x (:vheight viewport)
                      :y (:vwidth viewport)}]
    (om/component
      (html [:svg.graph
              [:g (om/build-all item circle-data {:opts {:viewport viewport}})]]))))

(defn nav [state owner]
  (om/component
    (html [:div.menu
            [:svg.logo {:width "37px" :height "37px"}
              [:circle {:stroke "#777777" :fill "transparent" :stroke-width "3px" :cx "18" :cy "18" :r"15"} ""]]
            [:h3.circletitle "Crimea"]
            [:div.nav
              [:a.backbtn "back"]
              [:a.nextbtn "next"
                [:span.title "NSA"]]]])))

(defn main [state owner]
  (om/component
    (html [:div.circle-view
            (om/build nav state {})
            (om/build circle state {})])))
