(ns euler.problem42
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

"The nth term of the sequence of triangle numbers is given by, tn =
½n(n+1); so the first ten triangle numbers are:

1, 3, 6, 10, 15, 21, 28, 36, 45, 55, ...

By converting each letter in a word to a number corresponding to its
alphabetical position and adding these values we form a word
value. For example, the word value for SKY is 19 + 11 + 25 = 55 =
t10. If the word value is a triangle number then we shall call the
word a triangle word.

Using words.txt (right click and 'Save Link/Target As...'), a 16K text
file containing nearly two-thousand common English words, how many are
triangle words?"

(def alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn by-idx [n]
  (nth alphabet (dec n)))

(defn letter-idx [c]
  (inc (.indexOf (vec alphabet) c)))

(defn word-value [w]
  (reduce + (map letter-idx w)))

(defn get-words []
  (-> (slurp "https://projecteuler.net/project/resources/p042_words.txt")
      (str/split #",")
      (->> (map #(apply str (rest (drop-last %)))))))

#_ (def words (get-words))

(defn word-values [words]
  (map word-value words))

(defn longest-word [words]
  (reduce max (map count words)))
;; => 14

(defn max-word-value [words]
  (reduce max (word-values words)))
;; => 192

(def tri-nums-valid-range (set (take-while #(<= % 192) (map second triangle-numbers))))
;; => (1 3 6 10 15 21 28 36 45 55 66 78 91 105 120 136 153 171 190)

(defn solve []
  (count (filter tri-nums-valid-range (word-values words))))
