(ns euler.problem14
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
            [flatland.useful.seq :as seq]
            [flatland.useful.utils :refer [verify returning pop-if queue let-later empty-coll? with-timing]]

            [euler.common :refer :all]
            )
)

"The following iterative sequence is defined for the set of positive integers:

n → n/2 (n is even)
n → 3n + 1 (n is odd)

Using the rule above and starting with 13, we generate the following sequence:

13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1

It can be seen that this sequence (starting at 13 and finishing at 1)
contains 10 terms. Although it has not been proved yet (Collatz
Problem), it is thought that all starting numbers finish at 1.

Which starting number, under one million, produces the longest chain?

NOTE: Once the chain starts the terms are allowed to go above one million."

(defn collatz-next [n]
  (if (odd? n)
    (+ 1 (* 3 n))
    (/ n 2)))

(defn collatz [n]
  (take-while #(> % 1) (iterate collatz-next n))
  )

(defn collatz-odd ^long [^long n] (+ 1 (* 3 n)))
(defn collatz-even ^long [^long n] (/ n 2))

(defn collatz2 [^long n]
  (loop [x n sq [n]]
    (if (odd? x)
      (let [x2 (+ 1 (* 3 x))]
        (if (== x2 1)
          (conj sq x2)
          (recur x2 (conj sq x2))))
      (let [x2 (/ x 2)]
        (if (== x2 1)
          (conj sq x2)
          (recur x2 (conj sq x2))))
      )
    )
  )

(defn collatz-count [^long n]
  (loop [c 1 x n]
    (if (== 1 x)
      c
      (if (odd? x)
        (recur (+ 1 c) (collatz-odd x))
        (recur (+ 1 c) (collatz-even x))
        )
      )

    )
  )

(def collatz-count-memo (memoize collatz-count))

(def collatz-counts (map collatz-count positive-integers))

;; should do this without holding onto previous numbers, just keep the count.

;; (defn solve ^long [^long n]
;;   (reduce max collatz-nums)
;; )

(defn solve [n]
  ;;(reduce max (take n collatz-counts))
  (time
   (loop [i 1 imx 1 mx 0]
     (if (= i n)
       [imx mx]
       (let [x (collatz-count-memo i)
             new-max (> x mx)]
         (if new-max
           (recur (+ 1 i) i x)
           (recur (+ 1 i) imx mx)
           ))))))
