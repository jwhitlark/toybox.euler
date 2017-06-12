(ns euler.common
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

            [clojure.math.numeric-tower :as clj-math]

            [flatland.useful.debug :refer [?]]
            [flatland.useful.map :refer [merge-in]]
            [flatland.useful.fn :refer [! validator as-fn thrush applied]]
            [flatland.useful.utils :refer [verify returning pop-if queue let-later empty-coll? with-timing]]
            ))

;; -------------------- constants

;; Don't use these, just reminders
;; (def one-thousand 1e3)
;; (def one-million 1e6)
;; (def one-billion 1e9)

(defn third [sq] (first (next (next sq))))

(def not-empty? (complement empty?))

;; -------------------- formatting

(defn ->binary [i]
  (Integer/toString i 2))

;; -------------------- number theory

(def natural-numbers (iterate inc 0))
(def positive-integers (iterate inc 1))

(defn triangle-number
  "Fastest to calculate a single triangle-number"
  [n]
  (* n (/ (+ n 1) 2)))

(defn- tri
  ([] (tri 1 1))
  ([n t] (let [n2 (inc n)
               t2 (+ n2 t)]
           [n2 t2])))

;; Sequence of triangle-numbers
(def triangle-numbers
  (iterate #(apply tri %) [1 1]))

(defn pentagonal-number [n]
  (* n (/ (dec (* 3 n )) 2)))

(defn hexagonal-number [n]
  (* n (dec (* 2 n))))

(defn arithmetic-progression?
  "Check that each element is a constant increase/decrease of the
  previous. One and two element vectors would be true by definition."
  [v-prime]
  (->>  v-prime
        (partition 3 1)
        (reduce #(and %1 (= (- (second %2) (first %2))
                            (- (third %2) (second %2))))
                true)))

(defn digit?
  "Handles single char or strings.  Does not handle hex, octal, or
  anything other than base 10 numerals."
  [c]
  (cond
    (instance? java.lang.Character c)
      (Character/isDigit c)
    (instance? java.lang.String c)
      (every? digit? c)))

(defn digits
  "Get the digits |i|, as a vector, in order."
  [i]
  (->> i
       clj-math/abs
       str
       (mapv #(Integer/parseInt (str %)))))

(defn fib-step [[a b]]
  [b (bigint (+ a b))])

(defn fib-seq []
  (map first (iterate fib-step [0 1])))

;; -------------------- factorial

(defn- factorial-raw [n]
  (reduce * (range (biginteger 1) (inc (biginteger n)))))

(def factorial (memoize factorial-raw))

;; -------------------- powers

(defn square [x]
  (Math/pow x 2))

;; -------------------- Conversions

(defn char->int [c]
  (Integer/parseInt (if (seq? c)
                        (apply str c)
                        (str c))))

(defn str->int [s]
  (Integer/parseInt s))

;; -------------------- Factoring

(defn divisor? [x y]
  (zero? (mod x y)))

(defn half-factors
  "Calculate the first half of the factors of n."
  [n]
  ;; bug when counting factors of 1
  (let [factor? (partial divisor? n)]
    (->> (range 1
                (Math/sqrt n)
                (if (odd? n) 2 1))
         (filter factor?))))

;; TODO: better factors, not sure how

(defn factors-raw
  "Calculate the factors of n."
  [n]
  ;; bug when counting factors of 1
  (let [factor? (partial divisor? n)]
    (->> (range 1
                (inc (/ n 2))
                (if (odd? n) 2 1))
         (filter factor?))))

(def factors (memoize factors-raw))

(defn divisor-count [n]
  ;; bug when counting factors of 1
  (* 2 (count (half-factors n))))

(defn gcd
  "Greatest Common Divsor"
  [x y]
  (.gcd (biginteger x) (biginteger y)))

(defn lcm
  "Least Common Multiple"
  [m n]
  (/ (* m n)
     (gcd m n)))

;; -------------------- Primes

(defn next-prime [n]
  (.nextProbablePrime (biginteger n)))

(defn primes
  ([] (primes 1))
  ;; drop first, as we're not checking it.
  ([starting-with] (drop 1 (iterate next-prime starting-with))))

(defn prime?
  ([n] (prime? 5 n))
  ([certainty n] (.isProbablePrime (BigInteger/valueOf n) certainty)))

(defn next-composite [n]
  (first (take 1 (filter #(not (prime? %)) (iterate inc (inc n))))))

(defn composite? [n]
  (not (prime? n)))

(defn composites []
  (iterate next-composite 4))

(defn prime-factors [x]
  (let [num (biginteger x)
        limit (inc (/ x 2))]
    (->> (primes 0)
         (take-while #(< % limit))
         (filter #(divisor? num %))
         )))

(defn relative-prime [x y]
  (= (gcd x y) 1))

(defn max-decimal-period
  "When m/n, the decimal period can be no longer than n - 1. Not positive this is correct."
  [m n]
  (- n 1))

;; -------------------- EulerPhi

(defn totatives
  "The integers <= x that are relatively prime."
  [x]
  (->> (inc x)
       (range 1)
       (filter #(= (gcd x %) 1))))

(defn totient
  "The number of integers <= x that are relatively prime. Frequently
  written as phi."
  [x]
  (count (totatives x)))

;; -------------------- From problem 23
;; These need to be cleaned up and memoized.
(defn factor-sum [x]
  (reduce + (factors x)))

(defn deficient? [x]
  (< (factor-sum x) x))

(defn abundant-raw? [x]
  (< x (factor-sum x)))

(def abundant? (memoize abundant-raw?))

(defn perfect? [x]
  (= x (factor-sum x)))

;; (def positive-integers (iterate inc 1))

;; (defn primz? [x]
;;   (let [candiates (iterate inc 2)]
;;     (filter #(= 0 (mod x %)) candiates)
;;     )
;;   )

;; (defn primz [cnt]
;;   (take cnt positive-integers)
;;   )
