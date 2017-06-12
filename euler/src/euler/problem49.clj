(ns euler.problem49
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

"The arithmetic sequence, 1487, 4817, 8147, in which each of the
terms increases by 3330, is unusual in two ways: (i) each of the three
terms are prime, and, (ii) each of the 4-digit numbers are
permutations of one another.

There are no arithmetic sequences made up of three 1-, 2-, or 3-digit
primes, exhibiting this property, but there is one other 4-digit
increasing sequence.

What 12-digit number do you form by concatenating the three terms in
this sequence?"

;; could check all 4 digit numbers, but let's just get all for digit
;; primes first...

(def possible-primes (filter prime? (concat ;(range -9999 -1000)
                                            (range 1000 10000))))

(defn num-key [n]
  (sort (digits n)))

(defn conj-v [c x]
  (conj (vec c) x))

(defn prime-permutations [lst]
  (reduce #(update %1 (num-key %2) conj-v %2)
          {}
          lst))

(defn solve []
  (time (->> possible-primes
             prime-permutations
             (filter #(< 2 (count (val %))))
             ;;(filter #(arithmetic-progression? (val %)))
             (map #(filter arithmetic-progression? (combo/combinations (val %) 3)))
             (filter not-empty?)
             ))
  )

;; "Elapsed time: 9.209 msecs"
;; (((2969 6299 9629)) ((1487 4817 8147)))

;; So answer is: 296962999629
