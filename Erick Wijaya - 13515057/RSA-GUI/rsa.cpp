/**
 * Implementation of RSA Class.
 * For more details, see rsa.h.
 *
 * @author Erick Wijaya 
 * @version 1.0
 * @since 2017-13-07
 */

#include <iostream>
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
    if (digits <= 0){
        e = 0;
        n = 0;
        d = 0;
    }
    else{
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
		biginteger result = biginteger::modpow(in, d, n);
		result %= MULTIPLIER_MOD;
		ssplain << (char)result.toInt();
	}

	return ssplain.str();
}

biginteger RSA::getPublicKey(){
	return e;
}

biginteger RSA::getPrivateKey(){
	return d;
}
