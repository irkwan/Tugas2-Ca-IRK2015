// NIM/Nama : 13515057 / Erick Wijaya
// File     : rsa.cpp

#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <string>
#include <ctime>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include "biginteger.h"
using namespace std;

class RSA {
public:
	RSA(char* filename);
	void createEncryptionFile();
	void createDecryptionFile();
	void createTimeFile();
private:
	biginteger p, q;
	biginteger n;
	biginteger eulerPhi;
	biginteger e, d;
	vector<biginteger> cipherText;
	char* filename;
	double elapsedTime;

	vector<char> readFile();
	void initPrimeNumbers();
	void initSecurityParam();
	void initEulerPhi();
	void initPublicKey();
	void initPrivateKey();

	void encrypt(vector<char> plainText);
	vector<char> decrypt();
};

RSA::RSA(char* filename) : filename(filename){
	vector<char> plainText = readFile();cout << "0";

	// start counting time..
	clock_t t = clock();

	initPrimeNumbers();cout << "1";
	initSecurityParam();cout << "2";
	initEulerPhi();cout << "3";

	initPublicKey();cout << "4";
	initPrivateKey();cout << "5";
	encrypt(plainText);cout << "6";

	t = clock() - t;
	elapsedTime = (double)t/CLOCKS_PER_SEC;
	// end of counting time.
}

vector<char> RSA::readFile(){
	ifstream inputFile(filename);
	vector<char> plainText;

	while(inputFile.good()){
		plainText.push_back(inputFile.get());
	}
	plainText.pop_back(); // delete EOF
	inputFile.close();

	return plainText;
}

void RSA::initPrimeNumbers(){
	p = biginteger::generateRandomProbablePrime();
	q = biginteger::generateRandomProbablePrime();
	while (p == q){
		q = biginteger::generateRandomProbablePrime();
	}
//	cout << p << " " << q << endl;
}

void RSA::initSecurityParam(){
	n = p * q;
}

void RSA::initEulerPhi(){
	eulerPhi = (p - 1) * (q - 1);
}

void RSA::initPublicKey(){
	e = 3;
	while (biginteger::gcd(e, eulerPhi) != 1){
		e += 2;
	}
//	cout << "e = " << e << endl;
}

void RSA::initPrivateKey(){
	// e * d + phi * a = gcd(e,phi) = 1
	biginteger a;
	biginteger gcd = biginteger::gcdExtended(e, eulerPhi, d, a);
	if (d < 0)
		d += eulerPhi;
}

void RSA::encrypt(vector<char> plainText){
	// C = P^e % n
	for(int i=0; i<plainText.size(); i++){
		biginteger p(plainText[i]);
		cipherText.push_back(biginteger::modpow(p, e, n));
	}
}

vector<char> RSA::decrypt(){
	// P = C^d % n
	vector<char> plainText;
	for(int i=0; i<cipherText.size(); i++){
		biginteger result = biginteger::modpow(cipherText[i], d, n);
		cout << cipherText[i] << " " << d << " " << " " << n << " = " << result << " " << endl;;
		plainText.push_back((char)result.toInt());
	}
	return plainText;
}

void RSA::createEncryptionFile(){
	const int prefix = 2;
	int len = sizeof(filename)/sizeof(char);
	char efile[len+prefix+1];

	efile[0] = 'e';
	efile[1] = '-';
	efile[len+prefix] = '\0';
	for(int i=prefix; i < len+prefix; i++){
		efile[i] = filename[i-prefix];
	}

	ofstream outputFile(efile);
	
	for(int i=0; i<cipherText.size(); i++){
		outputFile << cipherText[i] << " ";
	}

	outputFile.close();
}

void RSA::createDecryptionFile(){
	const int prefix = 2;
	int len = sizeof(filename)/sizeof(char);
	char dfile[len+prefix+1];

	dfile[0] = 'd';
	dfile[1] = '-';
	dfile[len+prefix] = '\0';
	for(int i=prefix; i < len+prefix; i++){
		dfile[i] = filename[i-prefix];
	}

	ofstream outputFile(dfile);
	
	vector<char> plainText = decrypt();
	for(int i=0; i<plainText.size(); i++){
		outputFile << plainText[i];
	}

	outputFile.close();
}

void RSA::createTimeFile(){
	const int prefix = 2;
	int len = sizeof(filename)/sizeof(char);
	char tfile[len+prefix+1];

	tfile[0] = 't';
	tfile[1] = '-';
	tfile[len+prefix] = '\0';
	for(int i=prefix; i < len+prefix; i++){
		tfile[i] = filename[i-prefix];
	}

	ofstream outputFile(tfile);
	outputFile << "Time Elapsed: " << setprecision(4) << fixed << elapsedTime << endl;
	outputFile.close();
}

/* ---------------------------------------------------------------------- */
/* Main Program Start Here */

int main(int argc, char* argv[]){
	RSA myfile(argv[1]);
	myfile.createEncryptionFile();
	myfile.createDecryptionFile();
	myfile.createTimeFile();

	return 0;
}
