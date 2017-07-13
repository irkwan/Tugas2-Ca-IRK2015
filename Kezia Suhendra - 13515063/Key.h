//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef KEY_H_
#define KEY_H_

#include "BigInteger.h"
#include "UtilsMsg.h"

class Key {
private:
  int digit;
  BigInt n, e, d;
public:
  Key() : digit(0) {};
  Key(const BigInt& n, const BigInt& e, const BigInt& d): n(n), e(e), d(d) {
    LOGLN("Key: ");
    LOGLN("e: " << e);
    LOGLN("n: " << n);
    LOGLN("d: " << d);
  };
  Key(int digit) {
    this->digit = digit;
    generate(digit);
  }
  void getPublicKey(BigInt& N, BigInt& E) const;
  void getPrivateKey(BigInt& N, BigInt& D) const;
  void generate(int);
};

#endif
