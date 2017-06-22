// File : rsa.cpp
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include "rsa.h"

/* Cpnstructor */
RSA::RSA() {
	prime1 = prime1.GenerateRandomPrime(7);
	prime2 = prime2.GenerateRandomPrime(8);
	mod = prime2 * prime1;
	lambda = mod - prime2 - prime1 + ONE;
	enc = lambda.Random();
	while (enc.GCD(lambda, dec) != ONE) {
		enc = lambda.Random();
	}
}

BigNumber RSA::ConvertToNum(string st);

string RSA::ConvertToString(BigNumber number);

string RSA::EncryptString(string st) {
	BigNumber temp = ConvertToNum(st);
	temp.ModPow(enc, mod);
	return ConvertToString(temp);
}

string RSA::DecryptString(string st) {
	BigNumber temp = ConvertToNum(st);
	temp.ModPow(dec, mod);
	return ConvertToString(temp);
}