(ns euler.problem37
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

"The number 3797 has an interesting property. Being prime itself, it
is possible to continuously remove digits from left to right, and
remain prime at each stage: 3797, 797, 97, and 7. Similarly we can
work from right to left: 3797, 379, 37, and 3.

Find the sum of the only eleven primes that are both truncatable from
left to right and right to left.

NOTE: 2, 3, 5, and 7 are not considered to be truncatable primes."

;; (defn left-truncatable? [p]
;;   (let [pt (str p)]
;;     (loop [x pt]
;;       (if (empty? x)
;;         true
;;         (if (not (prime? (char->int x)))
;;           false
;;           (recur (rest x))
;;           )))))

;; (defn right-truncatable? [p]
;;   (let [pt (str p)]
;;     (loop [x pt]
;;       (if (empty? x)
;;         true
;;         (if (not (prime? (char->int x)))
;;           false
;;           (recur (drop-last x))
;;           )))))

;; (defn truncatable? [p]
;;   (and (left-truncatable? p)
;;        (right-truncatable? p)))

(defn truncatable? [p]
  (let [left-seq (drop 1 (take-while seq (iterate rest (str p))))
        right-seq (drop 1 (take-while seq (iterate drop-last (str p))))
        full-seq (concat left-seq right-seq)]
    (reduce #(and %1 (prime? (char->int %2))) true full-seq)))

(defn solve []
  (time (apply + (doall (->> (primes)
                             (filter truncatable?)
                             (drop 4)
                             (take 11))))))

;; euler.problem37> (solve)
;; "Elapsed time: 3205.925 msecs"
;; 748317N
