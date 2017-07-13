//NIM      : 13515063
//Nama     : Kezia Suhendra

#ifndef BIGINT_H_
#define BIGINT_H_
#include <iostream>
#include <string>
#include <stdlib.h>
#include "PrimeData.h"

using namespace std;

class BigInt {
public:
  BigInt();
  BigInt(const int&);
  BigInt(const BigInt&);
  BigInt(string, int);
  void GenFromHexString(string str);
  void GenFromBinString(string str);
  void GenFromByteString(const string& buf);
  string ToString() const;
  string ToHexString() const;
  BigInt& operator= (const BigInt&);
  BigInt& operator= (int& a) {
    Clear();
    num[0] = a;
    return *this;
  }
  BigInt& operator>> (const int&);
  BigInt& operator<< (const int&);
  int GetBitLength() const;
  int GetLength() const;
  bool TestSign() const {return sign;}
  void Clear();
  void Random(int digit);
  void Randomsmall(int digit);
  bool IsOdd() const {return (num[0] & 1);}
  BigInt operator+ (const BigInt&) const;
  BigInt operator- (const BigInt&) const;
  BigInt operator- (const int&) const;
  BigInt operator* (const BigInt&) const;
  BigInt operator* (const unsigned int&) const;
  BigInt operator% (const BigInt&) const;
  int operator% (const int&) const;
  BigInt operator/ (const BigInt&) const;
  BigInt operator& (const BigInt&) const;
  BigInt operator^ (const BigInt&) const;
  BigInt operator| (const BigInt&) const;
  bool operator< (const BigInt&) const;
  bool operator> (const BigInt&) const;
  bool operator<= (const int&) const;
  bool operator== (const BigInt&) const;
  bool operator== (const int&) const;
  friend ostream& operator<< (ostream&, const BigInt&);
  static BigInt ModPow(const BigInt& n, const BigInt& p, const BigInt& m);
  static BigInt Gcd(const BigInt& m, const BigInt& n);
  static BigInt Euc(BigInt& E, BigInt& A);
  static BigInt GcdExtended(const BigInt& a, const BigInt& b, BigInt& x, BigInt& y);
private:
  static const size_t _capacity = 128 + 1;
  unsigned int num[_capacity];
  bool sign;
  void output(ostream& out) const;
  int HexCharToInt(char c);
  char IntToHexChar(int n);
};

enum STRING_TYPE {
  BIN_STRING = 2,
  HEX_STRING = 16,
  BYTE_STRING = 10
};

#endif
