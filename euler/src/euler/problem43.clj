(ns euler.problem43
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

"The number, 1406357289, is a 0 to 9 pandigital number because it is
made up of each of the digits 0 to 9 in some order, but it also has a
rather interesting sub-string divisibility property.

Let d1 be the 1st digit, d2 be the 2nd digit, and so on. In this way,
we note the following:

d2d3d4=406 is divisible by 2
d3d4d5=063 is divisible by 3
d4d5d6=635 is divisible by 5
d5d6d7=357 is divisible by 7
d6d7d8=572 is divisible by 11
d7d8d9=728 is divisible by 13
d8d9d10=289 is divisible by 17
Find the sum of all 0 to 9 pandigital numbers with this property."

;; could all be cleaned up

(defn pandigitals [n]
  (->> (range 0 n)
       combo/permutations
       (map #(apply str %))))

(defn check-subs [s start end divisor]
  (= 0 (mod (str->int (subs s start end)) divisor)))

(defn test-num [s]
  (and (check-subs s 1 4 2)
       (check-subs s 2 5 3)
       (check-subs s 3 6 5)
       (check-subs s 4 7 7)
       (check-subs s 5 8 11)
       (check-subs s 6 9 13)
       (check-subs s 7 10 17)))

(defn check []
  (test-num "1406357289"))

(defn solve []
  (time (->> (pandigitals 10)
             (filter test-num)
             (map bigint)
             (reduce +))))

;; euler.problem43> (solve)
;; "Elapsed time: 7650.903 msecs"
;; 16695334890N
