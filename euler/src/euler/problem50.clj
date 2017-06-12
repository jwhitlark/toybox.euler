(ns euler.problem50
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

"The prime 41, can be written as the sum of six consecutive primes:

41 = 2 + 3 + 5 + 7 + 11 + 13
(reduce + (take 6 (primes)))

This is the longest sum of consecutive primes that adds to a prime
below one-hundred. (last is .41 of max)

The longest sum of consecutive primes below one-thousand that adds to
a prime, contains 21 terms, and is equal to 953.
(reduce + (take 21 (drop 3 (primes))))
;; 7 .. 89
(last is .089)

Which prime, below one-million, can be written as the sum of the most
consecutive primes?
(might be something like .004)
"


;; Since it's the largest count we're looking for, it will probably contain the low end.

;; Shouldn't be over half, should be well under half, as a matter of fact.

;; find roughly how many primes, starting w/ 2, sum to 1M
;; less than 1K primes
;; more than 500? 824693N
;; 546 -> 997661 (not prime)
;; (last (take 546  (primes)))
;; => 3931

;; just decrementing c => [536 0 958577N]

;; since a sum dropping 0, over 1M is the max count we could ever
;; have, (because adding 2 is going to be the smallest increase you
;; could have.) Then decrement, checking each sum to make sure < 1M

(defn sum-too-large? [n]
  (> n 1000000))

;; need better prime sequence...

(defn prime-sum-raw [count offset]
  (reduce + (take count (drop offset (primes)))))

(def prime-sum (memoize prime-sum-raw))

;; how to calculate what the largest prime component could be?

(defn find-max-possible-terms [n]
  (loop [i 1]
    (if (> (prime-sum i 0) n)
      (- i 1)
      (recur (+ 1 i)))))

;; answer:
;; (reduce + (take 543 (drop 3 (primes))))
;; 997651N

;; Following doesn't work, is incomplete, utter garbage, etc.
#_ (defn solve []
  (let [n 1000000]
    ;; must be less than 546 primes
    (loop [c 546 result nil]
      (if result
        result
        (recur (dec c)
               (loop [o 0]
                 (let [test-v (reduce + (take c (drop o (primes))))]
                   (if (> o 20)
                     nil
                     (if (and (< test-v n)
                              (prime? test-v))
                       [c o test-v]
                       (recur (inc 0))
                       ))))))
      ))
  )
