// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

/* Constructors */
biginteger::biginteger() {
	pos = true;
	digits.push_back(0);
}

biginteger::biginteger(long long v) {
	pos = v >= 0;
	if (!pos)
		v = -v;
	while (v != 0){
		int dig = v % BASE;
		digits.push_back(dig);
		v /= BASE;
	}
}

biginteger::biginteger(const string& v) {
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

biginteger::biginteger(const biginteger& v) : pos(v.pos), digits(v.digits) {

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
biginteger biginteger::operator+(const biginteger& rhs){
	int end = max(rhs.digits.size(), digits.size());
	int carry = 0;
	biginteger res = rhs;

	if (pos == res.pos){
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
		return biginteger(0);
	}
}

biginteger biginteger::operator-(){
	biginteger res = *this;
	res.pos = !res.pos;
	return res;
}

biginteger biginteger::operator-(const biginteger& rhs){

}

biginteger biginteger::operator*(const biginteger& rhs){

}

biginteger biginteger::operator/(const biginteger& rhs){

}

biginteger biginteger::operator%(const biginteger& rhs){

}

biginteger& biginteger::operator+=(const biginteger& rhs){

}

biginteger& biginteger::operator-=(const biginteger& rhs){

}

biginteger& biginteger::operator*=(const biginteger& rhs){

}

biginteger& biginteger::operator/=(const biginteger& rhs){

}

biginteger& biginteger::operator%=(const biginteger& rhs){

}

biginteger biginteger::add(const biginteger& rhs){

}

biginteger biginteger::min(const biginteger& rhs){

}

biginteger biginteger::mul(const biginteger& rhs){

}

biginteger biginteger::div(const biginteger& rhs){

}

biginteger biginteger::mod(const biginteger& rhs){

}

biginteger biginteger::abs(){
	biginteger res = *this;
	res.pos = true;
	return res;
}


/* Relational Operators */
bool biginteger::operator==(const biginteger& rhs){

}

bool biginteger::operator>(const biginteger& rhs){

}

bool biginteger::operator<(const biginteger& rhs){

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

int biginteger::max(int a, int b){
	return (a > b) ? a : b;
}