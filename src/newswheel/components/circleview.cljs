(ns newswheel.components.circleview
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            ))

(defn nav [state owner]
  (om/component
    (html [:div.menu
            [:div.logo "O"]
            [:div.circletitle "Crimea"]
            [:div.nav
              [:a.backbtn "back"]
              [:a.nextbtn "next"
                [:span.title "\"NSA\""]]]])))

(defn main [state owner]
  (om/component
    (html [:div.circle-view
            (om/build nav state {})
            [:h2 "Circle"]])))
