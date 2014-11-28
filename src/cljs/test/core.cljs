(ns test.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [test.salp :as salp]))

(defonce app-state (atom {:text "Hello Chestnut!!!!"}))

(defn main []
  (let [key-down-stream (.asEventStream (js/$ js/document) "keydown")
        key-code-stream (.map key-down-stream (fn [e] (.-keyCode e)))
        velocity-atom (atom {})
        resistance #(* (/ % 10) -1)
        keyboard-acceleration-stream (.map key-code-stream (fn [keyCode] (case keyCode
                                                                          37 [-1 0]
                                                                          38 [0 1]
                                                                          39 [1 0]
                                                                          40 [0 -1]
                                                                          [0 0])))
        resistance-acceleration-stream (.flatMapFirst
                                        (.once js/Bacon 0)
                                        (fn []
                                          (.sampledBy (.map @velocity-atom #(map resistance %))
                                                      (.interval js/Bacon 1000))))
        acceleration-stream (.merge keyboard-acceleration-stream resistance-acceleration-stream)
        initial-velocity [0, 0]
        velocity-stream (.scan acceleration-stream initial-velocity #(map + %1 %2))
        style {:backgroundColor "blue"}]
    (swap! velocity-atom (fn [] velocity-stream))
    (.onValue velocity-stream (fn [k] (.log js/console (clj->js k))))
    (om/root
     (fn [app owner]
       (reify
         om/IRender
         (render [_]
           (salp/blah)
           (dom/h1 #js {:className "blah" :style (clj->js style)} (:text app)))))
     app-state
     {:target (. js/document (getElementById "app"))}))

  (let [click-stream (.asEventStream (js/$ "h1") "click")]
    (.onValue click-stream (fn [] (.log js/console "here"))))
  )
