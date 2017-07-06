/*
Author : Arno Alexander
*/

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

#include <iostream>
#include <deque>
#include <cmath>
#include <utility>

using namespace std;

class BigNumber {
public:
	/*CONSTRUCTORS*/
	BigNumber();
	BigNumber(long long n);
	BigNumber(const string& str);
	BigNumber(const BigNumber& bn);

	/*OPERATORS*/
	BigNumber& operator=(long long n);
	BigNumber& operator=(const string& str);
	BigNumber& operator=(const BigNumber& bn);

	friend bool operator==(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator==(const BigNumber& bn, long long n);
	friend bool operator==(long long n, const BigNumber& bn);
	friend bool operator==(const BigNumber& bn, const string& str);
	friend bool operator==(const string& str, const BigNumber& bn);
	friend bool operator!=(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator!=(const BigNumber& bn, long long n);
	friend bool operator!=(long long n, const BigNumber& bn);
	friend bool operator!=(const BigNumber& bn, const string& str);
	friend bool operator!=(const string& str, const BigNumber& bn);
	friend bool operator>(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator>(const BigNumber& bn, long long n);
	friend bool operator>(long long n, const BigNumber& bn);
	friend bool operator>(const BigNumber& bn, const string& str);
	friend bool operator>(const string& str, const BigNumber& bn);
	friend bool operator>=(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator>=(const BigNumber& bn, long long n);
	friend bool operator>=(long long n, const BigNumber& bn);
	friend bool operator>=(const BigNumber& bn, const string& str);
	friend bool operator>=(const string& str, const BigNumber& bn);
	friend bool operator<(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator<(const BigNumber& bn, long long n);
	friend bool operator<(long long n, const BigNumber& bn);
	friend bool operator<(const BigNumber& bn, const string& str);
	friend bool operator<(const string& str, const BigNumber& bn);
	friend bool operator<=(const BigNumber& bn1, const BigNumber& bn2);
	friend bool operator<=(const BigNumber& bn, long long n);
	friend bool operator<=(long long n, const BigNumber& bn);
	friend bool operator<=(const BigNumber& bn, const string& str);
	friend bool operator<=(const string& str, const BigNumber& bn);


	friend BigNumber operator+(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber operator+(const BigNumber& bn, long long n);
	friend BigNumber operator+(long long n, const BigNumber& bn);
	friend BigNumber operator+(const BigNumber& bn, const string& str);
	friend BigNumber operator+(const string& str, const BigNumber& bn);
	friend BigNumber operator-(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber operator-(const BigNumber& bn, long long n);
	friend BigNumber operator-(long long n, const BigNumber& bn);
	friend BigNumber operator-(const BigNumber& bn, const string& str);
	friend BigNumber operator-(const string& str, const BigNumber& bn);
	friend BigNumber operator*(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber operator*(const BigNumber& bn, long long n);
	friend BigNumber operator*(long long n, const BigNumber& bn);
	friend BigNumber operator*(const BigNumber& bn, const string& str);
	friend BigNumber operator*(const string& str, const BigNumber& bn);
	friend BigNumber operator/(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber operator/(const BigNumber& bn, long long n);
	friend BigNumber operator/(long long n, const BigNumber& bn);
	friend BigNumber operator/(const BigNumber& bn, const string& str);
	friend BigNumber operator/(const string& str, const BigNumber& bn);
	friend BigNumber operator%(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber operator%(const BigNumber& bn, long long n);
	friend BigNumber operator%(long long n, const BigNumber& bn);
	friend BigNumber operator%(const BigNumber& bn, const string& str);
	friend BigNumber operator%(const string& str, const BigNumber& bn);
	BigNumber& operator+=(const BigNumber& bn);
	BigNumber& operator+=(long long n);
	BigNumber& operator+=(const string& str);
	BigNumber& operator-=(const BigNumber& bn);
	BigNumber& operator-=(long long n);
	BigNumber& operator-=(const string& str);
	BigNumber& operator*=(const BigNumber& bn);
	BigNumber& operator*=(long long n);
	BigNumber& operator*=(const string& str);
	BigNumber& operator/=(const BigNumber& bn);
	BigNumber& operator/=(long long n);
	BigNumber& operator/=(const string& str);
	BigNumber& operator%=(const BigNumber& bn);
	BigNumber& operator%=(long long n);
	BigNumber& operator%=(const string& str);

	friend istream& operator>>(istream& in, BigNumber& bn);
	friend ostream& operator<<(ostream& out, const BigNumber& bn);

	/*METHODS*/
	string toString() const;
	BigNumber absolute() const;
	static BigNumber gcd(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber lcm(const BigNumber& bn1, const BigNumber& bn2);

private:
	/*ATTRIBUTES*/
	const static unsigned base = 1000000000;
	deque <unsigned> digits; //little endian
	bool isZero;
	bool isNegative;

	/*METHODS*/
	void normalizeForm();
	static bool isUnsignedGreater(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber unsignedSum(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber unsignedDifference(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber unsignedMultiply(const BigNumber& bn1, const BigNumber& bn2);
	static pair<BigNumber,BigNumber> unsignedDivide(const BigNumber& bn1, const BigNumber& bn2);
};

#endif