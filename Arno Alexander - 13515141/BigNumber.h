/*
Author : Arno Alexander
*/

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

#include <iostream>
#include <deque>
#include <cmath>
#include <utility>
#include <cstdlib>
#include <ctime>

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
	friend BigNumber operator/(const BigNumber& bn1, const BigNumber& bn2); //bn2!=0
	friend BigNumber operator/(const BigNumber& bn, long long n); //n!=0
	friend BigNumber operator/(long long n, const BigNumber& bn); //bn!=0
	friend BigNumber operator/(const BigNumber& bn, const string& str); //str!=0
	friend BigNumber operator/(const string& str, const BigNumber& bn); //bn!=0
	friend BigNumber operator%(const BigNumber& bn1, const BigNumber& bn2); //bn2!=0
	friend BigNumber operator%(const BigNumber& bn, long long n); //n!=0
	friend BigNumber operator%(long long n, const BigNumber& bn); //bn!=0
	friend BigNumber operator%(const BigNumber& bn, const string& str); //str!=0
	friend BigNumber operator%(const string& str, const BigNumber& bn); //bn!=0
	BigNumber& operator+=(const BigNumber& bn);
	BigNumber& operator+=(long long n);
	BigNumber& operator+=(const string& str);
	BigNumber& operator-=(const BigNumber& bn);
	BigNumber& operator-=(long long n);
	BigNumber& operator-=(const string& str);
	BigNumber& operator*=(const BigNumber& bn);
	BigNumber& operator*=(long long n);
	BigNumber& operator*=(const string& str);
	BigNumber& operator/=(const BigNumber& bn); //bn!=0
	BigNumber& operator/=(long long n); //n!=0
	BigNumber& operator/=(const string& str); //str!=0
	BigNumber& operator%=(const BigNumber& bn); //bn!=0
	BigNumber& operator%=(long long n); //n!=0
	BigNumber& operator%=(const string& str); //str!=0

	friend istream& operator>>(istream& in, BigNumber& bn);
	friend ostream& operator<<(ostream& out, const BigNumber& bn);

	/*METHODS*/
	string toString() const;
	BigNumber absolute() const;
	static BigNumber gcd(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber lcm(const BigNumber& bn1, const BigNumber& bn2);
	static BigNumber generateRandom(unsigned length); //non-negative only
	static BigNumber generateProbablePrime(unsigned minLength);
	BigNumber nextProbablePrime(); //this has >=2 digits

private:
	/*ATTRIBUTES*/
	const static unsigned base = 1000000000;
	static bool isRandomInitialized;
	deque <unsigned> digits; //little endian
	bool isZero;
	bool isNegative;

	/*METHODS*/
	void normalizeForm();
	static void initializeRandom();
	static bool isUnsignedGreater(const BigNumber& bn1, const BigNumber& bn2); //signs of bn1,bn2 are neglected
	static BigNumber unsignedSum(const BigNumber& bn1, const BigNumber& bn2); //signs of bn1,bn2 are neglected
	static BigNumber unsignedDifference(const BigNumber& bn1, const BigNumber& bn2); //signs of bn1,bn2 are neglected
	static BigNumber unsignedMultiply(const BigNumber& bn1, const BigNumber& bn2); //signs of bn1,bn2 are neglected
	static pair<BigNumber,BigNumber> unsignedDivide(const BigNumber& bn1, const BigNumber& bn2); //signs of bn1,bn2 are neglected; result = <quotient,remainder>; bn2!=0
	static BigNumber unsignedPowMod(const BigNumber& num, const BigNumber& pow, const BigNumber& mod); //signs of num,pow,mod are neglected; calculate (num**pow)%mod; mod!=0
};

#endif