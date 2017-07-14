"""
  Name : Dery Rahman Ahaddienata
  Nim  : 13515097
  
  BigPrime lib. Generate prime number. Expected not larger than 2^64. So it still realistic not to use BigInt :)
"""
import random
import math
import sys
from BigInt import BigInt

"""
  Rabin miller primality test
"""
def rabin_miller(n):
  s = n-1
  t = 0
  # Masking
  while s&1 == 0:
    s = s/2
    t +=1
    k = 0
  while k<128:
    a = random.randrange(2,n-1)
    v = pow(a,s,n)
    if v != 1:
      i=0
      while v != (n-1):
        if i == t-1:
          return False
        else:
          i = i+1
          v = (v**2)%n
    k+=2
  return True

"""
  Baillie primality tesr
"""
def is_prime(n):
  low_primes = [3,5,7,11,13,17,19,23]
  if (n >= 3):
    if (n&1 != 0):
      for p in low_primes:
        if (n == p): return True
        if (n % p == 0): return False
        return rabin_miller(n)
  return False

"""
  Function to generate large prime with length of bit
"""
def generate_large_prime(k):
  while True:
    n = random.randrange(2**(k-1),2**(k))
    if is_prime(n) == True:
      return n
