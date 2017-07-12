//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef STRINGTRANS_H_
#define STRINGTRANS_H_

#include "BigInteger.h"
#include <vector>

using namespace std;

class StringTrans : public vector<BigInt> {
private:
  void split(const string&);
  int BitLen;
public:
  StringTrans(const string& a, int b);
  string toString();
  string toHexString();
  void push_back(const string&);
  void push_back(const BigInt&);
  StringTrans& operator+= (const string&);
  StringTrans& operator+= (const BigInt&);
  StringTrans& operator+= (const StringTrans&);
};

#endif
