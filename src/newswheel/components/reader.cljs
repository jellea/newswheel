(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.text.LoremIpsum]
            [clojure.string :as str]
            [hickory.core :as hick]
            ))


(defn item [state owner]
    (om/component
      (html [:section
              [:h3 (:title state)]
              [:em (str "By " (:authors state))]
              [:h5 (:description state)]
              [:div (hick/as-hiccup (hick/parse (:content state)))]])))


(defn main [state owner]
  (prn (second (second state)))
  (om/component
    (html [:aside#reader
            (om/build-all item state {})])))
