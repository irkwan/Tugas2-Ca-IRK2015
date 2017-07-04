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
	biginteger operator+(const biginteger& rhs) const;
	biginteger operator-() const;
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

	/* Relational Operators */
	bool operator==(const biginteger& rhs) const;
	bool operator!=(const biginteger& rhs) const;
	bool operator>(const biginteger& rhs) const;
	bool operator>=(const biginteger& rhs) const;
	bool operator<(const biginteger& rhs) const;
	bool operator<=(const biginteger& rhs) const;

	/* I/O */
	friend istream& operator>>(istream &is, const biginteger& v);
	friend ostream& operator<<(ostream &os, const biginteger& v);

private:
	vector<int> digits;
	bool pos;

	static const int BASE = 10;

	int max(int a, int b) const;
	void delTrail0();
	long long toLLInt() const;

	static biginteger karatsubaMultiply(const biginteger& lhs, const biginteger& rhs);
	static pair<biginteger, long long> divmod(const biginteger& v, long long den);
};
