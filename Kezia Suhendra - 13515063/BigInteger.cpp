#include <vector>
#include <string>
#include <iostream>
#include <algorithm>
#include "BigInteger.h"

using namespace std;

BigInteger::BigInteger();
BigInteger::BigInteger(int);
BigInteger::BigInteger(string&);
BigInteger::BigInteger(const BigInteger&);
BigInteger BigInteger::operator=(const BigInteger& opr);
void BigInteger::trimZero();
BigInteger BigInteger::absolute() const;
BigInteger BigInteger::pow(int p);
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
