(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            ))

(defn item [state owner]
  (om/component
    (html [:section
            [:h3 (:title state)]
            [:h6 (:authors state)]
            [:h5 (:description state)]
            [:div (:content state)]])))

(defn main [state owner]
  (om/component
    (html [:aside#reader
            (om/build item {})
            (om/build item {})
            (om/build item {})])))
