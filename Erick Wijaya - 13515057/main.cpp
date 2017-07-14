#include <iostream>
#include <fstream>
#include <cstdlib>
#include <iomanip>
#include <ctime>
#include "rsa.h"
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
	t = clock();
	RSA security;
	t = clock() - t;
	keyTime = (double)t/CLOCKS_PER_SEC;

	// Read From File
	string input = readFile(argv[1]);
	
	// Encrypt Message
	t = clock();
	string cipher = security.encrypt(input);
	t = clock() - t;
	encryptTime = (double)t/CLOCKS_PER_SEC;
	
	// Decrypt Message
	t = clock();
	string plain = security.decrypt(cipher);
	t = clock() - t;
	decryptTime = (double)t/CLOCKS_PER_SEC;

	// Show Output
	cout << setprecision(4) << fixed;
	cout << "Generate Key Time: " << keyTime << "s"<< endl;
	cout << "Encrypt Time     : " << encryptTime << "s"<< endl;
	cout << "Decrypt Time     : " << decryptTime << "s"<< endl;
	cout << "CipherText: " << cipher << endl;
	cout << "Decrypt Result: " << plain << endl;
	
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