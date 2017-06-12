(ns euler.problem30
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

"Surprisingly there are only three numbers that can be written as the
sum of fourth powers of their digits:

1634 = 14 + 64 + 34 + 44
8208 = 84 + 24 + 04 + 84
9474 = 94 + 44 + 74 + 44
As 1 = 14 is not a sum it is not included.

The sum of these numbers is 1634 + 8208 + 9474 = 19316.

Find the sum of all the numbers that can be written as the sum of
fifth powers of their digits."

;; (defn pow-digit-raw [p c]
;;   (Math/pow (char->int c) p))

;; (def pow-digit (memoize pow-digit-raw))

(defn pow-sum [x p]
  (reduce + (map #(Math/pow (char->int %) p) (str x))))

(defn candidate? [x p]
  (== x (pow-sum x p)))

(defn solve [mx p]
  (time (reduce + (doall (filter #(candidate? % p) (range 2 mx))))))

;; (solve 1e6 5)
;; "Elapsed time: 1766.964 msecs"
;; 443839

;;;; stuff below doesn't seem to work.
;; from https://projecteuler.net/thread=30;page=9#last
;; n x (pow 9 5) < (pow 10 (- n 1)) <=> n > 5
;; That means the numbers we're looking for are smaller than 1e6

(defn find-upper-limit [n]
  (< (* n (Math/pow 9 5))
     (Math/pow 10 (- n 1))))
