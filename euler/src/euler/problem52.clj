(ns euler.problem52
    (:require [clojure.reflect :as r]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.test :as test]
            [clojure.repl :refer [doc source dir]]

            [clojure.math.combinatorics :as combo]

            [criterium.core :as crit]
            [clj-http.client :as http]

            [clojure.inspector :refer [inspect inspect-table inspect-tree]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]

            [flatland.useful.debug :refer [?]]
            [flatland.useful.map :refer [merge-in]]
            [flatland.useful.fn :refer [! validator as-fn thrush applied]]
            [flatland.useful.seq :as seq]
            [flatland.useful.utils :refer [verify returning pop-if queue let-later empty-coll? with-timing]]

            ;; [multiset.core :as mset]
            [clojure.math.numeric-tower :as clj-math]
            [euler.common :refer :all]
            ))

"It can be seen that the number, 125874, and its double, 251748,
contain exactly the same digits, but in a different order.

Find the smallest positive integer, x, such that 2x, 3x, 4x, 5x, and
6x, contain the same digits."

(defn dset [n]
  (set (digits n)))

(defn check [n c]
  (apply = (map #(dset (* n %)) (range 1 (inc c)))))

(defn solve []
  (take 1 (filter #(check % 6) (iterate inc 1))))

;; euler.problem52> (time (doall (solve)))
;; "Elapsed time: 823.64 msecs"
;; (142857)
