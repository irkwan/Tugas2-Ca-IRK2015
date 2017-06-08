// NIM/Nama : 13515057 / Erick Wijaya
// File     : biginteger.cpp

#include "biginteger.h"
#include <iostream>
#include <vector>
#include <string>
using namespace std;

/* Constructors */
biginteger::biginteger(){
	sign = true;
}

biginteger::biginteger(long long v){
	sign = v >= 0;
	vector<char> temp;
	while (v != 0){
		char dig = v % 10;
		temp.push_back(dig);
		v /= 10;
	}
	for(int i=temp.size()-1; i>=0; i--){
		digits.push_back(temp[i]);
	}
}

biginteger::biginteger(const string& v){
	sign = v[0] != '-';
	int i = sign ? 0 : 1;
	for(; i<v.length(); i++){
		digits.push_back(v[i]);
	}
}

biginteger::biginteger(const biginteger& v){
	sign = v.sign;
	digits = v.digits;
}


/* Operator= */
biginteger& biginteger::operator=(const biginteger& rhs){

}


/* Arithmetic Operators */
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


biginteger biginteger::operator+(const biginteger& rhs){

}

biginteger biginteger::operator-(const biginteger& rhs){

}

biginteger biginteger::operator*(const biginteger& rhs){

}

biginteger biginteger::operator/(const biginteger& rhs){

}

biginteger biginteger::operator%(const biginteger& rhs){

}


/* Relational Operators */
bool biginteger::operator==(const biginteger& rhs){

}

bool biginteger::operator>(const biginteger& rhs){

}

bool biginteger::operator<(const biginteger& rhs){

}

bool biginteger::isZero() const {
	return digits.empty();
}


/* I/O */
istream& operator>>(istream &is, const biginteger& v){

}

ostream& operator<<(ostream &os, const biginteger& v){
	if (v.isZero()){
		os << 0;
	}
	else {
		if (!v.sign)
			os << "-";
		for(int i=0; i<v.digits.size(); i++)
			os << v.digits[i];
	}
	return os;
}
