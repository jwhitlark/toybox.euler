(ns euler.problem59
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

"Each character on a computer is assigned a unique code and the
preferred standard is ASCII (American Standard Code for Information
Interchange). For example, uppercase A = 65, asterisk (*) = 42, and
lowercase k = 107.

A modern encryption method is to take a text file, convert the bytes
to ASCII, then XOR each byte with a given value, taken from a secret
key. The advantage with the XOR function is that using the same
encryption key on the cipher text, restores the plain text; for
example, 65 XOR 42 = 107, then 107 XOR 42 = 65.

For unbreakable encryption, the key is the same length as the plain
text message, and the key is made up of random bytes. The user would
keep the encrypted message and the encryption key in different
locations, and without both \"halves\", it is impossible to decrypt
the message.

Unfortunately, this method is impractical for most users, so the
modified method is to use a password as a key. If the password is
shorter than the message, which is likely, the key is repeated
cyclically throughout the message. The balance for this method is
using a sufficiently long password key for security, but short enough
to be memorable.

Your task has been made easy, as the encryption key consists of three
lower case characters. Using cipher.txt (right click and 'Save
Link/Target As...'), a file containing the encrypted ASCII codes, and
the knowledge that the plain text must contain common English words,
decrypt the message and find the sum of the ASCII values in the
original text."

(def cipher-txt-source "https://projecteuler.net/project/resources/p059_cipher.txt")

(defn get-cipher []
  (-> cipher-txt-source
      slurp
      str/trim
      (str/split #",")
      (->> (map str->int))))

;; (* 26 26 26) ; possible combinations of 3 lower case letters.
;; 17576

(def letters (map char (range 97 123)))
(def common (conj letters \ ))
(def pos-keys (map ->ascii (combo/selections letters 3)))

(defn ->str [cipher]
  (->> cipher
       (map char)
       (apply str)))

(defn ->ascii [s]
  (map int s))

(defn test-plain [s]
  (let [total (count s)
        ;;freq (frequencies s)
        cm (filter #(get (set common) %) s)
        ]
    (/ (count cm) total)
    ))

(defn encode [pt ky]
  (map #(char (bit-xor %1 %2)) pt (cycle ky)))

(defn attempt-decode [cp ky]
  (map #(char (bit-xor %1 %2)) cp (cycle ky)))

(defn find-probable []
  (filter #(> (test-plain (attempt-decode cipher-txt (->ascii  %))) 0.9) pos-keys))

(defn solve []
  (time (let [prob (first (find-probable))
              pt (apply str (attempt-decode cipher-txt prob))]
          (println pt)
          (println (apply str (map char prob)))
          (reduce + (map int pt)))))

;; euler.problem59> (solve)
;; (The Gospel of John, chapter 1) 1 In the beginning the Word already existed. He was with God, and he was God. 2 He was in the beginning with God. 3 He created everything there is. Nothing exists that he didn't make. 4 Life itself was in him, and this life gives light to everyone. 5 The light shines through the darkness, and the darkness can never extinguish it. 6 God sent John the Baptist 7 to tell everyone about the light so that everyone might believe because of his testimony. 8 John himself was not the light; he was only a witness to the light. 9 The one who is the true light, who gives light to everyone, was going to come into the world. 10 But although the world was made through him, the world didn't recognize him when he came. 11 Even in his own land and among his own people, he was not accepted. 12 But to all who believed him and accepted him, he gave the right to become children of God. 13 They are reborn! This is not a physical birth resulting from human passion or plan, this rebirth comes from God.14 So the Word became human and lived here on earth among us. He was full of unfailing love and faithfulness. And we have seen his glory, the glory of the only Son of the Father.
;; god
;; "Elapsed time: 20366.095 msecs"
;; 107359


;; off site, looks pretty nice - very fast
(defn solved []
  (time (reduce +
                (map byte
                     (let [chars (range (byte \a) (inc (byte \z)))
                           cypher (map #(Byte/parseByte %)
                                       (re-seq #"\d+"
                                               (slurp "https://projecteuler.net/project/resources/p059_cipher.txt")))]
                       (first (for [a chars b chars c chars
                                    :let [s (apply str (map (comp char bit-xor) cypher (cycle [a b c])))]
                                    :when (not (neg? (.indexOf s " the ")))]
                                s)))))))

;; euler.problem59> (solved)
;; "Elapsed time: 1406.413 msecs"
;; 107359
