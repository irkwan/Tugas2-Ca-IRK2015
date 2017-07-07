#include "rsa.h"
#include "bignumber.h"
#include <map>
#include <stdlib.h>
#include <time.h>
using namespace std;

int main() {
	system("CLS");
	cout << "Setting up. Please wait" << endl;
	clock_t start = clock();
	RSA rsa;
	cout << "Generation time : " << (double)(clock() - start)/CLOCKS_PER_SEC << endl;
	map<string, string> ss;
	map<string, bool> sb;
	string file, enc, in;
	cout << endl;
	while (in != "3") {
		cout << "Menu :" << endl;
		cout << "1. Encrypt Files" << endl;
		cout << "2. Decrypt Files" << endl;
		cout << "3. Exit" << endl << endl;
		cout << "Input : ";
		cin >> in;
		if (in == "1") {
			cout << endl << "Enter file's name : ";
			cin >> file;
			cout << "Please wait until the process is done." << endl;
			start = clock();
			enc = rsa.EncryptFile(file);
			cout << endl << "Ciphertext : " << endl;
			cout << enc << endl << endl;
			cout << "Encryption time : " << (double)(clock() - start)/CLOCKS_PER_SEC << endl << endl;
			ss[file] = enc;
			sb[file] = true;
		} else if (in == "2") {
			cout << endl << "Enter file's name : ";
			cin >> file;
			if (sb[file]) {
				cout << "Please wait until the process is done." << endl;
				start = clock();
				enc = rsa.DecryptString(ss[file]);
				cout << endl << "Plaintext : " << endl;
				cout << enc << endl << endl;
				cout << "Decryption time : " << (double)(clock() - start)/CLOCKS_PER_SEC << endl << endl;
			} else {
				cout << "File has not been encrypted" << endl << endl;
			}
		} else if (in != "3") {
			cout << endl << "The input is not valid." << endl;
			cout << "Please enter number from 1 to 3." << endl << endl;
		}
	}
	return 0;
}