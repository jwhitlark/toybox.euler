(ns euler.problem15
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

"Starting in the top left corner of a 2×2 grid, and only being able to
move to the right and down, there are exactly 6 routes to the bottom
right corner.

1x1: 2
2x2: 6

for each side, there must be one :r

2x2:
[:r :r :d :d]
[:r :d :r :d]
[:d :r :d :r]
[:d :d :r :r]
[:r :d :d :r]
[:d :r :r :d]

How many such routes are there through a 20×20 grid?
"

;; has something to do with the central binomial coefficients and catalan numbers
;; see http://mathforum.org/advanced/robertd/manhattan.html

(defn solve [n]
  (time (/ (factorial (* 2 n)) (* (factorial n) (factorial n)))))

;; "Elapsed time: 0.302 msecs"
;; 137846528820N
