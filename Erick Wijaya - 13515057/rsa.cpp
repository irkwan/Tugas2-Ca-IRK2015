/**
 * Implementation of RSA Class.
 * For more details, see rsa.h.
 *
 * @author Erick Wijaya 
 * @version 1.0
 * @since 2017-13-07
 */

#include <iostream>
#include <iomanip>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <ctime>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include "rsa.h"

const biginteger RSA::MULTIPLIER_SUB = biginteger(255);
const biginteger RSA::MULTIPLIER_MOD = biginteger(256);

RSA::RSA(int digits){
	// Initialize Prime Numbers
	p = biginteger::generateRandomProbablePrime(digits);
	q = biginteger::generateRandomProbablePrime(digits);
	while (p == q){
		q = biginteger::generateRandomProbablePrime(digits);
	}

	// Calculate Security Parameter
	n = p * q;

	// Calculate EulerPhi
	biginteger eulerPhi = (p - 1) * (q - 1);

	// Calculate Public Key
	e = 3;
	while (biginteger::gcd(e, eulerPhi) != 1){
		e += 2;
	}

	// Calculate Private Key
	biginteger a;
	biginteger gcd = biginteger::gcdExtended(e, eulerPhi, d, a);
	if (d < 0)
		d += eulerPhi;

	// Initialize for RSA-CRT Decryption
	dp = d % (p - 1);
	dq = d % (q - 1);
	gcd = biginteger::gcdExtended(q, p, qInv, a);
	if (qInv < 0)
		qInv += p;
}

string RSA::encrypt(string plainText){
	stringstream sscipher;

	// C = P^e % n
	for(int i=0; i<plainText.length(); i++){
		biginteger p(plainText[i]);
		biginteger mul = (n - MULTIPLIER_SUB) / MULTIPLIER_MOD;
		int len = mul.toString().length();
		if (len > 0)
			len--;

		p += biginteger::generateRandom(len) * MULTIPLIER_MOD;
		sscipher << biginteger::modpow(p, e, n).toString();
		if (i < plainText.length()-1)
			sscipher << " ";
	}

	return sscipher.str();
}

string RSA::decrypt(string cipherText){
	stringstream ssplain;
	stringstream sscipher(cipherText);

	// P = C^d % n
	biginteger in;
	while (sscipher >> in){
		/*biginteger m1 = biginteger::modpow(in, dp, p);
		biginteger m2 = biginteger::modpow(in, dq, q);
		//biginteger h = (qInv * (m1 - m2)) % p;
		biginteger h = qInv;
		h *= (m1 - m2);
		h %= p;

		if (h < 0)
			h += p;
		
		biginteger result = m2 + h * q;
		result %= MULTIPLIER_MOD;
		ssplain << (char)result.toInt();*/

		/*biginteger result = biginteger::modpow(in, d, n);
		plainText += (char)result.toInt();*/

		biginteger result = biginteger::modpow(in, d, n);
		result %= MULTIPLIER_MOD;
		ssplain << (char)result.toInt();
	}

	return ssplain.str();
}

/*
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
*/

/* ---------------------------------------------------------------------- */
/* Main Program Start Here */

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

int main(int argc, char* argv[]){
	clock_t t;
	double keyTime, encryptTime, decryptTime;

	t = clock(); // Generate Public and Private Key
	RSA security;
	t = clock() - t;
	keyTime = (double)t/CLOCKS_PER_SEC;

	string input = readFile(argv[1]); // Read From File
	
	t = clock(); // Encrypt Message
	string cipher = security.encrypt(input);
	t = clock() - t;
	encryptTime = (double)t/CLOCKS_PER_SEC;
	
	t = clock(); // Decrypt Message
	string plain = security.decrypt(cipher);
	t = clock() - t;
	decryptTime = (double)t/CLOCKS_PER_SEC;

	cout << setprecision(4) << fixed;
	cout << "Generate Key Time: " << keyTime << endl;
	cout << "Encrypt Time     : " << encryptTime << endl;
	cout << "Decrypt Time     : " << decryptTime << endl;
	cout << "CipherText: " << cipher << endl;
	cout << "Decrypt Result: " << plain << endl;
	


	return 0;
}
