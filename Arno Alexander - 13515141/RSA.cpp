/*
Author : Arno Alexander
*/

#include "RSA.h"

RSA::RSA() {
	n = BigNumber(323);
	e = BigNumber(17);
	d = BigNumber(17);
}

RSA::RSA(unsigned minPrimeLength) {
	generateNew(minPrimeLength);
}

RSA::RSA(const BigNumber& prime1, const BigNumber& prime2) {
	generateNew(prime1, prime2);
}

BigNumber RSA::get_n() const {
	return n;
}

BigNumber RSA::get_e() const {
	return e;
}

BigNumber RSA::get_d() const {
	return d;
}

void RSA::generateNew(unsigned minPrimeLength) {
	BigNumber p = BigNumber::generateProbablePrime(minPrimeLength);
	BigNumber q = BigNumber::generateProbablePrime(minPrimeLength);
	if (p == q) {
		q = p.nextProbablePrime();
	}
	generateNew(p,q);
}

void RSA::generateNew(const BigNumber& prime1, const BigNumber& prime2) {
	n = prime1 * prime2;
	BigNumber m = BigNumber::lcm((prime1-1),(prime2-1));
	BigNumber two(2), four(4);
	unsigned modsix = 3;
	e = 3;
	while (BigNumber::gcd(m,e) != 1) {
		if (modsix == 1) {
			modsix = 5;
			e += four;
		} else {
			modsix = (modsix + 2) % 6;
			e += two;
		}
	}
	BigNumber big(m), small(e), temp, zero(0), one(1);
	deque<BigNumber> d_candidate;
	pair<BigNumber,BigNumber> qr;
	bool stop = false;
	d_candidate.push_back(zero);
	d_candidate.push_back(zero);
	d_candidate.push_back(one);
	do {
		qr = BigNumber::divMod(big,small);
		temp = qr.second;
		big = small;
		small = temp;
		stop = temp==zero;
		if (!stop) {
			d_candidate.pop_front();
			d_candidate.push_back(d_candidate[0]-qr.first*d_candidate[1]);
		}
	} while (!stop);
	d = d_candidate.back();
	if (d<=zero) d+=m;
}

BigNumber RSA::asciiToBigNumber(char character) {
	return BigNumber((unsigned char)(character));
}

char RSA::bigNumberToAscii(const BigNumber& bn) {
	return ((char)(bn.toLongLong()));
}

void RSA::encryptAscii(const string& originPath, const string& destinationPath) const {
	BigNumber multiplier = (n - 255) / 256;
	unsigned multiplierLength = multiplier.toString().length();
	if (multiplierLength > 0) {
		multiplierLength--;
	}
	ifstream inf(originPath.c_str());
	ofstream outf(destinationPath.c_str());
	char chr;
	while (inf.get(chr)) {
		outf << BigNumber::powMod(BigNumber::generateRandom(multiplierLength)*256+asciiToBigNumber(chr),e,n) << " ";
	}
}

void RSA::decryptAscii(const string& originPath, const string& destinationPath) const {
	ifstream inf(originPath.c_str());
	ofstream outf(destinationPath.c_str());
	BigNumber chr;
	while (inf >> chr) {
		outf << bigNumberToAscii(BigNumber::powMod(chr,d,n)%256);
	}
}

void RSA::encryptAscii(const string& originPath, const string& destinationPath, const BigNumber& n_value, const BigNumber& e_value) {
	RSA rsa;
	rsa.n = n_value;
	rsa.e = e_value;
	rsa.encryptAscii(originPath, destinationPath);
}

void RSA::decryptAscii(const string& originPath, const string& destinationPath, const BigNumber& n_value, const BigNumber& d_value) {
	RSA rsa;
	rsa.n = n_value;
	rsa.d = d_value;
	rsa.decryptAscii(originPath, destinationPath);
}