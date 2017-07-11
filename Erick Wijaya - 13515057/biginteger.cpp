// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.cpp

#include "biginteger.h"
#include <iostream>
#include <deque>
#include <string>
using namespace std;

/* Commonly Used Constants */
const biginteger biginteger::ZERO = biginteger();
const biginteger biginteger::ONE = biginteger(1);
const biginteger biginteger::TWO = biginteger(2);
const biginteger biginteger::THREE = biginteger(3);
const biginteger biginteger::FOUR = biginteger(4);
const biginteger biginteger::FIVE = biginteger(5);
const biginteger biginteger::SIX = biginteger(6);
const biginteger biginteger::SEVEN = biginteger(7);
const biginteger biginteger::EIGHT = biginteger(8);
const biginteger biginteger::NINE = biginteger(9);
const biginteger biginteger::TEN = biginteger(10);

/* Constructors */
biginteger::biginteger(){
	pos = true;
	digits.push_back(0);
}

biginteger::biginteger(long long v){
	pos = v >= 0;
	if (!pos)
		v = -v;
	while (v != 0){
		int dig = v % BASE;
		digits.push_back(dig);
		v /= BASE;
	}
	if (digits.size() == 0)
		digits.push_back(0);
}

biginteger::biginteger(const string& v){
	if (v.length() == 0){
		pos = true;
		digits.push_back(0);
	}
	else{
		pos = v[0] != '-';
		int end = pos ? 0 : 1;
		for(int i=v.length()-1; i>=end; i--){
			int dig = v[i] - '0';
			digits.push_back(dig);
		}
	}
}

biginteger::biginteger(const biginteger& v) : pos(v.pos), digits(v.digits){

}


/* Operator= */
biginteger& biginteger::operator=(const biginteger& rhs){
	if (this != &rhs){
		pos = rhs.pos;
		digits = rhs.digits;
	}
	return *this;
}


/* Arithmetic Operators */
biginteger biginteger::operator+(const biginteger& rhs) const{
	if (pos == rhs.pos){
		int end = max(rhs.digits.size(), digits.size());
		int carry = 0;
		biginteger res = rhs;

		for(int i=0; i<end; i++){
			if (i == res.digits.size())
				res.digits.push_back(0);

			res.digits[i] += carry + (i < digits.size() ? digits[i] : 0);
			carry = res.digits[i] / BASE;
			if (carry)
				res.digits[i] -= BASE;
		}
		if (carry)
			res.digits.push_back(carry);

		return res;
	}
	else{
		if (rhs == biginteger::ZERO)
			return *this;
		return *this - (-rhs);
	}
}

biginteger biginteger::operator-() const{
	biginteger res = *this;
	if (res != biginteger::ZERO)
		res.pos = !res.pos;
	return res;
}

biginteger biginteger::operator-(const biginteger& rhs) const{
	if (pos == rhs.pos){
		if (abs() >= rhs.abs()){ // this >= rhs
			biginteger res = *this;
			int carry = 0;

			for(int i=0; i<rhs.digits.size() || carry; i++){
				res.digits[i] -= carry;
				if (i < rhs.digits.size())
					res.digits[i] -= rhs.digits[i];
				carry = (res.digits[i] < 0)? 1 : 0;
				if (carry)
					res.digits[i] += BASE;
			}

			res.normalize();

			return res;
		}
		else{ // this < rhs
			// this - rhs = -(rhs - this)
			return -(rhs - *this);
		}
	}
	else{
		if (rhs == biginteger::ZERO)
			return *this;
		return *this + (-rhs);
	}
}

biginteger biginteger::operator*(const biginteger& rhs) const{
	biginteger res = karatsubaMultiply(*this, rhs);
	res.pos = pos == rhs.pos;
	res.normalize();
	return res;
}

biginteger biginteger::operator/(const biginteger& rhs) const{
	biginteger res = divmod(*this, rhs).first;
	return res;
}

biginteger biginteger::operator%(const biginteger& rhs) const{
	biginteger res = divmod(*this, rhs).second;
	return res;
}

biginteger& biginteger::operator+=(const biginteger& rhs){
	*this = *this + rhs;
	return *this;
}

biginteger& biginteger::operator-=(const biginteger& rhs){
	*this = *this - rhs;
	return *this;
}

biginteger& biginteger::operator*=(const biginteger& rhs){
	*this = *this * rhs;
	return *this;
}

biginteger& biginteger::operator/=(const biginteger& rhs){
	*this = *this / rhs;
	return *this;
}

biginteger& biginteger::operator%=(const biginteger& rhs){
	*this = *this % rhs;
	return *this;
}

biginteger biginteger::abs() const{
	biginteger res = *this;
	res.pos = true;
	return res;
}

biginteger biginteger::pow(const biginteger& a, const biginteger& n){
	if (n == biginteger::ZERO)
		return biginteger::ONE;
	else if (n == biginteger::ONE)
		return a;
	else{
		biginteger a2 = pow(a, n / biginteger::TWO);
		if (n.isOdd())
			return a2 * a2 * a;
		else
			return a2 * a2;
	}
}

biginteger biginteger::modpow(const biginteger& a, const biginteger& n, const biginteger& m){
    biginteger base = a;
    biginteger exp = n;
    biginteger res = biginteger::ONE;
    while (exp > biginteger::ZERO)
    {
        if (exp.isOdd())
            res = (res * base) % m;
        exp /= biginteger::TWO;
        base = (base * base) % m;
    }
    return res;
}

/* Relational Operators */
bool biginteger::operator==(const biginteger& rhs) const{
	bool same = (digits.size() == rhs.digits.size()) && (pos == rhs.pos);
	//cout << digits.size() << " " << rhs.digits.size() << endl;
	//cout << same << endl;
	int i=0;
	while (same && (i < digits.size())){
		same = (digits[i] == rhs.digits[i]);
		i++;
	}
	return same;
}

bool biginteger::operator!=(const biginteger& rhs) const{
	return !(*this == rhs);
}

bool biginteger::operator>(const biginteger& rhs) const{
	if (pos != rhs.pos)
		return pos == true;
	else if (digits.size() != rhs.digits.size())
		return pos ? digits.size() > rhs.digits.size() : digits.size() < rhs.digits.size();
	else{
		for(int i=digits.size()-1; i>=0; i--){
			if (digits[i] != rhs.digits[i])
				return pos ? digits[i] > rhs.digits[i] : digits[i] < rhs.digits[i];
		}
		return false;
	}
}

bool biginteger::operator>=(const biginteger& rhs) const{
	return !(*this < rhs);
}

bool biginteger::operator<(const biginteger& rhs) const{
	return (*this != rhs) && !(*this > rhs);
}

bool biginteger::operator<=(const biginteger& rhs) const{
	return !(*this > rhs);
}


/* I/O */
istream& operator>>(istream &is, biginteger& v){
	string input;
	is >> input;

	v = biginteger(input);

	return is;
}

ostream& operator<<(ostream &os, const biginteger& v){
	if (!v.pos)
		os << "-";
	for(int i=v.digits.size()-1; i>=0; i--)
		os << v.digits[i];
	return os;
}

bool biginteger::isOdd() const{
	return digits[0] % 2 == 1;
}

bool biginteger::isEven() const{
	return !isOdd();
}

biginteger biginteger::subDigit(int pos, int n) const{
	biginteger res;
	int index = digits.size() - pos - 1;
	
	res.digits[0] += digits[index];
	for(int i=index-1; i>index-n; i--){
		res.digits.push_front(digits[i]);
	}
	return res;
}

biginteger biginteger::subDigit(int pos) const{
	biginteger res;
	int index = digits.size() - pos - 1;
	
	res.digits[0] += digits[index];
	for(int i=index-1; i>=0; i--){
		res.digits.push_front(digits[i]);
	}
	return res;
}

biginteger biginteger::gcd(biginteger a, biginteger b){
	if (a == biginteger::ZERO)
		return b;
	else
		return biginteger::gcd(b%a, a);
}

biginteger biginteger::gcdExtended(biginteger a, biginteger b, biginteger& x, biginteger& y){
    if (a == biginteger::ZERO){
        x = biginteger::ZERO;
        y = biginteger::ONE;
        return b;
    }
 	
    biginteger x1, y1; // To store results of recursive call
    biginteger gcd = biginteger::gcdExtended(b%a, a, x1, y1);
    // Update x and y using results of recursive call
    /*out << y1 << endl;
    cout << (b/a) << endl;
    cout << x1 << endl;
    cout << (b/a) * x1 << endl;
    cout << y1 - (b/a) * x1 << endl;*/
    x = y1 - (b/a) * x1;
    y = x1;

    return gcd;
}

int biginteger::max(int a, int b){
	return (a > b) ? a : b;
}

void biginteger::normalize(){
	while ((digits[digits.size()-1] == 0) && (digits.size() > 1)){
		digits.pop_back();
	}
	if ((digits.size() == 1) && (digits[0] == 0)){
		pos = true; // handle negative zero
	}
}

biginteger biginteger::multiply10(int n){
	biginteger res = *this;
	for(int i=0; i<n; i++)
		res.digits.push_front(0);
	return res;
}

biginteger biginteger::multiplySingleDigit(const biginteger& lhs, const biginteger& rhs){
	int num = lhs.digits[0] * rhs.digits[0];
	return biginteger(num);
}

biginteger biginteger::karatsubaMultiply(const biginteger& lhs, const biginteger& rhs){
	if ((lhs.digits.size() < 2) && (rhs.digits.size() < 2)){
		return multiplySingleDigit(lhs, rhs);
	}
	else{
		int m1 = max(lhs.digits.size(), rhs.digits.size());
		if (m1 % 2 == 1)
			m1++;
		int m2 = m1 / 2;

		biginteger left = lhs;
		biginteger right = rhs;

		while (left.digits.size() < m1)
			left.digits.push_back(0);
		while (right.digits.size() < m1)
			right.digits.push_back(0);

		biginteger h1 = left.subDigit(0,m2);
		biginteger l1 = left.subDigit(m2);
		biginteger h2 = right.subDigit(0,m2);
		biginteger l2 = right.subDigit(m2);

//		cout << l1 << "," << h1 << "  " << l2 << "," << h2 << endl;
		
		biginteger x0 = karatsubaMultiply(l1, l2);
		biginteger x1 = karatsubaMultiply(l1+h1, l2+h2);
//		cout << "tes1" << endl;
		biginteger x2 = karatsubaMultiply(h1, h2);
//		cout << "tes2" << endl;

		x0.normalize(); x1.normalize(); x2.normalize();
		return x2.multiply10(2*m2) + (x1-x2-x0).multiply10(m2) + x0;
	}
}

pair<biginteger, biginteger> biginteger::divmod(const biginteger& lhs, const biginteger& rhs){ // TODO: benerin kasus mod negatif (klo sempet aja krn ga ngaruh rsa)
	biginteger den(rhs); // denominator
	biginteger res;
	biginteger remainder;
	bool negativeDen = den < 0;

	if (lhs < den){
		return make_pair(biginteger::ZERO, lhs);
	}

	if (den < 0)
		den = -den;

	remainder = lhs.subDigit(0, den.digits.size());
	for(int i=lhs.digits.size() - den.digits.size(); i>=0; /* No Decrement */){
//		cout << remainder << " " << i << " " << res << endl; cin.get();
		if (remainder < den){
			if (i > 0){
				res.digits.push_front(0);
				remainder.digits.push_front(lhs.digits[i-1]);
				remainder.normalize(); // needed to avoid bug
			}
			i--;
		}
		else{
			int count = 0;
			while (remainder >= den){
				remainder -= den;
				count++;
			}
			res.digits[0] = count;
		}
	}

	res.normalize();
	remainder.normalize();

	res.pos = lhs.pos == rhs.pos;

	return make_pair(res, remainder);
}

