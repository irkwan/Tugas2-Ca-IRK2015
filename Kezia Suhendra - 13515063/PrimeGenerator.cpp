//NIM      : 13515063
//Nama     : Kezia Suhendra

#include "PrimeGenerator.h"
#include "BigInteger.h"
#include "UtilsMsg.h"

void GenPrime(BigInt& n, int digit) {
  int i = 0;
  BigInt divisor;
  const int length = sizeof(prime) / sizeof(int);
  while (i != length) {
    n.Random(digit);
    while (!n.IsOdd()) {
      n.Random(digit);
    }
    i = 0;
    for ( ; i < length; i++) {
      divisor = prime[i];
      if ((n % divisor) == 0) {
        break;
      }
    }
  }
}

bool RabinMiller(const BigInt& n, int digit) {
  BigInt r, a, y;
  unsigned int s, j;
  r = n - 1;
  s = 0;
  while (!r.IsOdd()) {
    s++;
    r >> 1;
  }
  a.Randomsmall(digit);
  y = BigInt::ModPow(a, r, n);
  if ((!(y == 1)) && (!(y == (n - 1)))) {
    j = 1;
    while ((j <= s - 1) && (!(y == (n - 1)))) {
      y = (y * y) % n;
      if (y == 1) {
        return false;
      }
      j++;
    }
    if (!(y == (n - 1))) {
      return false;
    }
  }
  return true;
}

BigInt GeneratePrime(int digit) {
  BigInt n;
  int i = 0;
  LOG("\nGenerating Prime Number ");
  while (i < 5) {
    GenPrime(n, digit);
    i = 0;
    for ( ; i < 5; i++) {
      if (!RabinMiller(n, digit)) {
        LOG(".");
        break;
      }
    }
  }
  LOGLN("\n" << n);
  return n;
}
