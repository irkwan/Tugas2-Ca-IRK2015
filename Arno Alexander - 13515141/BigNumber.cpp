/*
Author : Arno Alexander
*/

#include "BigNumber.h"

bool BigNumber::isRandomInitialized = false;

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
			return BigNumber::isUnsignedGreater(bn1,bn2);
		} else if (sign1 < 0) {
			return BigNumber::isUnsignedGreater(bn2,bn1);
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
			return BigNumber::isUnsignedGreater(bn2,bn1);
		} else if (sign1 < 0) {
			return BigNumber::isUnsignedGreater(bn1,bn2);
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
		result = BigNumber::unsignedSum(bn1,bn2);
		result.isNegative = bn1.isNegative;
	} else {
		result = BigNumber::unsignedDifference(bn1,bn2);
		result.isNegative = BigNumber::isUnsignedGreater(bn1,bn2) == bn1.isNegative;
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
		result = BigNumber::unsignedSum(bn1,bn2);
		result.isNegative = bn1.isNegative;
	} else {
		result = BigNumber::unsignedDifference(bn1,bn2);
		result.isNegative = BigNumber::isUnsignedGreater(bn1,bn2) == bn1.isNegative;
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
	BigNumber result = BigNumber::unsignedMultiply(bn1,bn2);
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

BigNumber operator/(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result = BigNumber::unsignedDivide(bn1,bn2).first;
	result.isNegative = bn1.isNegative!=bn2.isNegative;
	result.normalizeForm();
	return result;
}

BigNumber operator/(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn/bn2;
}

BigNumber operator/(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1/bn;
}

BigNumber operator/(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn/bn2;
}

BigNumber operator/(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1/bn;
}

BigNumber operator%(const BigNumber& bn1, const BigNumber& bn2) {
	BigNumber result = BigNumber::unsignedDivide(bn1,bn2).second;
	result.isNegative = bn1.isNegative;
	result.normalizeForm();
	return result;
}

BigNumber operator%(const BigNumber& bn, long long n) {
	BigNumber bn2(n);
	return bn%bn2;
}

BigNumber operator%(long long n, const BigNumber& bn) {
	BigNumber bn1(n);
	return bn1%bn;
}

BigNumber operator%(const BigNumber& bn, const string& str) {
	BigNumber bn2(str);
	return bn%bn2;
}

BigNumber operator%(const string& str, const BigNumber& bn) {
	BigNumber bn1(str);
	return bn1%bn;
}

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

BigNumber& BigNumber::operator/=(const BigNumber& bn) {
	*this = *this / bn;
	return *this;
}

BigNumber& BigNumber::operator/=(long long n) {
	*this = *this / n;
	return *this;
}

BigNumber& BigNumber::operator/=(const string& str) {
	*this = *this / str;
	return *this;
}

BigNumber& BigNumber::operator%=(const BigNumber& bn) {
	*this = *this % bn;
	return *this;
}

BigNumber& BigNumber::operator%=(long long n) {
	*this = *this % n;
	return *this;
}

BigNumber& BigNumber::operator%=(const string& str) {
	*this = *this % str;
	return *this;
}

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

long long BigNumber::toLongLong() const {
	if (isZero) {
		return 0;
	} else {
		long long result = 0;
		for (deque<unsigned>::size_type i = 0; i < digits.size(); i++) {
			result = (long long)(base) * result + (long long)(digits[i]);
		}
		if (isNegative) result *= -1;
		return result;
	}
}

BigNumber BigNumber::absolute() const {
	BigNumber result = *this;
	result.isNegative = false;
	return result;
}

pair<BigNumber,BigNumber> BigNumber::divMod(const BigNumber& bn1, const BigNumber& bn2) {
	pair<BigNumber,BigNumber> result = unsignedDivide(bn1,bn2);
	result.first.isNegative = bn1.isNegative!=bn2.isNegative;
	result.first.normalizeForm();
	result.second.isNegative = bn1.isNegative;
	result.second.normalizeForm();
	return result;
}

BigNumber BigNumber::powMod(const BigNumber& num, const BigNumber& pow, const BigNumber& mod) {
	if (pow.isNegative) {
		return BigNumber(0);
	} else {
		BigNumber result = unsignedPowMod(num, pow, mod);
		result.isNegative = num.isNegative && pow.digits.front()%2==1;
		result.normalizeForm();
		return result;
	}
}

BigNumber BigNumber::gcd(const BigNumber& bn1, const BigNumber& bn2) {
	if (bn1 != 0 && bn2 != 0) {
		BigNumber zero(0), left(bn1), right(bn2), temp;
		do {
			temp = right;
			right = unsignedDivide(left,right).second;
			left = temp;
		} while (right != zero);
		return left;
	} else {
		return BigNumber(0);
	}
}

BigNumber BigNumber::lcm(const BigNumber& bn1, const BigNumber& bn2) {
	return unsignedDivide(unsignedMultiply(bn1,bn2),gcd(bn1,bn2)).first;
}

BigNumber BigNumber::generateRandom(unsigned length) {
	unsigned long long generatedDigitValue = 0, randomMultiplier = 1;
	unsigned generatedDigit, randomIdentifier;
	BigNumber result;
	result.digits.clear();
	initializeRandom();
	while (length > 0) {
		randomIdentifier = (rand() + length) % 4;
		switch (randomIdentifier) {
			case 0 : generatedDigit = (rand() * 3) % 10; break;
			case 1 : generatedDigit = (rand() * 9) % 10; break;
			case 2 : generatedDigit = (rand() * 7) % 10; break;
			default : generatedDigit = rand() % 10; break;
		}
		if (generatedDigit == 0 && length == 1) {
			generatedDigit = 1;
		}
		generatedDigitValue += randomMultiplier * generatedDigit;
		randomMultiplier *= 10;
		if (randomMultiplier >= base) {
			generatedDigit = (unsigned)(generatedDigitValue);
			result.digits.push_back(generatedDigit);
			generatedDigitValue = 0;
			randomMultiplier = 1;
		}
		length--;
	}
	generatedDigit = (unsigned)(generatedDigitValue % base);
	result.digits.push_back(generatedDigit);
	result.normalizeForm();
	return result;
}

BigNumber BigNumber::generateProbablePrime(unsigned minLength) {
	BigNumber result = generateRandom(minLength);
	return result.nextProbablePrime();
}

BigNumber BigNumber::nextProbablePrime() const {
	BigNumber result = *this;
 	if (result.digits.front() % 2 == 0) {
 		result -= 1;
 	}
 	BigNumber two(2), three(3), five(5), seven(7);
 	bool found = false;
 	do {
 		result += two;
 		if (result.digits.front() % 5 != 0) {
 			found = unsignedPowMod(two,result,result) == two;
 			if (found) {
 				found = unsignedPowMod(three,result,result) == three;
 				if (found) {
 					found = unsignedPowMod(five,result,result) == five;
 					if (found) {
 						found = unsignedPowMod(seven,result,result) == seven;
 					}
 				}
 			}
 		}
 	} while (!found);
 	return result;
}

bool BigNumber::isProbablePrime() const {
	bool result = true;
	if (digits.front() % 5 != 0) {
		result = unsignedPowMod(2,result,result) == 2;
		if (result) {
			result = unsignedPowMod(3,result,result) == 3;
			if (result) {
				result = unsignedPowMod(5,result,result) == 5;
				if (result) {
					result = unsignedPowMod(7,result,result) == 7;
				}
			}
		}
	}
	return result;
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

void BigNumber::initializeRandom() {
	if (!isRandomInitialized) {
		srand(time(NULL));
		isRandomInitialized = true;
	}
}

bool BigNumber::isUnsignedGreater(const BigNumber& bn1, const BigNumber& bn2) {
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

BigNumber BigNumber::unsignedSum(const BigNumber& bn1, const BigNumber& bn2) {
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

BigNumber BigNumber::unsignedDifference(const BigNumber& bn1, const BigNumber& bn2) {
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

BigNumber BigNumber::unsignedMultiply(const BigNumber& bn1, const BigNumber& bn2) {
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

pair<BigNumber,BigNumber> BigNumber::unsignedDivide(const BigNumber& bn1, const BigNumber& bn2) {
	if (isUnsignedGreater(bn2,bn1)) {
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

BigNumber BigNumber::unsignedPowMod(const BigNumber& num, const BigNumber& pow, const BigNumber& mod) {
	if (pow.isZero) {
		if (isUnsignedGreater(mod,1)) {
			return BigNumber(1);
		} else {
			return BigNumber(0);
		}
	} else {
		if (num.isZero) {
			return BigNumber(0);
		} else {
			pair<BigNumber,BigNumber> halfpow = unsignedDivide(pow,2);
			BigNumber result = unsignedPowMod(num, halfpow.first, mod);
			result = unsignedMultiply(result,result);
			if (!halfpow.second.isZero) {
				result = unsignedMultiply(result,num);
			}
			return unsignedDivide(result,mod).second;
		}
	}
}