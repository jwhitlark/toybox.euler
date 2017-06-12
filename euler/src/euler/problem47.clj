(ns euler.problem47
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

"The first two consecutive numbers to have two distinct prime factors are:

14 = 2 × 7
15 = 3 × 5

The first three consecutive numbers to have three distinct prime factors are:

644 = 2² × 7 × 23
645 = 3 × 5 × 43
646 = 2 × 17 × 19.

Find the first four consecutive integers to have four distinct prime
factors. What is the first of these numbers?"

(defn prime-factor-count-raw [n]
  (count (prime-factors n)))

(defn solve [c]
  (->> natural-numbers
       (filter #(not (prime? %))) ;; won't work right
       (map #(vector % (prime-factor-count-raw %)))
       (partition c 1)
       (filter #(= (take c (iterate inc (ffirst %)))
                   (map first %))) ; filter non-incrementing
       (filter #(apply = c (map second %)))
       first))

;; In fresh JVM, so meomizing doesn't help.
;; euler.problem47> (time (solve 4))
;; "Elapsed time: 266228.354 msecs"
;; ([134043 4] [134044 4] [134045 4] [134046 4])

(defn possible-groups [c]
  (->> natural-numbers
       (partition-by prime?)
       (filter #(<= c (count %)))))

(defn matches-in-group [c grp]
  (->> grp
       (map #(vector % (prime-factor-count-raw %)))
       (partition c 1)
       (filter #(apply = c (map second %)))
       )
  )

;; (take 1 (filter seq (map #(matches-in-group 4 %) (possible-groups 4))))
;; have list of primes below n,
(defn solve-2 [c]
  (->> (possible-groups c)
       (pmap #(matches-in-group c %))
       (filter seq)
       (first)))

;; need to learn how to work with java arrays...
;; build up an array containing number of factors, from the prime seq?
;; then scan it for c c's?

(defn aget-int [^ints ar ^Integer i]
  (aget ar i))

(defn check-n [ar n i]
  (apply = n
     (take n
           (map #(aget ^ints ar ^Integer %) (iterate inc i)))))

(defn find-run [ar n]
  (let [len (- (alength ^ints ar) n)]
    (loop [i 2]
      (cond
        (> i len) :no-answer-found
        (check-n ar n i) i
        :else (recur (+ 1 i))))))

(defmacro aupdate [ar i f]
  `(aset ~ar ~i (~f (aget ~ar ~i))))

;; (defn solve-3 [len n]
;;   (time (let [<len #(< % len)
;;               ar (int-array len 0)]
;;           (dorun (for [i (primes) :while (<len i)]
;;                    (dorun (for [j (take-while <len (iterate #(+ i %) i))]
;;                             (aupdate ar j inc)))))
;;           (find-run ar n))))

;; Could probably be made much faster if find-run was combined, so you
;; didn't need to make multiples of primes that weren't needed.
(defn solve-3 [len n]
  (time (let [<len #(if (< % (- len n)) % false)
              ar (int-array len 0)]
          (loop [prms (take-while #(< % (/ len 2)) (primes))]
            (when-first [next-prime prms]
              (loop [j next-prime]
                (aupdate ar j inc)
                (when-let [next-j (<len (+ j next-prime))]
                  (recur next-j)))
              (recur (rest prms))))
          (find-run ar n))))


;; (for [j (take-while <len (iterate #(+ i %) i))]
;;   (do (println j)
;;       (aset j (+ 1 (aget j)))))

;; 20-Interesting python version
;; max=1000000
;; n_factor = [0]*max

;; for i in range(2,max):
;;   if n_factor[i] == 0 :
;;     for j in range(2*i,max,i):
;;       n_factor[j] += 1

;; goal = [4]*4

;; for i in range(2,max):
;;   if n_factor[i:i+4] == goal:
;;     print i
;;     break
