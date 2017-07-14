package com.endec.model;

import java.security.SecureRandom;

/*
 * File name          : RSA.java
 * Created on         : 14/07/17
 * Modified on        : 14/07/17
 */

/**
 * Implementation of RSA cryptosystem.
 *
 * @author Jordhy Fernando
 * @version 1.0
 */
public class RSA {

  private BigInteger n; //modulus for the public key and private key
  private BigInteger e; //public key exponent
  private BigInteger d; //private key exponent

  public RSA(int length) {
    BigInteger[] keys = generateKeys(length);
    n = keys[0];
    e = keys[1];
    d = keys[2];
  }

  /**
   * Return the minimum integer between a and b.
   * @param a integer to be compared.
   * @param b integer to be compared.
   * @return min between a and b.
   */
  private static int min(int a, int b) {
    return (a < b ? a : b);
  }

  /**
   * Return the maximum integer between a and b.
   * @param a integer to be compared.
   * @param b integer to be compared.
   * @return maximal between a and b.
   */
  private static int max(int a, int b) {
    return (a > b ? a : b);
  }

  /**
   * Generate public and private keys needed for RSA algorithm.
   * @param length length of the prime number used to generate the keys.
   * @return array of BigInteger that contains the keys.
   */
  public static BigInteger[] generateKeys(int length) {
    BigInteger[] keys = new BigInteger[3];
    SecureRandom random = new SecureRandom();
    BigInteger p = BigInteger.probablePrime(random.nextInt(6) + max(20, length),
        128);
    BigInteger q = BigInteger.probablePrime(random.nextInt(6) + max(20, length),
        128);
    keys[0] = p.multiply(q);
    BigInteger temp1 = p.subtract(new BigInteger(1));
    BigInteger temp2 = q.subtract(new BigInteger(1));
    BigInteger totient = temp1.divide(temp1.gcd(temp2)).multiply(temp2);
    temp1 = new BigInteger(2);
    temp2 = totient.subtract(new BigInteger(1));
    do {
      keys[1] = BigInteger.random(temp1, temp2);
    } while (!totient.gcd(keys[1]).equals(new BigInteger(1)));
    keys[2] = keys[1].modInverse(totient);
    return keys;
  }

  /**
   * Encrypt m (plaintext) using public key.
   * @param m plaintext to be encrypted.
   * @param e public key.
   * @param n modulus.
   * @return ciphertext.
   */
  public static BigInteger encrypt(BigInteger m, BigInteger e, BigInteger n) {
    return m.modPow(e, n);
  }

  /**
   * Encrypt m (plaintext) using public key.
   * @param m plaintext to be encrpyted.
   * @return ciphertext.
   */
  public BigInteger encrypt(BigInteger m) {
    return m.modPow(e, n);
  }

  /**
   * Decrypt c (ciphertext) using private key.
   * @param c ciphertext to be decrypted.
   * @param d private key.
   * @param n modulus.
   * @return plaintext.
   */
  public static BigInteger decrypt(BigInteger c, BigInteger d, BigInteger n) {
    return c.modPow(d, n);
  }

  /**
   * Decrypt c (ciphertext) using private key.
   * @param c ciphertext to be decrypted.
   * @return plaintext.
   */
  public BigInteger decrypt(BigInteger c) {
    return c.modPow(d, n);
  }

  /**
   * Return string that contains the keys of RSA (modulus, public, private).
   * @return String that contains the keys of RSA.
   */
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("n: ").append(n);
    result.append("\n");
    result.append("e: ").append(e);
    result.append("\n");
    result.append("d: ").append(d);
    return result.toString();
  }
}
