(ns newswheel.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [newswheel.components.circleview :as circleview]
            [newswheel.data]
            [newswheel.components.reader :as reader]
            )
  (:import [goog.net Jsonp]
           [goog Uri]))

(enable-console-print!)

 
(def test-url
  "http://www.newyorker.com/arts/critics/atlarge/2014/03/24/140324crat_atlarge_menand")
 
(def app-state 
  (atom 
    {:hover-article ""
     :selected-article ""
     :urls [test-url]
     :article-info []
     :current-topic "TwitterBlock"
     :articles newswheel.data/data
     }
    ))

;; state: list of article sources + hover-article + selected-article


  


;; Sanity checks
 
(defn main [state owner]
  (reify
    ;om/IDidMount
    ;(did-mount [_]
      ;(om/transact! state
                  ;#(assoc % :article-info
                    ;(parse-urls (:urls %)))))
    om/IRender
    (render [_]
    (html
      [:div.container
        (om/build circleview/main state {})
        (om/build reader/main state {})]))))

(om/root
  main
  app-state
  {:target (. js/document (getElementById "app"))})
