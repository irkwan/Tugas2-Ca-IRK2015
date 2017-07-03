/*
Author : Arno Alexander
*/

#include <iostream>
#include <deque>
#include <cmath>
#include <utility>
#include "BigNumber.h"

BigNumber::BigNumber() {
	digits.clear();
	digits.push_back(0);
	isZero = true;
	isNegative = false;
}

BigNumber::BigNumber(long long n) {
	digits.clear();
	isZero = n==0;
	isNegative = n<0;
	if (isZero) {
		digits.push_back(0);
	} else {
		n = abs(n);
		while (n>0) {
			digits.push_back(n%base);
			n /= base;
		}
	}
}

BigNumber::BigNumber(const string& str) {
	digits.clear();
	isNegative = str[0]=='-';
	unsigned minIdx;
	isNegative ? minIdx = 1 : minIdx = 0;
	unsigned factor = 1;
	unsigned digitComponent = 0; 
	for (size_t i = str.length(); i>minIdx; i--) {
		digitComponent += factor * (str[i-1] - 48);
		factor *= 10;
		if (factor >= base || i == minIdx+1) {
			digits.push_back(digitComponent);
			factor = 1;
			digitComponent = 0;
		} 
	}
	normalizeForm();
}

BigNumber::BigNumber(const BigNumber& bn) {
	digits = bn.digits;
	isZero = bn.isZero;
	isNegative = bn.isNegative;
}

BigNumber& BigNumber::operator=(long long n) {
	digits.clear();
	isZero = n==0;
	isNegative = n<0;
	if (isZero) {
		digits.push_back(0);
	} else {
		n = abs(n);
		while (n>0) {
			digits.push_back(n%base);
			n /= base;
		}
	}
	return *this;
}

BigNumber& BigNumber::operator=(const string& str) {
	digits.clear();
	isNegative = str[0]=='-';
	unsigned minIdx;
	isNegative ? minIdx = 1 : minIdx = 0;
	unsigned factor = 1;
	unsigned digitComponent = 0; 
	for (size_t i = str.length(); i>minIdx; i--) {
		digitComponent += factor * (str[i-1] - 48);
		factor *= 10;
		if (factor >= base || i == minIdx+1) {
			digits.push_back(digitComponent);
			factor = 1;
			digitComponent = 0;
		} 
	}
	normalizeForm();
	return *this;
}

BigNumber& BigNumber::operator=(const BigNumber& bn) {
	digits = bn.digits;
	isZero = bn.isZero;
	isNegative = bn.isNegative;
	return *this;
}

bool operator==(const BigNumber& bn1, const BigNumber& bn2) {
	if (bn1.isZero!=bn2.isZero || bn1.isNegative!=bn2.isNegative) {
		return false;
	} else {
		return bn1.digits==bn2.digits;
	}
}

bool operator==(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn==bn2;
}

bool operator==(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1==bn;
}

bool operator==(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn==bn2;
}

bool operator==(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1==bn;
}

bool operator!=(const BigNumber& bn1, const BigNumber& bn2) {
	return !(bn1==bn2);
}

bool operator!=(const BigNumber& bn, long long n) {
	return !(bn==n);
}

bool operator!=(long long n, const BigNumber& bn) {
	return !(n==bn);
}

bool operator!=(const BigNumber& bn, const string& str) {
	return !(bn==str);
}

bool operator!=(const string& str, const BigNumber& bn) {
	return !(str==bn);
}

bool operator>(const BigNumber& bn1, const BigNumber& bn2) {
	int sign1 = 1 - bn1.isNegative * 2 - bn1.isZero;
	int sign2 = 1 - bn2.isNegative * 2 - bn2.isZero;
	if (sign1 == sign2) {
		if (sign1 > 0) {
			return isUnsignedGreater(bn1,bn2);
		} else if (sign1 < 0) {
			return isUnsignedGreater(bn2,bn1);
		} else {
			return false;
		}
	} else {
		return sign1>sign2;
	}
}

bool operator>(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn>bn2;
}

bool operator>(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1>bn;
}

bool operator>(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn>bn2;
}

bool operator>(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1>bn;
}

bool operator>=(const BigNumber& bn1, const BigNumber& bn2) {
	return !(bn1<bn2);
}

bool operator>=(const BigNumber& bn, long long n) {
	return !(bn<n);
}

bool operator>=(long long n, const BigNumber& bn) {
	return !(n<bn);
}

bool operator>=(const BigNumber& bn, const string& str) {
	return !(bn<str);
}

bool operator>=(const string& str, const BigNumber& bn) {
	return !(str<bn);
}

bool operator<(const BigNumber& bn1, const BigNumber& bn2) {
	int sign1 = 1 - bn1.isNegative * 2 - bn1.isZero;
	int sign2 = 1 - bn2.isNegative * 2 - bn2.isZero;
	if (sign1 == sign2) {
		if (sign1 > 0) {
			return isUnsignedGreater(bn2,bn1);
		} else if (sign1 < 0) {
			return isUnsignedGreater(bn1,bn2);
		} else {
			return false;
		}
	} else {
		return sign1<sign2;
	}
}

bool operator<(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn<bn2;
}

bool operator<(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1<bn;
}

bool operator<(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn<bn2;
}

bool operator<(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1<bn;
}

bool operator<=(const BigNumber& bn1, const BigNumber& bn2) {
	return !(bn1>bn2);
}

bool operator<=(const BigNumber& bn, long long n) {
	return !(bn>n);
}

bool operator<=(long long n, const BigNumber& bn) {
	return !(n>bn);
}

bool operator<=(const BigNumber& bn, const string& str) {
	return !(bn>str);
}

bool operator<=(const string& str, const BigNumber& bn) {
	return !(str>bn);
}

BigNumber operator+(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result;
	if (bn1.isNegative == bn2.isNegative) {
		result = unsignedSum(bn1,bn2);
		result.isNegative = bn1.isNegative;
	} else {
		result = unsignedDifference(bn1,bn2);
		result.isNegative = isUnsignedGreater(bn1,bn2) == bn1.isNegative;
	}
	result.normalizeForm();
	return result;
}

BigNumber operator+(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn+bn2;
}

BigNumber operator+(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1+bn;
}

BigNumber operator+(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn+bn2;
}

BigNumber operator+(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1+bn;
}

BigNumber operator-(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result;
	if (bn1.isNegative != bn2.isNegative) {
		result = unsignedSum(bn1,bn2);
		result.isNegative = bn1.isNegative;
	} else {
		result = unsignedDifference(bn1,bn2);
		result.isNegative = isUnsignedGreater(bn1,bn2) == bn1.isNegative;
	}
	result.normalizeForm();
	return result;
}

BigNumber operator-(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn-bn2;
}

BigNumber operator-(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1-bn;
}

BigNumber operator-(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn-bn2;
}

BigNumber operator-(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1-bn;
}

BigNumber operator*(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result = unsignedMultiply(bn1,bn2);
	result.isNegative = bn1.isNegative!=bn2.isNegative;
	result.normalizeForm();
	return result;
}

BigNumber operator*(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn*bn2;
}

BigNumber operator*(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1*bn;
}

BigNumber operator*(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn*bn2;
}

BigNumber operator*(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1*bn;
}

//
//TODO: normal arithmetic
//

BigNumber& BigNumber::operator+=(const BigNumber& bn) {
	*this = *this + bn;
	return *this;
}

BigNumber& BigNumber::operator+=(long long n) {
	*this = *this + n;
	return *this;
}

BigNumber& BigNumber::operator+=(const string& str) {
	*this = *this + str;
	return *this;
}

BigNumber& BigNumber::operator-=(const BigNumber& bn) {
	*this = *this - bn;
	return *this;
}

BigNumber& BigNumber::operator-=(long long n) {
	*this = *this - n;
	return *this;
}

BigNumber& BigNumber::operator-=(const string& str) {
	*this = *this - str;
	return *this;
}

BigNumber& BigNumber::operator*=(const BigNumber& bn) {
	*this = *this * bn;
	return *this;
}

BigNumber& BigNumber::operator*=(long long n) {
	*this = *this * n;
	return *this;
}

BigNumber& BigNumber::operator*=(const string& str) {
	*this = *this * str;
	return *this;
}

//
//TODO: self arithmetic
//

istream& operator>>(istream& in, BigNumber& bn) {
	string str;
	in >> str;
	bn = str;
	return in;
}

ostream& operator<<(ostream& out, const BigNumber& bn) {
	out << bn.toString();
	return out;
}

string BigNumber::toString() const {
	if (isZero) {
		return "0";
	} else {
		deque <char> stringBuffer;
		bool isPrinted = false;
		if (isNegative) {
			stringBuffer.push_back('-');
		}
		for (deque<unsigned>::size_type i = digits.size(); i>0; i--) {
			unsigned digitComponent = digits[i-1];
			unsigned tempBase = base / 10;
			while (tempBase > 0) {
				char digitChar = (char) (digitComponent / tempBase + 48);
				if (!isPrinted) {
					isPrinted = digitChar!='0' || (i==1 && tempBase==1);
				}
				if (isPrinted) {
					stringBuffer.push_back(digitChar);
				}
				digitComponent %= tempBase;
				tempBase /= 10;
			}
		}
		return string(stringBuffer.begin(), stringBuffer.end());
	}
}

void BigNumber::negate() {
	if (!isZero) {
		isNegative = !isNegative;
	}
}

void BigNumber::normalizeForm() {
	if (digits.size() == 0) {
		digits.push_back(0);
		isZero = true;
		isNegative = false;
	} else {
		bool zeroesInMostSignificant = digits.back()==0;
		while (digits.size()>1 && zeroesInMostSignificant) {
			digits.pop_back();
			zeroesInMostSignificant = digits.back()==0;
		}
		isZero = digits.size()==1 && digits.back()==0;
		isNegative = isNegative && !isZero;
	}
}

bool isUnsignedGreater(const BigNumber& bn1, const BigNumber& bn2) {
	if (bn1.digits.size() == bn2.digits.size()) {
		bool stopComparing = false;
		deque<unsigned>::size_type index = bn1.digits.size();
		while (index>0 && !stopComparing) {
			index--;
			stopComparing = bn1.digits[index] != bn2.digits[index];
		}
		return bn1.digits[index] > bn2.digits[index];
	} else {
		return bn1.digits.size() > bn2.digits.size();
	}
}

BigNumber unsignedSum(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result;
	unsigned carry = 0;
	deque<unsigned>::size_type index = 0;
	deque<unsigned>::size_type maxIndex = (bn1.digits.size()>bn2.digits.size() ? bn1.digits.size()-1 : bn2.digits.size()-1);
	result.digits.clear();
	while (carry>0 || index<=maxIndex) {
		carry += (index<bn1.digits.size() ? bn1.digits[index] : 0);
		carry += (index<bn2.digits.size() ? bn2.digits[index] : 0);
		result.digits.push_back(carry % BigNumber::base);
		carry /= BigNumber::base;
		index++;
	}
	result.normalizeForm();
	return result;
}

BigNumber unsignedDifference(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result;
	bool needCarry = false, nextNeedCarry, isBn1Bigger = isUnsignedGreater(bn1,bn2);
	unsigned resultComponent, subtractorComponent;
	deque<unsigned>::size_type index = 0;
	deque<unsigned>::size_type maxIndex = (isBn1Bigger ? bn1.digits.size()-1 : bn2.digits.size()-1);
	result.digits.clear();
	while (index<=maxIndex) {
		if (isBn1Bigger) {
			resultComponent = bn1.digits[index];
			subtractorComponent = (index<bn2.digits.size() ? bn2.digits[index] : 0);
		} else {
			resultComponent = bn2.digits[index];
			subtractorComponent = (index<bn1.digits.size() ? bn1.digits[index] : 0);
		}
		nextNeedCarry = resultComponent<subtractorComponent+needCarry;
		resultComponent += (nextNeedCarry ? BigNumber::base : 0);
		resultComponent -= subtractorComponent+needCarry;
		result.digits.push_back(resultComponent);
		index++;
		needCarry = nextNeedCarry;
	}
	result.normalizeForm();
	return result;
}

BigNumber unsignedMultiply(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result;
	unsigned long long carry;
	deque<unsigned>::size_type additionalIndex;
	result.digits.resize(bn1.digits.size()+bn2.digits.size());
	for (deque<unsigned>::size_type i=0; i<bn1.digits.size(); i++) {
		for (deque<unsigned>::size_type j=0; j<bn2.digits.size(); j++) {
			carry = (unsigned long long)(bn1.digits[i])*(unsigned long long)(bn2.digits[j]);
			additionalIndex = 0;
			do {
				carry += result.digits[i+j+additionalIndex];
				result.digits[i+j+additionalIndex] = carry % BigNumber::base;
				carry /= BigNumber::base;
				additionalIndex++;
			} while (carry>0);
		}
	}
	result.normalizeForm();
	return result;
}

pair<BigNumber,BigNumber> unsignedDivide(const BigNumber& bn1, const BigNumber& bn2) {
	if (bn2==0) {
		throw "division by 0";
	} else if (isUnsignedGreater(bn2,bn1)) {
		return pair<BigNumber,BigNumber>(0,bn1);
	} else {
		unsigned long long mostSignificantDigitValue;
		unsigned quotientDigitCandidate, quotientDigitStep;
		bool stopOperation = false, isSubtractorGreaterRemainder, isSubtractorRemainderDifferenceValid;
		BigNumber quotient, remainder, quotientDigit, subtractor, bigBase(BigNumber::base), subtractorRemainderDifference;
		deque<unsigned>::size_type nextDigitIndex = bn1.digits.size();
		remainder.digits.clear();
		do {
			nextDigitIndex--;
			remainder.digits.push_front(bn1.digits[nextDigitIndex]);
		} while (remainder.digits.size()<bn2.digits.size());
		do {
			if (remainder.digits.size()==bn2.digits.size()) {
				mostSignificantDigitValue = (unsigned long long)(remainder.digits.back());
			} else {
				mostSignificantDigitValue = (unsigned long long)(BigNumber::base) * (unsigned long long)(remainder.digits.back()) + (unsigned long long)(remainder.digits[remainder.digits.size()-2]);
			}
			quotientDigitCandidate = (unsigned)(mostSignificantDigitValue/(unsigned long long)(bn2.digits.back()));
			quotientDigitStep = quotientDigitCandidate - (unsigned)(mostSignificantDigitValue/(unsigned long long)(bn2.digits.back()+1));
			do {
				quotientDigit.digits.clear();
				quotientDigit.digits.push_back(quotientDigitCandidate);
				subtractor = unsignedMultiply(bn2,quotientDigit);
				subtractorRemainderDifference = unsignedDifference(remainder,subtractor);
				isSubtractorGreaterRemainder = isUnsignedGreater(subtractor,remainder);
				isSubtractorRemainderDifferenceValid = isUnsignedGreater(bn2,subtractorRemainderDifference);
				if (isSubtractorGreaterRemainder) {
					quotientDigitCandidate -= quotientDigitStep;
				} else if (!isSubtractorRemainderDifferenceValid) {
					quotientDigitCandidate += quotientDigitStep;
				}
				quotientDigitStep = (quotientDigitStep + 1) / 2;
			} while (isSubtractorGreaterRemainder || !isSubtractorRemainderDifferenceValid);
			quotientDigit.digits.clear();
			quotientDigit.digits.push_back(quotientDigitCandidate);
			remainder = unsignedDifference(remainder,subtractor);
			quotient = unsignedSum(quotientDigit,unsignedMultiply(quotient,bigBase));
			stopOperation = nextDigitIndex==0;
			if (!stopOperation) {
				nextDigitIndex--;
				remainder.digits.push_front(bn1.digits[nextDigitIndex]);
			}
		} while (!stopOperation);
		quotient.normalizeForm();
		remainder.normalizeForm();
		return pair<BigNumber,BigNumber>(quotient,remainder);
	}
}