(ns newswheel.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [newswheel.components.circleview :as circleview]
            [newswheel.components.reader :as reader]
            ))

(enable-console-print!)

(def app-state (atom {:text "Hello world!"}))

(defn main [state]
  (om/component
    (html
      [:div
        (om/build circleview/main state {})
        (om/build reader/main state {})])))

(om/root
  main
  app-state
  {:target (. js/document (getElementById "app"))})
