
(ns source-code-map.map.ns-defns
    (:require [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-defn-name?
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (boolean)
  [_ _ {:keys [ending-tag left-sibling-count parent-tag]}]
  (and (-> (parent-tag)         (= :defn))
       (-> (ending-tag)         (= :symbol))
       (-> (left-sibling-count) (= 0))))

(defn read-defn-name
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (vector)
  [result _ {:keys [tag-body]}]
  (let [left-symbol (tag-body :symbol)]
       (vector/update-last-item result assoc :name left-symbol)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-defn?
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (boolean)
  [_ _ {:keys [starting-tag]}]
  (-> (starting-tag) (= :defn)))

(defn add-defn
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (vector)
  [result _ _]
  (vector/conj-item result {}))

(defn close-defn?
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (boolean)
  [_ _ {:keys [ending-tag]}]
  (-> (ending-tag) (= :defn)))

(defn close-defn
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (vector)
  [result {:keys [cursor]} {:keys [tag-started-at]}]
  (let [started-at (tag-started-at :defn)]
       (vector/update-last-item result assoc :bounds [started-at cursor])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn map-ns-defns
  ; @ignore
  ;
  ; @param (vector) result
  ; @param (map) state
  ; @param (map) metafunctions
  ;
  ; @return (vector)
  [result state metafunctions]
  (cond (add-defn?       result state metafunctions) (add-defn       result state metafunctions)
        (read-defn-name? result state metafunctions) (read-defn-name result state metafunctions)
        (close-defn?     result state metafunctions) (close-defn     result state metafunctions)
        :return result))
