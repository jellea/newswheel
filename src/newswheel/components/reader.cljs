(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.text.LoremIpsum]
            ))

(defn item [state owner]
  (let [lorem     (goog.text.LoremIpsum.)
        sentence  (.generateParagraph lorem)]
    (om/component
      (html [:section
              [:h3 (:title state)]
              [:h5 (:subtitle state)]
              [:div sentence]]))))

(defn main [state owner]
  (om/component
    (html [:aside#reader
            (om/build-all item (second (second state)) {})])))
