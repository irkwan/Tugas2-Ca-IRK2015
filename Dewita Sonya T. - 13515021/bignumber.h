// File : bignumber.h
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include <bits\stdc++.h>
using namespace std;

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

class BigNumber {
public:
	/* Constructor */
	BigNumber();
	BigNumber(int number);
	BigNumber(string number);
	BigNumber(const BigNumber& number);

	/* operator= */
	BigNumber& operator=(const BigNumber& number);
	BigNumber& operator=(const int number);
	BigNumber& operator=(const string number);

	/* Input Output */
	friend ostream& operator<< (ostream& os, const BigNumber& number);
	friend istream& operator>> (istream& is, BigNumber& number);

	/* Binary Arithmetic Operators */
	BigNumber& operator+=(const BigNumber& number);
	BigNumber operator+(BigNumber number) const {number += *this; return number;}
	BigNumber& operator-=(const BigNumber& number);
	BigNumber operator-(BigNumber number) const {number -= *this; number.Negate(); return number;}
	BigNumber& operator*=(const BigNumber& number);
	BigNumber operator*(BigNumber number) const {number *= *this; return number;}
	BigNumber& operator%=(const BigNumber& number);
	BigNumber operator%(const BigNumber& number) const;
	BigNumber operator^(const BigNumber&);

	/* Unary Arithmetic Operator */
	BigNumber& operator++();
	BigNumber operator++(int) {BigNumber temp(*this); operator++(); return temp;}
	BigNumber operator--();
	BigNumber operator--(int) {BigNumber temp(*this); operator--(); return temp;}

	/* Comparison Operator */
	bool operator==(const BigNumber& number) const;
	bool operator!=(const BigNumber& number) const {return !operator==(number);}
	bool operator<(const BigNumber& number) const;
	bool operator>(const BigNumber& number) const {return number.operator<(*this);}
	bool operator<=(const BigNumber& number) const {return !operator>(number);}
	bool operator>=(const BigNumber& number) const {return !operator<(number);}

	/* Other */
	void Abs() {negative = true;}
	long long size() {return num.size();}

private:
	vector<int> num; //menyimpan angka, 0 <= num[i] <= 9.
	bool negative;

	void Negate();
	bool IsNegative();
};

#endif