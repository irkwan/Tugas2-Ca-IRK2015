// File : rsa.h
// Nama : Dewita Sonya Tarabunga
// NIM : 13515021

#include "bignumber.h"

#ifndef RSA_H
#define RSA_H

class RSA {
public:
	/* Cpnstructor */
	RSA();

	static BigNumber ConvertToNum(string st);
	static string ConvertToString(BigNumber number);
	string EncryptString(string st);
	string DecryptString(string st);
	string EncryptFile(string namafile);

private:
	BigNumber prime1;
	BigNumber prime2;
	BigNumber mod; // = prime1 * prime2
	BigNumber lambda; // = (prime1 - 1) * (prime2 - 1)
	BigNumber enc;
	BigNumber dec; // inverse of enc modulo lambda
};

#endif