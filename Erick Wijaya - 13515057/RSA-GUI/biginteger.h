#ifndef BIGINTEGER_H
#define BIGINTEGER_H

#include <iostream>
#include <vector>
#include <string>
using namespace std;

/**
 * A class that represents integer with unrestrained size.
 *
 * @author Erick Wijaya 
 * @version 1.0
 * @since 2017-13-07
 */

class biginteger {
public:
	/* Commonly used constants, to avoid recreation of instances */
	static const biginteger ZERO;
	static const biginteger ONE, TWO, THREE, FOUR, FIVE;
	static const biginteger SIX, SEVEN, EIGHT, NINE, TEN;

	/**
	 * Constructor that constructs new Big Integer with value 0.
	 */
	biginteger();

	/**
	 * Constructor.
	 * @param v The value for new Big Integer.
	 */
	biginteger(int v);

	/**
	 * Constructor.
	 * @param v The value for new Big Integer.
	 */
	biginteger(const string& v);

	/**
	 * Constructor.
	 * @param v Another Big Integer object.
	 */
	biginteger(const biginteger& v);

	/**
	 * Initializate this object with new value.
	 * @param rhs The value for initialization.
	 * @return this object with initialized value.
	 */
	biginteger& operator=(const biginteger& rhs);

	/**
	 * Initializate this object with new value.
	 * @param rhs The value for initialization.
	 * @return this object with initialized value.
	 */
	biginteger& operator=(int rhs);

	/**
	 * Get the digits as vector
	 * @return digits
	 */
	vector<int> getDigits();

	/**
	 * Get the pos value
	 * @return pos
	 */
	bool getPos();

	/** 
	 * Add this object with parameter.
	 * @param rhs The right-hand-side value for addition.
	 * @return this plus rhs.
	 */
	biginteger operator+(const biginteger& rhs) const;

	/** 
	 * Add this object with parameter without changing this value.
	 * @param rhs The right-hand-side value for addition.
	 * @return this plus rhs.
	 */
	biginteger operator+(int rhs) const;

	/**
	 * Negate sign from this object.
	 * @return This object with negated sign.
	 */
	biginteger operator-() const; // unary operator

	/** 
	 * Substract this object with parameter without changing this value.
	 * @param rhs The right-hand-side value for substraction.
	 * @return this minus rhs.
	 */
	biginteger operator-(const biginteger& rhs) const;

	/** 
	 * Substract this object with parameter without changing this value.
	 * @param rhs The right-hand-side value for substraction.
	 * @return this minus rhs.
	 */
	biginteger operator-(int rhs) const;

	/** 
	 * Multiply this object with parameter without changing this value.
	 * @param rhs The right-hand-side value for multiplication.
	 * @return this times rhs.
	 */
	biginteger operator*(const biginteger& rhs) const;

	/** 
	 * Multiply this object with parameter without changing this value.
	 * @param rhs The right-hand-side value for multiplication.
	 * @return this times rhs.
	 */
	biginteger operator*(int rhs) const;

	/** 
	 * Execute Integer Division with this object and parameter without changing this value.
	 * This object is the numerator.
	 * @param rhs The denominator.
	 * @return this divide rhs.
	 */
	biginteger operator/(const biginteger& rhs) const;

	/** 
	 * Execute Integer Division with this object and parameter without changing this value.
	 * This object is the numerator.
	 * @param rhs The denominator.
	 * @return this divide rhs.
	 */
	biginteger operator/(int rhs) const;

	/** 
	 * Return the remainder of Integer Division with this object and parameter without changing this value.
	 * This object is the numerator.
	 * @param rhs The denominator.
	 * @return this mod rhs.
	 */
	biginteger operator%(const biginteger& rhs) const;

	/** 
	 * Return the remainder of Integer Division with this object and parameter without changing this value.
	 * This object is the numerator.
	 * @param rhs The denominator.
	 * @return this mod rhs.
	 */
	biginteger operator%(int rhs) const;

	/**
	 * Add this object with rhs.
	 * @param rhs The value for addition.
	 * @return this object with updated value.
	 */
	biginteger& operator+=(const biginteger& rhs);

	/**
	 * Add this object with rhs.
	 * @param rhs The value for addition.
	 * @return this object with updated value.
	 */
	biginteger& operator+=(int rhs);

	/**
	 * Negate this object's sign.
	 */
	void negate();

	/**
	 * Substract this object with rhs.
	 * @param rhs The value for substraction.
	 * @return this object with updated value.
	 */
	biginteger& operator-=(const biginteger& rhs);

	/**
	 * Substract this object with rhs.
	 * @param rhs The value for substraction.
	 * @return this object with updated value.
	 */
	biginteger& operator-=(int rhs);

	/**
	 * Multiply this object with rhs.
	 * @param rhs The value for multiplication.
	 * @return this object with updated value.
	 */
	biginteger& operator*=(const biginteger& rhs);

	/**
	 * Multiply this object with rhs.
	 * @param rhs The value for multiplication.
	 * @return this object with updated value.
	 */
	biginteger& operator*=(int rhs);

	/**
	 * Divide this object with rhs.
	 * @param rhs The value for division.
	 * @return this object with updated value.
	 */
	biginteger& operator/=(const biginteger& rhs);

	/**
	 * Divide this object with rhs.
	 * @param rhs The denominator.
	 * @return this object with updated value.
	 */
	biginteger& operator/=(int rhs);

	/**
	 * Modulo this object with rhs.
	 * @param rhs The denominator.
	 * @return this object with updated value.
	 */
	biginteger& operator%=(const biginteger& rhs);

	/**
	 * Modulo this object with rhs.
	 * @param rhs The denominator.
	 * @return this object with updated value.
	 */
	biginteger& operator%=(int rhs);

	/**
	 * Add this object with one and return the value.
	 * @return this object with updated value.
	 */
	biginteger& operator++();

	/**
	 * Add this object with one and return the value before updated.
	 * @param d dummy.
	 * @return this object before updated.
	 */
	biginteger operator++(int d); // postfix

	/**
	 * Substract this object with one and return the value.
	 * @return this object with updated value.
	 */
	biginteger& operator--();

	/**
	 * Substract this object with one and return the value before updated.
	 * @param d dummy.
	 * @return this object before updated.
	 */
	biginteger operator--(int d); // postfix

	/**
	 * Calculate |this|.
	 * @return the positive value of this object.
	 */
	biginteger abs() const;

	/**
	 * Calculate a^b.
	 * @param a The base.
	 * @param n The exponent.
	 * @return a^b.
	 */
	static biginteger pow(const biginteger& a, const biginteger& n);

	/**
	 * Calculate a^b % m.
	 * @param a The base.
	 * @param n The exponent.
	 * @param m The modulo
	 * @return a^b % m.
	 */
	static biginteger modpow(const biginteger& a, const biginteger& n, const biginteger& m);

	/**
	 * Check if this object equals rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this == rhs.
	 */
	bool operator==(const biginteger& rhs) const;

	/**
	 * Check if this object equals rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this == rhs.
	 */
	bool operator==(int rhs) const;

	/**
	 * Check if this object is not equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this != rhs.
	 */
	bool operator!=(const biginteger& rhs) const;

	/**
	 * Check if this object is not equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this != rhs.
	 */
	bool operator!=(int rhs) const;

	/**
	 * Check if this object is greater than rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this > rhs.
	 */
	bool operator>(const biginteger& rhs) const;

	/**
	 * Check if this object is greater than rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this > rhs.
	 */
	bool operator>(int rhs) const;

	/**
	 * Check if this object is greater than or equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this >= rhs.
	 */
	bool operator>=(const biginteger& rhs) const;

	/**
	 * Check if this object is greater than or equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this >= rhs.
	 */
	bool operator>=(int rhs) const;

	/**
	 * Check if this object is lesser than rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this < rhs.
	 */
	bool operator<(const biginteger& rhs) const;

	/**
	 * Check if this object is lesser than rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this < rhs.
	 */
	bool operator<(int rhs) const;

	/**
	 * Check if this object is lesser than or equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this <= rhs.
	 */
	bool operator<=(const biginteger& rhs) const;

	/**
	 * Check if this object is lesser than or equal to rhs.
	 * @param rhs The right-hand-side value.
	 * @return TRUE if this <= rhs.
	 */
	bool operator<=(int rhs) const;

	/**
	 * Input Stream Operator.
	 * @param is Input stream.
	 * @param v The Big Integer in the input stream.
	 * @return input stream and update value of v.
	 */
	friend istream& operator>>(istream &is, biginteger& v);

	/**
	 * Output Stream Operator.
	 * @param is Output stream.
	 * @param v The Big Integer in the output stream.
	 * @return output stream and print v.
	 */
	friend ostream& operator<<(ostream &os, const biginteger& v);

	/**
	 * Get string representation of this object.
	 * @return string representation of this object.
	 */
	string toString();

	/**
	 * Get integer representation of this object.
	 * This object's value must within integer min and max value.
	 * @return integer representation of this object.
	 */
	int toInt();

	/**
	 * Check if this object is an odd number.
	 * return TRUE if this object is an odd number.
	 */
	bool isOdd() const;

	/**
	 * Check if this object is an even number.
	 * return TRUE if this object is an even number.
	 */
	bool isEven() const;

	/**
	 * Get the subdigit of this object.
	 * @param pos The starting digit.
	 * @param n The amount of digits from starting digit.
	 * @return Positive Big Integer with n digits starting from pos.
	 */
	biginteger subDigit(int pos, int n) const;

	/**
	 * Get the subdigit of this object.
	 * @param pos The starting digit.
	 * @return Positive Big Integer starting from pos until the last digit.
	 */
	biginteger subDigit(int pos) const;

	/**
	 * Calculate greatest common divisor with Euclidean Algorithm.
	 * @param a The first integer.
	 * @param b The second integer.
	 * @return gcd(a,b).
	 */
	static biginteger gcd(biginteger a, biginteger b);

	/**
	 * Calculate greatest common divisor and solve Bézout's Identity with Extended Euclidean Algorithm.
	 * The Bézout's Identity is: a*x + b*y = gcd(a,b).
	 * @param a The first integer.
	 * @param b The second integer.
	 * @param x The first coefficient.
	 * @param y The second coefficient.
	 * @return gcd(a,b), update x and y value.
	 */
	static biginteger gcdExtended(biginteger a, biginteger b, biginteger& x, biginteger& y);

	/**
	 * Generate probable prime number.
	 * @param The amount of digits.
	 * @return Big Integer that is probably prime.
	 */
	static biginteger generateRandomProbablePrime(int digits = DEFAULT_DIGITS);

	/**
	 * Generate random big integer.
	 * @param The amount of digits.
	 * @return new random Big Integer.
	 */
	static biginteger generateRandom(int digits = DEFAULT_DIGITS);

	/** 
	 * Check whether this object is probably prime or certainly composite.
	 * @param certainty The certainty factor that determines the accuracy.
	 * @return TRUE if this object is probably prime.
	 */
	bool isProbablePrime(int certainty = DEFAULT_CERTAINTY);

private:
	vector<int> digits; // reverse-ordered digits
	bool pos; // positive

	static const int BASE = 10;
	static const int DEFAULT_DIGITS = 20;
	static const int DEFAULT_CERTAINTY = 10;

	static int max(int a, int b);

	/**
	 * Make this object does not have leading zero and handles negative zero.
	 */
	void normalize();

	/**
	 * Calculate this * 10^n with n > 0.
	 * @param n The exponent.
	 * return this * 10^n.
	 */
	biginteger multiply10(int n);

	/**
	 * Update this value to this * 10^n with n > 0.
	 * @param n The exponent..
	 */
	void multiplyThis10(int n);

	/**
	 * Multiply single-lengthed Big Integers.
	 * @param lhs The first number.
	 * @param rhs The second number.
	 * @return lhs*rhs.
	 */
	static biginteger multiplySingleDigit(const biginteger& lhs, const biginteger& rhs);

	/**
	 * Performs Karatsuba Multiplication.
	 * @param lhs The first number.
	 * @param rhs The second number.
	 * @return lhs*rhs.
	 */
	static biginteger karatsubaMultiply(const biginteger& lhs, const biginteger& rhs);

	/**
	 * Calculate integer division and return both quotient and remainder.
	 * @param lhs The first number.
	 * @param rhs The second number.
	 * @return lhs/rhs and lhs%rhs.
	 */
	static pair<biginteger, biginteger> divmod(const biginteger& lhs, const biginteger& rhs);

	/**
	 * Generate an odd Big Integer that is not divisible by 5.
	 * @param digits The amound of digits.
	 * @return an odd Big Integer that is not divisible by 5.
	 */
	static biginteger generateRandomNearlyPrime(int digits);
};

#endif // BIGINTEGER_H
