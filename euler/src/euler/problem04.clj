(ns euler.problem4
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
            )
)


"A palindromic number reads the same both ways. The largest palindrome
made from the product of two 2-digit numbers is 9009 = 91 × 99.

Find the largest palindrome made from the product of two 3-digit numbers."

(defn- pal? [n]
  (let [p1 (str n)]
    (= p1 (apply str (reverse p1)))))

(def palindrome? (memoize pal?))

(defn solve []
  (time (->> (for [x (range 100 1000)
                   y (range 100 1000)
                   ]
               (* x y))
             (filter palindrome?)
             (reduce max)
             )))

;; euler.problem4> (solve)
;; "Elapsed time: 784.697 msecs"
;; 906609
