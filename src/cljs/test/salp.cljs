(ns test.salp
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn blah [] (.log js/console "blah"))
