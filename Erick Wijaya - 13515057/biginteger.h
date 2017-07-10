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
	biginteger(long long v);
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

	/*biginteger add(const biginteger& rhs) const;
	biginteger min(const biginteger& rhs) const;
	biginteger mul(const biginteger& rhs) const;
	biginteger div(const biginteger& rhs) const;
	biginteger mod(const biginteger& rhs) const;*/
	biginteger abs() const;
	biginteger pow() const;
	biginteger modpow() const;

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

	/* Other */
	biginteger subDigit(int pos, int n) const; // always yield positive biginteger

private:
	deque<int> digits; // reverse order
	bool pos; // positive

	static const int BASE = 10;

	int max(int a, int b) const;
	void normalize();
	long long toLLInt() const;

	static biginteger karatsubaMultiply(const biginteger& lhs, const biginteger& rhs);
	static pair<biginteger, biginteger> divmod(const biginteger& lhs, const biginteger& rhs);
};
