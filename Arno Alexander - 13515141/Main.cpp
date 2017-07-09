/*
Author : Arno Alexander
*/

#include <iostream>
#include <ctime>
#include "BigNumber.h"
#include "RSA.h"

using namespace std;

int main() {
	time_t start, encryptionTime, decryptionTime;
	string input, encryption, decryption;
	cout << "Path to initial file      : ";
	cin >> input;
	cout << "Path to encryption result : ";
	cin >> encryption;
	cout << "Path to decryption result : ";
	cin >> decryption;
	cout << endl << "Generating keys..." << endl;
	start = clock();
	BigNumber prime1 = BigNumber::generateProbablePrime(20);
	BigNumber prime2 = BigNumber::generateProbablePrime(20);
	if (prime1 == prime2) prime2 = prime1.nextProbablePrime();
	RSA rsa(prime1, prime2);
	cout << "Encrypting file..." << endl;
	rsa.encryptAscii(input,encryption);
	encryptionTime = clock();
	cout << "Decrypting file..." << endl;
	rsa.decryptAscii(encryption,decryption);
	decryptionTime = clock();
	cout << endl << "Process done. Statistics :" << endl;
	cout << "Time elapsed (key generation and encryption) : " << (long double)(encryptionTime-start)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Time elapsed (total processing)              : " << (long double)(decryptionTime-start)/CLOCKS_PER_SEC << " s" << endl;
	cout << "Prime 1 (secret)                             : " << prime1 << endl;
	cout << "Prime 2 (secret)                             : " << prime2 << endl;
	cout << "Key n (public)                               : " << rsa.get_n() << endl;
	cout << "Key e (public)                               : " << rsa.get_e() << endl;
	cout << "Key d (secret)                               : " << rsa.get_d() << endl;
	return 0;
}