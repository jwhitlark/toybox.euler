(ns euler.problem26
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

;; :unsolved

"A unit fraction contains 1 in the numerator. The decimal
representation of the unit fractions with denominators 2 to 10 are
given:

1/2	= 	0.5
1/3	= 	0.(3)
1/4	= 	0.25
1/5	= 	0.2
1/6	= 	0.1(6)
1/7	= 	0.(142857)
1/8	= 	0.125
1/9	= 	0.(1)
1/10	= 	0.1

Where 0.1(6) means 0.166666..., and has a 1-digit recurring cycle. It
can be seen that 1/7 has a 6-digit recurring cycle.

Find the value of d < 1000 for which 1/d contains the longest
recurring cycle in its decimal fraction part."

;; http://mathworld.wolfram.com/DecimalExpansion.html
;; Not terribly simple...

(defn step-1 [mx]
  (map #(double (/ 1 %)) (range 1 mx)))

(defn totatives
  "The integers <= x that are relatively prime."
  [x]
  (->> (inc x)
       (range 1 )
       (filter #(= (gcd x %) 1))))

(defn totient
  "The number of integers <= x that are relatively prime. Frequently
  written as phi."
  [x]
  (count (totatives x)))

(defn max-decimal-period
  "When m/n, the decimal period can be no longer than n - 1."
  [m n]
  (- n 1))

(defn relative-prime [x y]
  (= (gcd x y) 1))

(defn lcm [m n]
  (/ (* m n)
     (gcd m n)))


;; http://rosettacode.org/wiki/Multiplicative_order

"In number theory, given an integer a and a positive integer n with
gcd(a,n) = 1, the multiplicative order of a modulo n is the smallest
positive integer k with

ak ≡ 1 (mod n)."

"To determine the multiplicative order of 4 modulo 7, we compute
42 = 16 ≡ 2 (mod 7) and 43 = 64 ≡ 1 (mod 7), so ord7(4) = 3."


;; separate stuff?
;; from http://everything2.com/title/recurring+decimal
"If q is a factor of a power of ten, i.e. it has no factors other than
the factors of 10, 2 and 5, then there is no recurrence, the fraction
ends with all zeroes(which are not written).

There is another rule to the length(\"period\") of the
repetition(recurrence) in a recurring decimal in the fraction 1/x: The
repetition is just as long as the smallest number made up of only the
digit 9 (\"999..\") that is divisible by x.

For example, take 999999 (6 times). It is divisible by its factors
3*3*3*7*11*13*37= 999999. The factors 7 and 13 both appear first in a
9.. number here, and they have a repetition of length 6:"
