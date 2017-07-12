//NIM      : 13515063
//Nama     : Kezia Suhendra

#include "Key.h"
#include "BigInteger.h"
#include "PrimeGenerator.h"
#include "PrimeData.h"
#include "UtilsMsg.h"
#include <iostream>
#include <ctime>
#include <string>
#include <stdlib.h>

using namespace std;

void Key::getPublicKey(BigInt& N, BigInt& E) const {
  N = this->n;
  E = this->e;
}
void Key::getPrivateKey(BigInt& N, BigInt& D) const {
  N = this->n;
  D = this->d;
}
void Key::generate(int digit) {
  this->digit = digit;
  srand((unsigned)time(NULL));
  BigInt p(GeneratePrime(digit));
  BigInt q(GeneratePrime(digit));
  LOGLN("Key: ");
  BigInt totient = (p - 1) * (q - 1);
  BigInt x, y, temp;
  while (1) {
    e.Random(digit);
    while (!(BigInt::Gcd(e, totient) == 1)) {
      e.Random(digit);
    }
    temp = BigInt::GcdExtended(e, totient, x, y);
    temp = (e * x) % totient;
    if (temp == 1) {
      break;
    }
  }
  LOGLN("e: " << e);
  n = p * q;
  LOGLN("n: " << n);
  d = x;
  LOGLN("d: " << d);
}
