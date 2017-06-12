(ns euler.problem41
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


"We shall say that an n-digit number is pandigital if it makes use of
all the digits 1 to n exactly once. For example, 2143 is a 4-digit
pandigital and is also prime.

What is the largest n-digit pandigital prime that exists?"

;; only need test 10 digit primes, can start from the top?

;; 9....... primes,

;; this might be a cool place to use reductions

(defn solve-for-digits [n]
  (->> (range 1 n)
       combo/permutations
       (map #(str->int (apply str %)))
       (filter prime?)
       (reduce max 0)))

(defn solve [n]
  (time (loop [i n]
          (let [ans (solve-for-digits i)]
            (if (not= 0 ans)
              ans
              (recur (dec i)))))))

;; euler.problem41> (solve 10)
;; "Elapsed time: 799.892 msecs"
;; 7652413


;; -------------------- Wrote this first, but not needed
(defn pandigital? [n]
  (let [as-vec (digits n)
        as-set (set as-vec)]
    (cond
      (not= (count as-vec) (count as-set)) false
      (not= as-set (set (range 1 (inc (count as-set))))) false
      :else true)))
