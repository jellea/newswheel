(ns newswheel.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [newswheel.components.circleview :as circleview]
            [newswheel.data]
            [newswheel.components.reader :as reader]
            [ajax.core :refer [GET POST]]
            )
  (:import [goog.net Jsonp]
           [goog Uri]))

(enable-console-print!)


(def test-url
  "http://www.newyorker.com/arts/critics/atlarge/2014/03/24/140324crat_atlarge_menand")

(def app-state
  (atom
    {:hover-article {:title ""
                     :subtitle ""}
     :selected-article ""
     :urls [test-url]
     :article-info []
     :current-topic "Crimea"
     :next-topic "NSA"
     :articles newswheel.data/data
     }
    ))

;; state: list of article sources + hover-article + selected-article


;; Basic structures
(defrecord Article [title authors description content])

;; Embedly stuff
(def reader-width "600")

(def embedly-api-key "ac94988c1ff34648a1891c480378987b")

(def embedly-api-url
  (str "http://api.embed.ly/1/extract?key="
       embedly-api-key
        "&format=json&url="))

(defn parse-embedly [resp]
  {
    :title ( :title resp )
    :authors ( map #(:name %) (:authors resp) )
    :description ( :description resp )
    :content ( :content resp )
    :image (:url (first (:images resp)))
   })

(defn query-url [q]
  (str embedly-api-url q))

(defn upd-embed [resp]
  (swap! app-state 
         (fn [st] (update-in st [:article-info]
                     #(conj %
   (parse-embedly (js->clj resp :keywordize-keys true)))))))

(defn req-handler [response]
  (.log js/console (js->clj response)))

;; Sanity checks

(defn retrieve-embed [source callback error-callback]
  (.send (goog.net.Jsonp. (query-url source) )
    "" callback error-callback))

(defn get-urls [articles]
  "Find all urls in the current topic"
  (let [urls (map #(:url %) 
                  ((keyword (:current-topic @app-state)) articles))]
        (apply list urls)
        ))

(defn upd-all []
  (doseq [url (get-urls (:articles @app-state))]
       (retrieve-embed url upd-embed req-handler)))


(upd-all)

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
        (om/build reader/main (:article-info state) {})]))))

(om/root
  main
  app-state
  {:target (. js/document (getElementById "app"))})
