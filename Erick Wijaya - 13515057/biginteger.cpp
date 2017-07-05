// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

const biginteger biginteger::ZERO = biginteger();
const biginteger biginteger::ONE = biginteger(1);
const biginteger biginteger::TWO = biginteger(2);
const biginteger biginteger::THREE = biginteger(3);
const biginteger biginteger::FOUR = biginteger(4);
const biginteger biginteger::FIVE = biginteger(5);
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
			//cout << v[i] << " " << dig << " " << endl;
			//cout << *this << endl;
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
		return *this - (-rhs);
	}
}

biginteger biginteger::operator-() const{
	biginteger res = *this;
	res.pos = !res.pos;
	return res;
}

biginteger biginteger::operator-(const biginteger& rhs) const{
	if (pos == rhs.pos){
		if (abs() >= rhs.abs()){ // this >= rhs
			biginteger res = *this;
			int carry = 0;

			for(int i=0; i<digits.size(); i++){
				res.digits[i] -= carry + rhs.digits[i];
				carry = (res.digits[i] < 0)? 1 : 0;
				if (carry)
					res.digits[i] += BASE;
			}
			res.delTrail0();

			return res;
		}
		else{ // this < rhs
			// this - rhs = -(rhs - this)
			return -(rhs - *this);
		}
	}
	else{
		return *this + (-rhs);
	}
}

biginteger biginteger::operator*(const biginteger& rhs) const{
	// Make sure both value have same size
	int diffsize = digits.size() - rhs.digits.size();
	if (diffsize < 0) diffsize *= -1;

	biginteger v1 = *this;
	biginteger v2 = rhs;

	if (v1.digits.size() < v2.digits.size()){
		for(int i=0; i<diffsize; i++)
			v1.digits.push_back(0);
	}
	else{
		for(int i=0; i<diffsize; i++)
			v2.digits.push_back(0);
	}

	// Calculate with Karatsuba Algorithm

}

biginteger biginteger::operator/(const biginteger& rhs) const{
	biginteger res = divmod(*this, rhs.toLLInt()).first;
	return res;
}

biginteger biginteger::operator%(const biginteger& rhs) const{
	biginteger res(divmod(*this, rhs.toLLInt()).second);
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
/*
biginteger biginteger::add(const biginteger& rhs) const{
	return *this + rhs;
}

biginteger biginteger::min(const biginteger& rhs) const{
	return *this - rhs;
}

biginteger biginteger::mul(const biginteger& rhs) const{

}

biginteger biginteger::div(const biginteger& rhs) const{

}

biginteger biginteger::mod(const biginteger& rhs) const{

}*/

biginteger biginteger::abs() const{
	biginteger res = *this;
	res.pos = true;
	return res;
}


/* Relational Operators */
bool biginteger::operator==(const biginteger& rhs) const{
	bool same = (digits.size() == rhs.digits.size()) && (pos == rhs.pos);
	int i=0;
	while (same && (i < digits.size())){
		same = digits[i] == rhs.digits[i];
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
istream& operator>>(istream &is, const biginteger& v){

}

ostream& operator<<(ostream &os, const biginteger& v){
	if (!v.pos)
		os << "-";
	for(int i=v.digits.size()-1; i>=0; i--)
		os << v.digits[i];
	return os;
}

int biginteger::max(int a, int b) const{
	return (a > b) ? a : b;
}

void biginteger::delTrail0(){
	while ((digits[digits.size()-1] == 0) && (digits.size() > 1)){
		digits.pop_back();
	}
	if ((digits.size() == 1) && (digits[0] == 0)){
		pos = true; // handle negative zero
	}
}

long long biginteger::toLLInt() const{
	long long res = 0;
	for(int i=digits.size()-1; i>=0; i--){
		res = (res * BASE) + digits[i];
	}
	if (!pos)
		res = -res;
	return res;
}

pair<biginteger, long long> biginteger::divmod(const biginteger& v, long long den){ // TODO: benerin kasus negatif
	long long remainder = 0;
	bool negativeDen = den < 0;
	biginteger res;

	if (den < 0)
		den = -den;

	for(int i=v.digits.size()-1; i>=0; i--){
		remainder = (remainder * BASE) + v.digits[i];
		res.digits.insert(res.digits.begin(), remainder / den);
		remainder %= den;
	}

	//cout << v.pos << " " << den << endl;

	if (!v.pos){
		res.pos = negativeDen;
	}
	else if (v.pos && negativeDen){
		res.pos = false;
	}

	res.delTrail0();

	return make_pair(res, remainder);

}
