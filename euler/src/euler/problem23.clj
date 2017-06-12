(ns euler.problem23
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

"A perfect number is a number for which the sum of its proper
divisors is exactly equal to the number. For example, the sum of the
proper divisors of 28 would be 1 + 2 + 4 + 7 + 14 = 28, which means
that 28 is a perfect number.

A number n is called deficient if the sum of its proper divisors is
less than n and it is called abundant if this sum exceeds n.

As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16, the
smallest number that can be written as the sum of two abundant numbers
is 24. By mathematical analysis, it can be shown that all integers
greater than 28123 can be written as the sum of two abundant
numbers. However, this upper limit cannot be reduced any further by
analysis even though it is known that the greatest number that cannot
be expressed as the sum of two abundant numbers is less than this
limit.

Find the sum of all the positive integers which cannot be written as
the sum of two abundant numbers."

(defn factor-sum [x]
  (reduce + (factors x)))

(defn deficient? [x]
  (< (factor-sum x) x))

(defn abundant-raw? [x]
  (< x (factor-sum x)))

(def abundant? (memoize abundant-raw?))

(defn perfect? [x]
  (= x (factor-sum x)))

;; (range 1 28123)


(def abundants (filter abundant? (range 1 (inc 28123))))
;; (count abundants)
;; 6965

;; (count (combo/selections abundants 2))
;; 48511225
;; euler.problem23> (* 6965 6965)
;; 48511225

;; (reduce + (take-while #(< % 28124) (map #(apply + %) (combo/selections abundants 2))))
;; 97832594

(defn solve []
  (time (reduce + (set/difference  (set (range 1 28124))
                                   (set (map #(apply + %) (combo/selections abundants 2)))
                                   ))))

(def mx 28123)

;; not finished, not working, maybe use reduce?
#_ (defn solve-2 []
     (loop [total 0 i 1]
       (if (> i mx)
         total
         (loop [x 1]
           (if (and (abundant? x)
                    (abundant? (- i x)))
             )
           )
         )
       )
     )

;; -------------------- Note from an ocaml impl. that takes 2 secs.
;; This seems somewhat like my original idea.

"I'm summing all numbers in 1..28123 that are \"good\".  To determine if
x is \"good\" I iterate all numbers \"a\" in 1..x/2 to check if \"a\" is
abundant and \"x-a\" is abundant. Iteration stops if one such pair
found. Function \"ab\" checks for abundancy and memoizes its result for
later uses.
"

;; -------------------- Not sure if anything below this line is correct.

;; any n to test, doesn't need testing against any abundant which is greater than n - 12

;; sum up all numbers, sum up which numbers can be be made from abundants, subtract?
;; (reduce + (range 1 28124))
;; 395465626
;; (reduce + abundants)
;; 97861532
;; (- (reduce + (range 1 28124)) (reduce + abundants))
;; 297604094

;; (reduce + (range 1 28124))
