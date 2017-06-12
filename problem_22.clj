#! /usr/bin/env clojure

(use '[clojure.contrib.str-utils])

(def abcs "ABCDEFGHIJKLMNOPQRSTUVWXYZ")
(def whole-nums #(iterate inc 1))

(def names (slurp "/home/jw/Dropbox/fun/euler/names.txt"))


(defn find-letter-value [l]
  (inc (.indexOf abcs l)))

(defn find-name-value [nm]
  (reduce + (map find-letter-value (map str nm))))

(defn clean-and-filter-list [nms]
  (sort (re-split #"," (apply str (re-gsub #"\"" ""  nms)))))


(defn group-name-values [coll]
  (partition 2 (interleave (whole-nums) (map find-name-value coll))))

(defn process-names [coll]
  (group-name-values (clean-and-filter-list coll)))

(defn finalize-value [coll]
  (apply * coll))

(defn solve-problem [nms]
  (reduce + (map finalize-value (process-names nms))))
