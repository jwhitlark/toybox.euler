#! /usr/bin/python

"""
A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91 * 99

Find the largest palindrome made from the product of two 3-digit numbers.
"""

import sys

def fix_odd(num):
    """
    >>> fix_odd(1000)
    ('10', '00')

    >>> fix_odd(10100)
    ('10', '00')

    >>> fix_odd(123456789)
    ('1234', '6789')

    >>> fix_odd('123456789')
    ('1234', '6789')
    """
    x = str(num)
    l = len(x)
    half_l = l / 2
    return (x[:- half_l - l % 2] , x[- half_l:])


def compare_halves(num):
    """
    >>> compare_halves(('12', '21'))
    True

    >>> compare_halves(('12', '34'))
    False

    """
    return num[0] == ''.join(reversed(list(num[1])))

def compute_pals(max):
    """
    >>> compute_pals(100)
    9009

    """
    best = 0
    best_factors = ()
    for i in range(max):
        for j in range(i - 1):
            candidate = i * j
            val = fix_odd(candidate)
            if compare_halves(val):
                if candidate > best:
                    best = candidate
                    best_factors = (i,j)
    sys.stderr.write(str(best_factors))
    return best

if __name__ == '__main__':
    print "For two 3 digit #'s, the largest palindrome is %s" % compute_pals(1000)
