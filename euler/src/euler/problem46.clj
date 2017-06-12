(ns euler.problem46
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

"It was proposed by Christian Goldbach that every odd composite
number can be written as the sum of a prime and twice a square.

9 = 7 + 2×  (pow 1, 2)
15 = 7 + 2×  (pow 2, 2)
21 = 3 + 2×  (pow 3, 2)
25 = 7 + 2×  (pow 3, 2)
27 = 19 + 2× (pow 2, 2)
33 = 31 + 2× (pow 1, 2)

It turns out that the conjecture was false.

What is the smallest odd composite that cannot be written as the sum
of a prime and twice a square?"

;; must be odd
;; must be composite

(def odd-composites (filter odd? (composites)))

;; (primes)
(def squares (map #(int (Math/pow % 2)) (drop 1 natural-numbers)))

(defn conjecture [p s]
  (+ p (* 2 (int (Math/pow s 2)))))

(defn test-n [n p s]
  {:pre [(prime? p) (odd? n)]}
  (== n (conjecture p s)))

(defn test-squares [n p]
  ;; given n, and p prime, find if any square will fit
  (take-while #(>= n (conjecture p %)) natural-numbers))

(defn test-primes [n]
  (loop [p 2 s 1]
    (if (< n (conjecture p s))
      [n :no-match]
      (let [s (last (test-squares n p))]
        (if (test-n n p s)
          [n p s :match]
          (recur (next-prime p) 1)
          )))))

(defn solve []
  (time (doall (take 1 (drop-while #(not= :no-match (second (test-primes %))) odd-composites)))))

;; euler.problem46> (solve)
;; "Elapsed time: 5177.769 msecs"
;; (5777)

;; :-/ Not very good


;; Off site, doesn't compile.
;; (defn solve-2
;;   (let [sieve (fn [cap]
;;                 (let [c (inc cap) a (boolean-array c)]
;;                   (aset-boolean a 2 true)
;;                   (dorun (for [i (range 3 c 2)] (aset-boolean a i true)))
;;                   (dorun (for [i (range 3 c 2) :when (aget ^booleans a i)]
;;                            (dorun (for [j (range (* i i) c i)] (aset-boolean a j false)))))
;;                   a))
;;         a (sieve 1000000)
;;         suitable? (fn [n] (some #(let [sqn (Math/sqrt (/ (- n %) 2))] (when (== sqn (long sqn)) n))
;;                                 (filter #(aget ^booleans a %) (range 3 n 2))))]
;;     (first (drop-while suitable? (rest (keep-indexed #(when (and (false? %2) (odd? %1)) %1) a))))))
