//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef RSA_H_
#define RSA_H_

#include "BigInteger.h"
#include "Key.h"
#include "StringTrans.h"

class RSA {
private:
  Key key;
public:
  RSA(int digit = 512) : key(digit / 2) {};
  RSA(const Key& key) : key(key) {};
  RSA(const BigInt& N, const BigInt& E, const BigInt& D) : key(N, E, D) {};
  static BigInt encrypt(const BigInt& src, const BigInt& N, const BigInt& E);
  BigInt encrypt(const BigInt& src) const;
  static void encrypt(StringTrans& st, const BigInt& N, const BigInt& E);
  static BigInt decrypt(const BigInt& src, const BigInt& N, const BigInt& D);
  BigInt decrypt(const BigInt& src) const;
  void decrypt(StringTrans& st);
  void getPublicKey(BigInt& N, BigInt& E) const;
  void getPrivateKey(BigInt& N, BigInt& D) const;
};

#endif
