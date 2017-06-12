(ns euler.common-test
  (:require [euler.common :refer :all]
            [clojure.test :refer :all]))

(deftest fib-test
  (testing "fib-seq test"
    (is (= (take 13 (fib-seq)) '(0 1 1 2 3 5 8 13 21 34 55 89 144)))))

(deftest gcd-test
  (testing "gcd"
    (is (= (gcd 2 4) 2))
    (is (= (gcd 5 3) 1)))
  )
