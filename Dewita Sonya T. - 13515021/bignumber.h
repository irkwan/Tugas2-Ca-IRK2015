// File : bignumber.h
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include <vector>
#include <iostream>
using namespace std;

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

class BigNumber {
public:
	/* Constructor */
	BigNumber();
	BigNumber(string number);
	BigNumber(int number);
	BigNumber(const BigNumber& number);

	/* operator= */
	BigNumber& operator=(const BigNumber& number);

	/* Converter */
	long long ConvertToInt();
	char ConvertToChar();

	/* Input Output */
	friend ostream& operator<< (ostream& os, const BigNumber& number);
	friend void operator>> (istream& is, BigNumber& number);

	/* Binary Arithmetic Operators */
	BigNumber& operator+=(const BigNumber& number);
	BigNumber operator+(BigNumber number) const {number += *this; return number;}
	BigNumber& operator-=(const BigNumber& number);
	BigNumber operator-(BigNumber number) const {number -= *this; return number.Negate();}
	BigNumber& operator*=(BigNumber number); // Using Karatsuba Algorithm
	BigNumber operator*(BigNumber number) const {number *= *this; return number;}
	BigNumber& operator/=(const BigNumber& number);
	BigNumber operator/(const BigNumber& number) const {BigNumber temp(*this); temp /= number; return temp;}
	BigNumber& operator%=(const BigNumber& number);
	BigNumber operator%(const BigNumber& number) const {BigNumber temp(*this); temp %= number; return temp;}
	BigNumber& ModPow(BigNumber pow, const BigNumber& mod);
	BigNumber GCD(const BigNumber& b, BigNumber& inv); // Using Extended Euclidean Algorithm
	BigNumber& Divide(const BigNumber& number, BigNumber& mod);

	/* Unary Arithmetic Operator */
	BigNumber& operator++();
	BigNumber operator++(int) {BigNumber temp(*this); operator++(); return temp;}
	BigNumber& operator--();
	BigNumber operator--(int) {BigNumber temp(*this); operator--(); return temp;}

	/* Comparison Operator */
	bool operator==(const BigNumber& number) const;
	bool operator!=(const BigNumber& number) const {return !operator==(number);}
	bool operator<(const BigNumber& number) const;
	bool operator>(const BigNumber& number) const {return number.operator<(*this);}
	bool operator<=(const BigNumber& number) const {return !operator>(number);}
	bool operator>=(const BigNumber& number) const {return !operator<(number);}

	/* Shift Operator */
	BigNumber& operator<<(int len);
	BigNumber& operator>>(int len);

	/* Random */
	BigNumber Random() const; // Generate random number from 2, less than this
	BigNumber GenerateRandomPrime(int digit); //Using Fermat Primality Testing

	/* Other */
	void Abs() {negative = true;}
	int size() const {return num.size();}
	BigNumber Negate() const;
	void Swap(BigNumber& number);
	bool IsPositive() const {return (!negative && num[0] > 0);}

public:
	vector<int> num; //menyimpan angka, 0 <= num[i] <= 9.
	bool negative;

	BigNumber Truncate(int len);
};

#endif