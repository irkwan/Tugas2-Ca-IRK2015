#include "biginteger.h"
using namespace std;

/**
 * A class that handle RSA Encryption and Decryption.
 *
 * @author Erick Wijaya 
 * @version 1.0
 * @since 2017-13-07
 */

class RSA {
public:
	/**
	 * Constructor that constructs new RSA Security with its public and private key.
	 * @param digits The amount of digit to generate prime numbers.
	 */
	RSA(int digits = DEFAULT_DIGITS);

	/**
	 * Encrypt plaintext into ciphertext.
	 * @param plainText The message source.
	 * @return encrypted text.
	 */
	string encrypt(string plainText);

	/**
	 * Decrypt ciphertext into plaintext.
	 * @param cipherText The message source.
	 * @return decrypted text.
	 */
	string decrypt(string cipherText);

	/**
	 * Return the public key.
	 * @return Public key.
	 */
	biginteger getPublicKey();

	/**
	 * Return the private key.
	 * @return Private key.
	 */
	biginteger getPrivateKey();

private:
	biginteger n; // security parameter
	biginteger e; // public key
	biginteger d; // private key

	biginteger p, q;
	biginteger dp, dq;
	biginteger qInv;

	static const int DEFAULT_DIGITS = 20;
	static const biginteger MULTIPLIER_SUB;
	static const biginteger MULTIPLIER_MOD;
};