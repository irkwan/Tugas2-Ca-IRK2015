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
	while (v != 0){

	}
}

biginteger::biginteger(const string &v){

}

biginteger::biginteger(const biginteger &v){

}


/* Destructor */
biginteger::~biginteger(){

}


/* Operator= */
biginteger& biginteger::operator=(const biginteger &rhs){

}


/* Arithmetic Operators */
biginteger biginteger::add(const biginteger &rhs){

}

biginteger biginteger::min(const biginteger &rhs){

}

biginteger biginteger::mul(const biginteger &rhs){

}

biginteger biginteger::div(const biginteger &rhs){

}

biginteger biginteger::mod(const biginteger &rhs){

}


biginteger biginteger::operator+(const biginteger &rhs){

}

biginteger biginteger::operator-(const biginteger &rhs){

}

biginteger biginteger::operator*(const biginteger &rhs){

}

biginteger biginteger::operator/(const biginteger &rhs){

}

biginteger biginteger::operator%(const biginteger &rhs){

}


/* Relational Operators */
bool biginteger::operator==(const biginteger &rhs){

}

bool biginteger::operator>(const biginteger &rhs){

}

bool biginteger::operator<(const biginteger &rhs){

}

