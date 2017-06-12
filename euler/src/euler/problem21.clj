(ns euler.problem21
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

" Let d(n) be defined as the sum of proper divisors of n (numbers less
than n which divide evenly into n).

If d(a) = b and d(b) = a, where a ≠ b, then a and b are an amicable
pair and each of a and b are called amicable numbers.

For example, the proper divisors of 220 are 1, 2, 4, 5, 10, 11, 20,
 22, 44, 55 and 110.  therefore d(220) = 284. The proper divisors of
 284 are 1, 2, 4, 71 and 142, so d(284) = 220.

Evaluate the sum of all the amicable numbers under 10000."

(defn proper-divisors [n]
  (factors-raw n))

(defn pd-sum [n]
  (reduce + (proper-divisors n)))

(defn amicable-pair? [n m]
  (and (not= n m)
       (= n (pd-sum m))
       (= m (pd-sum n))))

;; build a map of val: factor-sum
;; for each factor-sum, look up m, compare val
(defn factor-sum-map [n]
  (->> (range 1 n)
       (map #(vector % (pd-sum %)))
       (into {})
       ))

(defn solve [max-n]
  (time (let [fsm (factor-sum-map max-n)]
          (->> (for [[k, v] fsm]
                 (if (and (not= k v)
                          (< k v)
                          (= k (get fsm v)))
                   (hash-set k v)))
               (filter identity)
               set
               (apply set/union)
               (reduce +)
               ))))

;; This is WITH memoization, not sure what it costs cold.
;; "Elapsed time: 28.156 msecs"
;; 31626

(defn run-tests []
  (test/is (= (proper-divisors 220) [1, 2, 4, 5, 10, 11, 20,
                                     22, 44, 55, 110]))
  (test/is (= (amicable-pair? 220 284) true))
  (test/is (= (amicable-pair? 5 3) false))
  )
