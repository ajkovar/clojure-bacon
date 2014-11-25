(ns test.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [test.salp :as salp]))

(defonce app-state (atom {:text "Hello Chestnut!!!!"}))

(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (salp/blah)
          (dom/h1 #js {:className "blah" :style #js {:backgroundColor "red"}} (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))})
  (let [click-stream (.asEventStream (js/$ "h1") "click")]
    (.onValue click-stream (fn [] (.log js/console "here"))))
  )
