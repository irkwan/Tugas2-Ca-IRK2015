// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.h

#pragma once

#include <iostream>
#include <deque>
#include <string>
using namespace std;

class biginteger {
public:
	/* Commonly Used Constants */
	static const biginteger ZERO;
	static const biginteger ONE, TWO, THREE, FOUR, FIVE;
	static const biginteger SIX, SEVEN, EIGHT, NINE, TEN;

	/* Constructors */
	biginteger(); // zero
	biginteger(int v);
	biginteger(const string& v);
	biginteger(const biginteger& v);

	/* Operator= */
	biginteger& operator=(const biginteger& rhs);

	/* Arithmetic Operators */
	biginteger operator+(const biginteger& rhs) const;
	biginteger operator-() const; // unary operator
	biginteger operator-(const biginteger& rhs) const;
	biginteger operator*(const biginteger& rhs) const;
	biginteger operator/(const biginteger& rhs) const;
	biginteger operator%(const biginteger& rhs) const;

	biginteger& operator+=(const biginteger& rhs);
	biginteger& operator-=(const biginteger& rhs);
	biginteger& operator*=(const biginteger& rhs);
	biginteger& operator/=(const biginteger& rhs);
	biginteger& operator%=(const biginteger& rhs);

	biginteger abs() const;
	static biginteger pow(const biginteger& a, const biginteger& n);
	static biginteger modpow(const biginteger& a, const biginteger& n, const biginteger& m);

	/* Relational Operators */
	bool operator==(const biginteger& rhs) const;
	bool operator!=(const biginteger& rhs) const;
	bool operator>(const biginteger& rhs) const;
	bool operator>=(const biginteger& rhs) const;
	bool operator<(const biginteger& rhs) const;
	bool operator<=(const biginteger& rhs) const;

	/* I/O */
	friend istream& operator>>(istream &is, biginteger& v);
	friend ostream& operator<<(ostream &os, const biginteger& v);

	/* Other Methods */
	bool isOdd() const;
	bool isEven() const;
	biginteger subDigit(int pos, int n) const; // always yield positive biginteger
	biginteger subDigit(int pos) const; // always yield positive biginteger
	static biginteger gcd(biginteger a, biginteger b);
	static biginteger gcdExtended(biginteger a, biginteger b, biginteger& x, biginteger& y);
	static biginteger generateRandomPrime(int digits = 20);
	bool isProbablePrime();

private:
	deque<int> digits; // reverse-ordered digits
	bool pos; // positive

	static const int BASE = 10;

	static int max(int a, int b);
	void normalize();

	biginteger multiply10(int n);
	static biginteger multiplySingleDigit(const biginteger& lhs, const biginteger& rhs);
	static biginteger karatsubaMultiply(const biginteger& lhs, const biginteger& rhs);
	static pair<biginteger, biginteger> divmod(const biginteger& lhs, const biginteger& rhs);
	static biginteger generateRandomNearlyPrime(int digits);
};
