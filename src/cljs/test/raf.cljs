(ns test.raf)
(defn raf-sequence []
  (let [raf (.fromCallback js/Bacon
                             (fn [cb]
                               (.requestAnimationFrame js/window
                                                       (fn [] (cb (.now js/Date))))))]
       (.merge raf (.flatMap raf raf-sequence))))
