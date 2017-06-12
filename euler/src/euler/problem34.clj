(ns euler.problem34
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

"145 is a curious number, as 1! + 4! + 5! = 1 + 24 + 120 = 145.

Find the sum of all numbers which are equal to the sum of the
factorial of their digits.

Note: as 1! = 1 and 2! = 2 are not sums they are not included."

;; figure out what the max could be,
;; order doesn't matter
;; each digit gives you an offset...
;; only 9 values...
;; so how many combinations can there be?

;; euler.problem34> (map factorial (range 1 10))
;; (1 2N 6N 24N 120N 720N 5040N 40320N 362880N)

;; (factorial 9)
;; => 362880N

;; 2540160
;; 9999999

;; 2177280
;; 999999

(defn digit-fact-sum [x]
  (reduce + (map #(factorial (char->int %)) (str x))))

(defn factorion? [x]
  (= x (digit-fact-sum x)))

(defn solve [max-val]
  (time (reduce + (filter factorion? (range 3 max-val)))))

;; (solve 100000)
;; "Elapsed time: 231.978 msecs"
;; 40730


;; how to calculate max value?

;; http://mathworld.wolfram.com/Factorion.html
