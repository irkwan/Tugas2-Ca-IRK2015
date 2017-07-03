/*
Author : Arno Alexander
*/

#ifndef BIGNUMBER_H
#define BIGNUMBER_H

using namespace std;

class BigNumber {
public:
	/*Constructor & Destructor*/
	BigNumber();
	BigNumber(long long n);
	BigNumber(const string& str);
	BigNumber(const BigNumber& bn);

	/*Operators*/
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

	/*Methods*/
	string toString() const;
	void negate();

private:
	/*Attributes*/
	const static unsigned base = 1000000000;
	const static unsigned maxDigitsComponentLength = 9;
	deque <unsigned> digits; //little endian
	bool isZero;
	bool isNegative;

	/*Methods*/
	void normalizeForm();
	friend bool isUnsignedGreater(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber unsignedSum(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber unsignedDifference(const BigNumber& bn1, const BigNumber& bn2);
	friend BigNumber unsignedMultiply(const BigNumber& bn1, const BigNumber& bn2);
	friend pair<BigNumber,BigNumber> unsignedDivide(const BigNumber& bn1, const BigNumber& bn2);
};

#endif