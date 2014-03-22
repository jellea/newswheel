(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            ))

(defn item [state owner]
  (om/component
    (html [:section
            [:h3 "Title"]
            [:h5 "subtitle"]
            [:p "lorem"]])))

(defn main [state owner]
  (om/component
    (html [:aside#reader
            (om/build item {})
            (om/build item {})
            (om/build item {})])))
