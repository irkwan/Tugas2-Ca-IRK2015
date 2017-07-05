// NIM/Nama : 13515057 / Erick Wijaya
// File     : rsa.cpp

#include <iostream>
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
	vector<char> cipherText;
	char* filename;

	void readFile();
	void initPrimeNumbers();
	void initSecurityParam();
	void initEulerPhi();
	void initPublicKey();
	void initPrivateKey();

	long long generateRandomPrimeNumber();
	long long pow2(int n);
	biginteger gcd(biginteger a, biginteger b);
};

RSA::RSA(char* filename) : filename(filename){
	srand(time(NULL));
	readFile();

	initPrimeNumbers();
	initSecurityParam();
	initEulerPhi();
	initPublicKey();

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
	e = biginteger::TEN + biginteger::ONE; // start from 11
	while (gcd(e, biginteger(265766508)) != 1){
		e += biginteger::TWO;
	}
	cout << e << endl;
}

void RSA::initPrivateKey(){

}

void RSA::createEncryptionFile(){
	const int prefix = 2;
	int len = sizeof(filename)/sizeof(char);
	char efile[len+prefix];

	efile[0] = 'e';
	efile[1] = '-';
	for(int i=prefix; i < len+prefix; i++){
		efile[i] = filename[i-prefix];
	}

	ofstream outputFile(efile);
	// insert ciphertext in file
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

biginteger RSA::gcd(biginteger a, biginteger b){
	if (a == biginteger::ZERO)
		return b;
	else
		return gcd(b%a, a);
}

/* ---------------------------------------------------------------------- */
/* Main Program Start Here */

int main(int argc, char* argv[]){
	RSA myfile(argv[1]);
	myfile.createEncryptionFile();

	return 0;
}
