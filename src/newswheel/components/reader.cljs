(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.text.LoremIpsum]
            [clojure.string :as str]
            [hickory.core :as hick]))


(defn item [state owner]
    (om/component
      (html [:section
              [:div.head
                [:h3 (:title state)]
                [:em (str "By " (:authors state))]]
              [:div.body
                [:img {:href (:image state)}]
                [:div (hick/as-hiccup (hick/parse (:content state)))]
              ]
             ])))

(defn main [state owner]
  (reify
    om/IDidMount
    (did-mount [_]
      (.panelSnap (js/$ "#reader")
            #js {:slideSpeed 250})
    )
    om/IRender
    (render [_]
      (html [:aside#reader
              (om/build-all item state nil)]))))
