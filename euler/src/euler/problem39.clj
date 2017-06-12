(ns euler.problem39
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
            ;; [clojure.math.numeric-tower :as clj-math]
            [euler.common :refer :all]
            ))

:unsolved

"If p is the perimeter of a right angle triangle with integral length
sides, {a,b,c}, there are exactly three solutions for p = 120.

{20,48,52}, {24,45,51}, {30,40,50}

For which value of p ≤ 1000, is the number of solutions maximised?"

(defn solution? [[a b c]]
  (= (+ (Math/pow a 2)
        (Math/pow b 2))
     (Math/pow c 2)))

(defn generate-solutions [p]
  (for [a (range 1 (- p 2))
        b (range 1 (- p 2))
        c (range 1 (- p 2))
        :when (= p (+ a b c))]
    [a b c]))

(defn filter-solutions [ps]
  (filter solution? ps))

(defn dedup-solutions [solutions]
  (set (map set solutions)))

(defn step [p]
  {:p p
   :c (->> (generate-solutions p)
           filter-solutions
           dedup-solutions
           count)})

(reduce #(if (< (:p %1) (:p %2))
           %2
           %1) {:p 0 :c 0} (map step (range 3 1001)))

(defn solve [mx]
  (time (reduce #(if (< (:c %1) (:c %2))
                   %2
                   %1) {:p 0 :c 0} (map step (range 3 mx)))))
