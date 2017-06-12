(ns euler.problem27
  (:require [clojure.reflect :as r]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.test :as test]

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

            [euler.common :refer :all]
            ))


"Euler discovered the remarkable quadratic formula:

n² + n + 41

It turns out that the formula will produce 40 primes for the
consecutive values n = 0 to 39. However, when n = 40, 402 + 40 + 41 =
40(40 + 1) + 41 is divisible by 41, and certainly when n = 41, 41² +
41 + 41 is clearly divisible by 41.

;; (count (take-while prime? (create-run 1 41)))

The incredible formula n² − 79n + 1601 was discovered, which produces
80 primes for the consecutive values n = 0 to 79. The product of the
coefficients, −79 and 1601, is −126479.

;; (count (take-while prime? (create-run -79 1601)))

Considering quadratics of the form:

n² + an + b, where |a| < 1000 and |b| < 1000

where |n| is the modulus/absolute value of n
e.g. |11| = 11 and |−4| = 4

Find the product of the coefficients, a and b, for the quadratic
expression that produces the maximum number of primes for consecutive
values of n, starting with n = 0."

(defn eval-formula [a b n]
  (+ (* n n)
     (* a n)
     b))

(defn create-run [a b]
  (map (partial eval-formula a b) natural-numbers))

#_ (count (take-while prime? (create-run 1 41)))

;; -1000 to +1000 for a and b

#_ (defn solve [m]
     (loop [a (- m)
            max-a nil
            max-b nil
            max-run-count 0]
       (if (> a m)
         [max-a max-b max-run-count]
         (recur (+ 1 a) (loop [b (- m)])))
       )
     )

(defn solve [m]
  (time (doall
         (for [a (range (- m) m)
               b (range (- m) m)]
           (let [run (count (take-while prime? (create-run a b)))]
             (if (< 50 run)
               (println a b " : " run))
             run)))))

;; (reduce max (solve 1000))
;; "Elapsed time: 5583.811 msecs"
;; -61 971  :  71
