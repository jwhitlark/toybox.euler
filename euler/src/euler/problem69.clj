(ns euler.problem69
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

"Euler's Totient function, φ(n) [sometimes called the phi function], is used to determine the number of numbers less than n which are relatively prime to n. For example, as 1, 2, 4, 5, 7, and 8, are all less than nine and relatively prime to nine, φ(9)=6.

|  n | Relatively Prime | φ(n) |    n/φ(n) |
|----+------------------+------+-----------|
|  2 | 1                |    1 |         2 |
|  3 | 1,2              |    2 |       1.5 |
|  4 | 1,3              |    2 |         2 |
|  5 | 1,2,3,4          |    4 |      1.25 |
|  6 | 1,5              |    2 |         3 |
|  7 | 1,2,3,4,5,6      |    6 | 1.1666... |
|  8 | 1,3,5,7          |    4 |         2 |
|  9 | 1,2,4,5,7,8      |    6 |       1.5 |
| 10 | 1,3,7,9          |    4 |       2.5 |
|----+------------------+------+-----------|

It can be seen that n=6 produces a maximum n/φ(n) for n ≤ 10.

Find the value of n ≤ 1,000,000 for which n/φ(n) is a maximum."

(defn step [m n]
  (let [t (totient n)
        o (/ n t)]
    (if (< (:o m) o)
      {:n n :t t :o o}
      m)))

; {:n 7 :t 6 :o 1.166}
(defn solve [n]
  (time (reduce step {:n 0 :t 0 :o 0} (range 1 n))))
