// File : rsa.cpp
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include <fstream>
#include "rsa.h"

/* Cpnstructor */
RSA::RSA() {
	BigNumber one("1");
	prime1 = prime1.GenerateRandomPrime(20);
	prime2 = prime2.GenerateRandomPrime(25);
	mod = prime2 * prime1;
	lambda = mod - prime2 - prime1 + one;
	enc = lambda.Random();
	while (enc.GCD(lambda, dec) != one) {
		--enc;
	}
}

BigNumber RSA::ConvertToNum(string st) {
	BigNumber temp("128"), res;
	for (long long i = 0; i < st.size(); ++i) {
		res *= temp;
		res += int(st[i]);
	}
	return res;
}

string RSA::ConvertToString(BigNumber number) {
	string res;
	BigNumber temp("128"), mod;
	while (number.IsPositive()) {
		number.Divide(temp, mod);
		res = mod.ConvertToChar() + res;
	}
	return res;
}

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

string RSA::EncryptFile(string namafile) {
	const char *file = namafile.c_str();
	ifstream in;
	in.open(file);
	string inp, temp;
	while (getline(in, temp)) {
		inp += temp;
		inp += '\n';
	}
	return EncryptString(inp);
}