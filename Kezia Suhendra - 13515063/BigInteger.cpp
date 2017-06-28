#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
#include "BigInteger.h"

using namespace std;

BigInteger::BigInteger() {
  sign = true;
}

BigInteger::BigInteger(int num) {
  if (num >= 0) {
    sign = true;
  } else {
    sign = false;
    num *= (-1);
  }
  do {
    number.push_back((char)(num % 10));
    num /= 10;
  } while (num != 0)
}

BigInteger::BigInteger(string& num) {
  sign = true;
  for (string::reverse_iterator idx = num.rbegin(); idx < num.rend(); ++idx) {
    char c = (*idx);
    if (idx == num.rend()-1) {
      if (c == '+') {
        break;
      }
      if (c == '-') {
        sign = false;
        break;
      }
    }
  }
  trimZero();
}

BigInteger::BigInteger(const BigInteger& big) {
  number = big.number;
  sign = big.sign;
}

BigInteger BigInteger::operator=(const BigInteger& big) {
  if (this != &big) {
    number = big.number;
    sign = big.sign;
  }
  return * this;
}

void BigInteger::trimZero() {
  vector<char>::reverse_iterator idx = number.rbegin();
  while (!number.empty() && (*idx) == 0) {
    number.pop_back();
    idx = number.rbegin();
  }
  if (number.size() == 0) {
    sign = true;
    number.push_back(0);
  }
}

BigInteger BigInteger::absolute() const {
  if (sign) {
    return * this;
  } else {
    return -(*this);
  }
}

BigInteger BigInteger::pow(int p) {
  BigInteger ex(1);
  for (int i = 0; i < p; ++i) {
    ex *= (*this);
  }
  return ex;
}

BigInteger operator+(const BigInteger&, const BigInteger&);
BigInteger operator++(BigInteger&);
BigInteger operator++(BigInteger&, int);
BigInteger operator-(const BigInteger&, const BigInteger&);
BigInteger operator-(const BigInteger&);
BigInteger operator--(BigInteger&);
BigInteger operator--(BigInteger&, int);
BigInteger operator*(const BigInteger&, const BigInteger&);
BigInteger operator/(const BigInteger&, const BigInteger&);
BigInteger operator%(const BigInteger&, const BigInteger&);
BigInteger operator+=(const BigInteger&, const BigInteger&);
BigInteger operator-=(const BigInteger&, const BigInteger&);
BigInteger operator*=(const BigInteger&, const BigInteger&);
BigInteger operator/=(const BigInteger&, const BigInteger&);
BigInteger operator%=(const BigInteger&, const BigInteger&);
bool operator>(const BigInteger&, const BigInteger&);
bool operator<(const BigInteger&, const BigInteger&);
bool operator>=(const BigInteger&, const BigInteger&);
bool operator<=(const BigInteger&, const BigInteger&);
bool operator!=(const BigInteger&, const BigInteger&);
bool operator==(const BigInteger&, const BigInteger&);
ostream& operator<<(ostream&, const BigInteger&);
istream& operator>>(istream&, BigInteger&);
