(ns euler.problem35
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
            ;; [euler.common :refer :all]
            ))

"The number, 197, is called a circular prime because all rotations of
the digits: 197, 971, and 719, are themselves prime.

There are thirteen such primes below 100: 2, 3, 5, 7, 11, 13, 17, 31,
37, 71, 73, 79, and 97.

How many circular primes are there below one million?"

(defn rotate-one [s]
  (apply str (concat (rest s) (vector (first s)))))

(defn rotations [n]
  (let [s (str n)
        c (count s)]
    (map str->int (take c (iterate rotate-one s)))))

(defn circular-prime? [n]
  (every? prime? (rotations n)))

(defn solve [n]
  (time
   (->> (primes)
        (take-while #(< % n))
        (filter circular-prime?)
        count)))

;; euler.problem35> (solve 1000000)
;; "Elapsed time: 4452.161 msecs"
;; 55

(defn solve-2 [n]
  (time
   (loop [i 0 x 2]
     ;; (println i x)
     (if (< n x)
       i
       (recur (if (circular-prime? x) (inc i) i)
              (next-prime x))))))

;; euler.problem35> (solve-2 1000000)
;; "Elapsed time: 4185.807 msecs"
;; 55

;; Fastest posted answer
#_ (defn circular-prime?
  [^long n]
  (let [res (int (dec (count (numcol n))))]
    (if (== res 0)
      (some #(== % n) [3 7])
      (loop [i res m n]
        (if (== i -1)
          true
          (if (prime' m)
            (recur (- i 1)
                   (+ (quot m 10)
                      (* (rem m 10) (pow 10 res))))
            false))))))

#_ (defn ^long all-cprimes
  [^long lim]
  (let [bahan [1 3 7 9]
        looper (fn looper [^long i]
                 (if (> i lim)
                   0
                   (if (circular-prime? i)
                     (->> bahan
                          (map #(looper (+ % (* 10 i)) ))
                          (reduce +)
                          (+ 1))
                     (->> bahan
                          (map #(looper (+ % (* 10 i))))
                          (reduce +)))))]
    (+ 2 (reduce + (pmap looper bahan)))))
