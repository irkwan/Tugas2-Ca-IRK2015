// NIM/Nama : 13515057 / Erick Wijaya
// File     : rsa.cpp

#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <string>
#include <cstdio>      /* NULL */
#include <cstdlib>     /* srand, rand */
#include <ctime>       /* time */
#include <cmath>
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
	vector<char> plainText;
	vector<biginteger> cipherText;
	char* filename;
	double elapsedTime;

	void readFile();
	void initPrimeNumbers();
	void initSecurityParam();
	void initEulerPhi();
	void initPublicKey();
	void initPrivateKey();

	void encrypt();
	vector<char> decrypt();

	long long generateRandomPrimeNumber();
	long long pow2(int n);
	biginteger gcd(biginteger a, biginteger b);
	biginteger gcdExtended(biginteger a, biginteger b, biginteger& x, biginteger& y);
};

RSA::RSA(char* filename) : filename(filename){
	srand(time(NULL));
	readFile();

	initPrimeNumbers();cout << "1";
	initSecurityParam();cout << "2";
	initEulerPhi();cout << "3";

	// start counting time..
	clock_t t = clock();

	initPublicKey();cout << "4";
	initPrivateKey();cout << "5";
	encrypt();cout << "6";

	t = clock() - t;
	elapsedTime = (double)t/CLOCKS_PER_SEC;
	// end of counting time.
}

void RSA::readFile(){
	ifstream inputFile(filename);
	char c;
	while(inputFile.good()){
		plainText.push_back(inputFile.get());
	}
	plainText.pop_back(); // delete EOF
	inputFile.close();

//	for(int i=0; i<plainText.size(); i++){
//		cout << plainText[i] << " " << (int)plainText[i] << endl;
//	}
	
}

void RSA::initPrimeNumbers(){
	p = biginteger(generateRandomPrimeNumber());
	q = biginteger(generateRandomPrimeNumber());
	while (p == q){
		q = biginteger(generateRandomPrimeNumber());
	}
//	cout << p << " " << q << endl;
}

void RSA::initSecurityParam(){
//	n = p * q;
}

void RSA::initEulerPhi(){
//	eulerPhi = (p-1)*(q-1);
}

void RSA::initPublicKey(){
	e = biginteger::THREE;
//	while (gcd(e, biginteger(265766508)) != 1){
//		e += biginteger::TWO;
//	}
//	cout << e << endl;
}

void RSA::initPrivateKey(){
	// e * d + phi * a = gcd(e,phi) = 1
	biginteger a;
	biginteger one = biginteger::gcdExtended(e, eulerPhi, d, a);
}

void RSA::encrypt(){

}

vector<char> RSA::decrypt(){

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
	// insert ciphertext in file
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
	// decrypt
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
}

long long RSA::generateRandomPrimeNumber(){
	const int BITS = sizeof(long long) * 4 - 1; // bit size for prime number
	int bin[BITS];
	bool isPrime = false;
	long long num;

	
	while (!isPrime){
		bin[0] = 1; // make sure num is big
		bin[BITS-1] = 1; // make sure num is odd
		for(int i=1; i<BITS-1; i++){
			bin[i] = (rand()%2 == 1);
		}

		// convert bin to LL
		num = 0;
		for(int i=BITS-1; i>=0; i--){
			num += bin[i] * pow2(i);
		}

		// check prime
		int i = 3;
		isPrime = true;
		while (isPrime && (i<=(int)sqrt(num))){
			if (num % i == 0)
				isPrime = false;
			else
				i += 2;
		}
	}

	return num;
}

long long RSA::pow2(int n){
	if (n == 0)
		return 1;
	else if (n == 1)
		return 2;
	else{
		int newn = pow2(n/2);
		if (n % 2 == 0)
			return newn * newn;
		else
			return newn * newn * 2;
	}
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
