// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.h

#pragma once

#include <iostream>
#include <vector>
#include <string>
using namespace std;

class biginteger {
public:
	/* Constructors */
	biginteger();
	biginteger(long long v);
	biginteger(const string& v);
	biginteger(const biginteger& v);

	/* Operator= */
	biginteger& operator=(const biginteger& rhs);

	/* Arithmetic Operators */
	biginteger operator+(const biginteger& rhs);
	biginteger operator-();
	biginteger operator-(const biginteger& rhs);
	biginteger operator*(const biginteger& rhs);
	biginteger operator/(const biginteger& rhs);
	biginteger operator%(const biginteger& rhs);

	biginteger& operator+=(const biginteger& rhs);
	biginteger& operator-=(const biginteger& rhs);
	biginteger& operator*=(const biginteger& rhs);
	biginteger& operator/=(const biginteger& rhs);
	biginteger& operator%=(const biginteger& rhs);

	biginteger add(const biginteger& rhs);
	biginteger min(const biginteger& rhs);
	biginteger mul(const biginteger& rhs);
	biginteger div(const biginteger& rhs);
	biginteger mod(const biginteger& rhs);
	biginteger abs();

	/* Relational Operators */
	bool operator==(const biginteger& rhs);
	bool operator!=(const biginteger& rhs);
	bool operator>(const biginteger& rhs);
	bool operator>=(const biginteger& rhs);
	bool operator<(const biginteger& rhs);
	bool operator<=(const biginteger& rhs);

	/* I/O */
	friend istream& operator>>(istream &is, const biginteger& v);
	friend ostream& operator<<(ostream &os, const biginteger& v);

private:
	vector<int> digits;
	bool pos;

	static const int BASE = 10;

	int max(int a, int b);
};
