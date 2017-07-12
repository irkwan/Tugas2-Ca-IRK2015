//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef OAEP_H_
#define OAEP_H_

#include "BigInteger.h"
#include "StringTrans.h"

class OAEP {
private:
  int K, M;
  BigInt KtoM(const BigInt& cnt);
  BigInt MtoK(const BigInt& cnt);
public:
  OAEP(int k = 16, int m = 32);
  BigInt encode(const BigInt& cnt);
  void encode(StringTrans& st);
  BigInt decode(const BigInt& cnt);
  void decode(StringTrans& st);
};

#endif
