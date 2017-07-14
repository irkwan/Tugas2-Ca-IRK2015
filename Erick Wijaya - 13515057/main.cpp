#include <iostream>
#include <fstream>
#include <cstdlib>
#include <iomanip>
#include <ctime>
#include "src/rsa.h"
using namespace std;

string readFile(char* filename);

int main(int argc, char* argv[]){
	if (argc < 2){
		cout << "Error: Program must accept a filename as an argument" << endl;
		cout << "Example: ./main myfile.txt" << endl;
		return EXIT_SUCCESS;
	}

	clock_t t;
	double keyTime, encryptTime, decryptTime;

	// Generate Public and Private Key
	cout << "Generating Keys... ";
	t = clock();
	RSA security;
	t = clock() - t;
	keyTime = (double)t/CLOCKS_PER_SEC;
	cout << "Done" << endl;

	// Read From File
	string input = readFile(argv[1]);
	
	// Encrypt Message
	cout << "Encrypting text... ";
	t = clock();
	string cipher = security.encrypt(input);
	t = clock() - t;
	encryptTime = (double)t/CLOCKS_PER_SEC;
	cout << "Done" << endl;
	
	// Decrypt Message
	cout << "Decrypting text... ";
	t = clock();
	string plain = security.decrypt(cipher);
	t = clock() - t;
	decryptTime = (double)t/CLOCKS_PER_SEC;
	cout << "Done" << endl << endl;

	// Show Output
	cout << setprecision(3) << fixed;
	cout << "-- RESULT --" << endl;
	cout << "Generate Key Time: " << keyTime << "s"<< endl;
	cout << "Encrypt Time     : " << encryptTime << "s"<< endl;
	cout << "Decrypt Time     : " << decryptTime << "s"<< endl;
	cout << "Total Time       : " << keyTime + encryptTime + decryptTime << "s"<< endl << endl;
	cout << "Public Key       : " << security.getPublicKey() << endl;
	cout << "Private Key      : " << security.getPrivateKey() << endl << endl;
	cout << "Plain Text       : " << endl << input << endl << endl;
	cout << "Cipher Text      : " << endl << cipher << endl << endl;
	cout << "Decrypt Result   : " << endl << plain << endl;
	
	return 0;
}

string readFile(char* filename){
	ifstream inputFile(filename);
	string plainText;

	while(inputFile.good()){
		char c = inputFile.get();
		if (c >= 0)
			plainText += c;
	}
	inputFile.close();

	return plainText;
}