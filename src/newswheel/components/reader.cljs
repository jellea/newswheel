(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            ))

(defn item [state owner]
  (om/component
    (html [:section
            [:h3 (:title article)]
            [:h6 (:authors article)]
            [:h5 (:description article)]
            [:div (:content article)]])))

(defn main [state owner]
  (prn (:urls state))
  (om/component
    (html [:aside#reader
            (om/build item {})
            (om/build item {})
            (om/build item {})])))
 
