/*
Author : Arno Alexander
*/

#ifndef RSA_H
#define RSA_H

#include <string>
#include <utility>
#include <fstream>
#include "BigNumber.h"

using namespace std;

class RSA {
public:
	/*CONSTRUCTORS*/
	RSA(); //generated with small primes, enough for ascii
	RSA(unsigned minPrimeLength);
	RSA(const BigNumber& prime1, const BigNumber& prime2);

	/*GETTER*/
	BigNumber get_n() const;
	BigNumber get_e() const;
	BigNumber get_d() const;

	/*METHODS*/
	void generateNew(unsigned minPrimeLength);
	void generateNew(const BigNumber& prime1, const BigNumber& prime2);
	static BigNumber asciiToBigNumber(char character);
	static char bigNumberToAscii(const BigNumber& bn);

	//TODO: make these
	void encryptAscii(const string& originPath, const string& destinationPath) const;
	void decryptAscii(const string& originPath, const string& destinationPath) const;
	static void encryptAscii(const string& originPath, const string& destinationPath, const BigNumber& n_value, const BigNumber& e_value);
	static void decryptAscii(const string& originPath, const string& destinationPath, const BigNumber& n_value, const BigNumber& d_value);

private:
	/*ATTRIBUTES*/
	BigNumber n; //public, for encryption and decryption
	BigNumber e; //public, for encryption
	BigNumber d; //secret, for decryption
};

#endif