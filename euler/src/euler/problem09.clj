(ns euler.problem9
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

"A Pythagorean triplet is a set of three natural numbers, a < b < c, for which,

a2 + b2 = c2
For example, 32 + 42 = 9 + 16 = 25 = 52.

There exists exactly one Pythagorean triplet for which a + b + c = 1000.
Find the product abc."

(defn check-ascending [a b c]
  (< a b c))


(test/is (check-ascending 1 2 3))
(test/is (not (check-ascending 3 2 3)))

(defn check-py-triplet [a b c]
  (== (+ (* a a)
         (* b b))
      (* c c)))

(test/is (check-py-triplet 3 4 5))

(defn check-adds [a b c]
  (= 1000 (+ a b c)))


(defn solve-1 []
  (time (first (->> (for [x (range 1000)
                          y (range 1000)
                          z (range 1000)]
                      [x y z])
                    (filter #(apply check-ascending %))
                    (filter #(apply check-adds %))
                    (filter #(apply check-py-triplet %))))
        ))

;; "elapsed time: 73949.266 msecs"
;; [200 375 425]

#_ (apply * [200 375 425])
;; 31875000

(defn solve-2 []
  (time (doall (->> (for [x (range 1000)
                          y (range 1000)
                          z (range 1000)
                          :when (and (< x y z)
                                     (= 1000 (+ x y z))
                                     (check-py-triplet x y z))]
                      [x y z])
                    ))
        ))

;; "elapsed time: 59081.932 msecs"
;; ([200 375 425])

(defn solve-3 []
  (time (first (->> (for [x (range 1000)
                          y (range 1000)
                          z (range 1000)
                          :when (and (< x y z)
                                     (== 1000 (+ x y z))
                                     (check-py-triplet x y z))]
                      [x y z])
                    ))))

;; "Elapsed time: 14148.378 msecs"
;; [200 375 425]


;; from site:

(defn ^long pita
  [^long n]
  (loop [a 3 res nil]
    (if res
      res
      (recur (inc a)
             (loop [b (inc a)]
               (let [c (- n a b)]
                 (if (< c b)
                   nil
                   (if (== (+ (* a a) (* b b))
                           (* c c))
                     (* a b c)
                     (recur (inc b))))))))))

;; (time (pita 1000))
;; "elapsed time: 4.033 msecs"
;; 31875000

;; my intrepretation
(defn ^long solve [^long n]
  (time (loop [a 3, ans nil]
          (if ans, ans
              (recur (inc a)
                     (loop [b (inc a)]
                       (let [c (- n a b)]
                         (if (< c b) nil,
                             (if (check-py-triplet a b c)
                               (* a b c)
                               (recur (inc b)))
                             ))))))))

;; "Elapsed time: 5.56 msecs"
;; 31875000

;; performance hints:
;; use type hints
;; use == instead of =
;; use (* a a) instead of Math/pow a 2
;; if only needing one answer, use first, so you don't generate tests you don't need.
