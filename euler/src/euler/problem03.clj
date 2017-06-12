(ns euler.problem3
  (:require [clojure.reflect :as r]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [clojure.pprint :as pp]
            [clojure.edn :as edn]
            [clojure.test :as test]

            [criterium.core :as crit]
            [clj-http.client :as http]

            [clojure.inspector :refer [inspect inspect-table inspect-tree]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]

            [flatland.useful.debug :refer [?]]
            [flatland.useful.map :refer [merge-in]]
            [flatland.useful.fn :refer [! validator as-fn thrush applied]]
            [flatland.useful.utils :refer [verify returning pop-if queue let-later empty-coll? with-timing]]

            [euler.common :refer :all]
            ))

(def problem-url "https://projecteuler.net/problem=3")

"
The prime factors of 13195 are 5, 7, 13 and 29.

What is the largest prime factor of the number 600851475143 ?
"

(defn solve []
  (time (reduce max (prime-factors 600851475143))))

#_ (time (last (prime-factors 600851475143)))
;; "Elapsed time: 3762.821 msecs"
;; 6857


;; slightly faster
#_ (time (reduce max (prime-factors 600851475143)))
;; "Elapsed time: 3681.606 msecs"
;; 6857
