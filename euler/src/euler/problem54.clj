(ns euler.problem54
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

"In the card game poker, a hand consists of five cards and are
ranked, from lowest to highest, in the following way:

High Card: Highest value card.
One Pair: Two cards of the same value.
Two Pairs: Two different pairs.
Three of a Kind: Three cards of the same value.
Straight: All cards are consecutive values.
Flush: All cards of the same suit.
Full House: Three of a kind and a pair.
Four of a Kind: Four cards of the same value.
Straight Flush: All cards are consecutive values of same suit.
Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
The cards are valued in the order:
2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.

If two players have the same ranked hands then the rank made up of the
highest value wins; for example, a pair of eights beats a pair of
fives (see example 1 below). But if two ranks tie, for example, both
players have a pair of queens, then highest cards in each hand are
compared (see example 4 below); if the highest cards tie then the next
highest cards are compared, and so on.

Consider the following five hands dealt to two players:

Hand	 	Player 1	 	Player 2	 	Winner
1	 	5H 5C 6S 7S KD
Pair of Fives
 	2C 3S 8S 8D TD
Pair of Eights
 	Player 2
2	 	5D 8C 9S JS AC
Highest card Ace
 	2C 5C 7D 8S QH
Highest card Queen
 	Player 1
3	 	2D 9C AS AH AC
Three Aces
 	3D 6D 7D TD QD
Flush with Diamonds
 	Player 2
4	 	4D 6S 9H QH QC
Pair of Queens
Highest card Nine
 	3D 6D 7H QD QS
Pair of Queens
Highest card Seven
 	Player 1
5	 	2H 2D 4C 4D 4S
Full House
With Three Fours
 	3C 3D 3S 9S 9D
Full House
with Three Threes
 	Player 1

The file, poker.txt, contains one-thousand random hands dealt to two
players. Each line of the file contains ten cards (separated by a
single space): the first five are Player 1's cards and the last five
are Player 2's cards. You can assume that all hands are valid (no
invalid characters or repeated cards), each player's hand is in no
specific order, and in each hand there is a clear winner.

How many hands does Player 1 win?"

(def src "https://projecteuler.net/project/resources/p054_poker.txt")

;; split on \n
;; partition by 5
;; conv. to cards?
;; split to 2 hands
;; score hand

;; scoring hand - max type, max win, then high cards
;; CDSH

"
High Card: Highest value card.
One Pair: Two cards of the same value.
Two Pairs: Two different pairs.
Three of a Kind: Three cards of the same value.
Straight: All cards are consecutive values.
Flush: All cards of the same suit.
Full House: Three of a kind and a pair.
Four of a Kind: Four cards of the same value.
Straight Flush: All cards are consecutive values of same suit.
Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
The cards are valued in the order:
2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
"
(def suites {"C" :clubs
             "H" :hearts
             "S" :spades
             "D" :diamonds})

(def face-values {"1" 1
                  "2" 2
                  "3" 3
                  "4" 4
                  "5" 5
                  "6" 6
                  "7" 7
                  "8" 8
                  "9" 9
                  "T" 10
                  "J" 11
                  "Q" 12
                  "K" 13
                  "A" 14
                  })

(defn parse-card [cd]
  (let [[v, s] cd]
    {:v (get face-values v)
     :s (get suites s)}
    )
  )

(defn prep-hand [rw]


)

(defn flush [hd]
  (= 1 (count (set (map :s hd)))))

(defn four-of-a-kind [hd]
  (ffirst (filter #(= (val %) 4) (frequencies (map :v hd)))))

(defn full-house [hd]
  (and (ffirst (filter #(= (val %) 2) (frequencies (map :v hd))))
       (ffirst (filter #(= (val %) 3) (frequencies (map :v hd))))))

(defn three-of-a-kind [hd]
  (ffirst (filter #(= (val %) 3) (frequencies (map :v hd)))))

(defn pairs [hd]
  (filter #(= (val %) 2) (frequencies (map :v hd))))

(defn one-pair [hd]
  (let [p (pairs hd)]
    (when (= 1(count p))
      p)))

(defn two-pair [hd]
  (let [ps (pairs hd)]
    (if (= 2 (count ps))
      ps)))

(defn high-card [hd]
  (reduce max (map :v hd)))

(defn straight? [hd]
  (let [vs (map :v hd)]
    (let [sorted-vals (sort vs)
          pos-straight (range (first sorted-vals)
                              (inc (last sorted-vals)))]
      (= sorted-vals pos-straight))))

(defn rank-hand [hd]
 (cond
   ;; royal-flush
   ;; straight-flush
   ;; four-of-a-kind
   )
)

(def s-1 [{:v 5 :s :diamonds}
          {:v 5 :s :hearts}
          {:v 5 :s :clubs}
          {:v 5 :s :spades}
          {:v 2 :s :hearts}])

(def s-2 [{:v 5 :s :diamonds}
          {:v 5 :s :hearts}
          {:v 5 :s :clubs}
          {:v 2 :s :spades}
          {:v 2 :s :hearts}])

(def s-3 [{:v 5 :s :diamonds}
          {:v 5 :s :hearts}
          {:v 8 :s :clubs}
          {:v 2 :s :spades}
          {:v 2 :s :hearts}])

(def s-4 [{:v 5 :s :diamonds}
          {:v 2 :s :diamonds}
          {:v 8 :s :diamonds}
          {:v 9 :s :diamonds}
          {:v 3 :s :diamonds}])

(def s-5 [{:v 5 :s :diamonds}
          {:v 6 :s :diamonds}
          {:v 9 :s :diamonds}
          {:v 7 :s :diamonds}
          {:v 8 :s :diamonds}])
