(ns newswheel.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [newswheel.components.circleview :as circleview]
            [newswheel.components.reader :as reader]
            )
  (:import [goog.net Jsonp]
           [goog Uri]))

(enable-console-print!)

(def app-state (atom {:text "Hello world!"}))

(defn jsonp [uri]
  (let [out (chan)
        req (Jsonp. (Uri. uri))]
    (.send req nil (fn [res] (put! out res)))
    out))

;; Basic structures
(defrecord Article [title authors description content])

;; Embedly stuff
(def reader-width "600")

(def embedly-api-key "30c971bdaf234365b7ed9ce8f3817048")

(def embedly-api-url
  (str "http://api.embed.ly/1/extract?key="
       embedly-api-key
        "&format=json&url="))

(defn parse-embedly [resp]
  (Article.
    (aget resp "title")
    (aget resp "authors")
    (aget resp "description")
    (aget resp "content")))

(defn query-url [q]
  (str embedly-api-url q))

;; Sanity checks
(def test-url
  "http://www.newyorker.com/arts/critics/atlarge/2014/03/24/140324crat_atlarge_menand")

(go (.log js/console (<! (jsonp (query-url test-url)))))

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
