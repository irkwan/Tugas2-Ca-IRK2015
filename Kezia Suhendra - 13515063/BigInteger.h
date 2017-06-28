#ifndef BIGINT_H_
#define BIGINT_H_

#include <string>
#include <vector>
#include<iostream>

using namespace std;

class BigInteger {
public:
  BigInteger();
  BigInteger(int);
  BigInteger(string&);
  BigInteger(const BigInteger&);
  BigInteger operator=(const BigInteger& opr);
  BigInteger absolute() const;
  BigInteger pow(int p);
  friend BigInteger operator+(const BigInteger&, const BigInteger&);
  friend BigInteger operator++(BigInteger&);
  friend BigInteger operator++(BigInteger&, int);
  friend BigInteger operator-(const BigInteger&, const BigInteger&);
  friend BigInteger operator-(const BigInteger&);
  friend BigInteger operator--(BigInteger&);
  friend BigInteger operator--(BigInteger&, int);
  friend BigInteger operator*(const BigInteger&, const BigInteger&);
  friend BigInteger operator/(const BigInteger&, const BigInteger&);
  friend BigInteger operator%(const BigInteger&, const BigInteger&);
  friend BigInteger operator+=(const BigInteger&, const BigInteger&);
  friend BigInteger operator-=(const BigInteger&, const BigInteger&);
  friend BigInteger operator*=(const BigInteger&, const BigInteger&);
  friend BigInteger operator/=(const BigInteger&, const BigInteger&);
  friend BigInteger operator%=(const BigInteger&, const BigInteger&);
  friend bool operator>(const BigInteger&, const BigInteger&);
  friend bool operator<(const BigInteger&, const BigInteger&);
  friend bool operator>=(const BigInteger&, const BigInteger&);
  friend bool operator<=(const BigInteger&, const BigInteger&);
  friend bool operator!=(const BigInteger&, const BigInteger&);
  friend bool operator==(const BigInteger&, const BigInteger&);
  friend ostream& operator<<(ostream&, const BigInteger&);
  friend istream& operator>>(istream&, BigInteger&);

private:
  vector<char> number;
  bool sign;
  void trimZero();
};

#endif
