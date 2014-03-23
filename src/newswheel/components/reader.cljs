(ns newswheel.components.reader
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [goog.text.LoremIpsum]
            [clojure.string :as str]
            ;[net.cgrand.enlive-html :as ehtml]
            ))

;(defn html->hiccup
   ;[html]
   ;(if-not (string? html)
     ;(->> (map html->hiccup (:content html))
       ;(concat [(:tag html) (:attrs html)])
       ;(keep identity)
       ;vec)
     ;html))

;(defn unescape-html
  ;[txt]
  ;;(prn (type text)))
  ;(let [text (str txt)]
    ;txt))
  ;(.replace text #"&lt;" "<" )))
    ;(str/replace   "&gt;" ">")
    ;(str/replace "&quot;" "\"" )
    ;(str/replace  "&amp;" "&" )))
 
;(defn unescape-html
  ;[text]
    ;(str/replace  "&lt;" "<" 
      ;(str/replace   "&gt;" ">"
        ;(str/replace "&quot;" "\"" 
          ;(str/replace  "&amp;" "&"  text))) ))
(defn item [state owner]
  ;(let [lorem     (goog.text.LoremIpsum.)
        ;sentence  (.generateParagraph lorem)]
  ;(let [sentence (
    (om/component
      (html [:section
              [:h3 (:title state)]
              [:em (str "By " (:authors state))]
              [:h5 (:description state)]
              [:div (:content state)]])))


(defn main [state owner]
  (prn (second (second state)))
  (om/component
    (html [:aside#reader
            (om/build-all item state {})])))
