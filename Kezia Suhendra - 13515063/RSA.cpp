//NIM      : 13515063
//Nama     : Kezia Suhendra

#include <iostream>
#include "BigInteger.h"

using namespace std;

BigInteger gcdExtended(BigInteger, BigInteger, BigInteger*, BigInteger*);
BigInteger modInverse(BigInteger, BigInteger);

int main() {

  return 0;
}

BigInteger gcdExtended(BigInteger a, BigInteger b, BigInteger* c, BigInteger* d) {
  if (a == BigInteger(0)) {
    *c = 0;
    *d = 1;
    return b;
  }
  BigInteger x, y;
  BigInteger res = gcdExtended(b%a, a, &x, &y);
  *c = y - (b/a) * x;
  *d = x;
  return res;
}

BigInteger modInverse(BigInteger a, BigInteger m) {
  BigInteger x, y;
  BigInteger n = gcdExtended(a, m, &x, &y);
  if (n != BigInteger(1)) {
    return -1; //inverse doesn't exist
  } else {
    BigInteger res = (x%m + m) % m;
    return res;
  }
}
