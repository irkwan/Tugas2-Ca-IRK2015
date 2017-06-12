package org.felixlimanta.RSAEncryptor.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.felixlimanta.RSAEncryptor.model.BigInt;

/**
 * Created by ASUS on 10/06/17.
 */
public class RSA {
  private static final int BIT_LENGTH = 1024;
  private static final BigInt DEFAULT_E_VALUE = new BigInt("65537");
  private static final int MAX_PLAINTEXT_LENGTH = BIT_LENGTH / 8;
  private static final int MAX_CIPHERTEXT_LENGTH = MAX_PLAINTEXT_LENGTH * 2;
  private static SecureRandom random;

  private BigInt p;
  private BigInt q;
  private BigInt n;
  private BigInt lambda;
  private BigInt e;
  private BigInt d;

  private AES aes;

  static {
    random = new SecureRandom();
  }

  public RSA() {
    generateKeys();
  }

  public RSA(BigInt n, BigInt e, BigInt d) {
    this.n = n;
    this.e = e;
    this.d = d;
  }

  public RSA(String n, String e, String d) {
    this.n = new BigInt(n);
    this.e = new BigInt(e);
    this.d = new BigInt(d);
  }

  BigInt getP() {
    return p;
  }

  BigInt getQ() {
    return q;
  }

  BigInt getLambda() {
    return lambda;
  }

  public BigInt getN() {
    return n;
  }

  public BigInt getE() {
    return e;
  }

  BigInt getD() {
    return d;
  }

  AES getAes() {
    return aes;
  }

  public void generateKeys() {
    // 4. Choose an integer e such that 1 < e < λ(n) and gcd(e, λ(n)) = 1;
    //    i.e., e and λ(n) are coprime.
    // e is predetermined
    e = DEFAULT_E_VALUE;

    // 1. Choose two distinct prime numbers p and q
    do {
      p = BigInt.probablePrime(BIT_LENGTH / 2, random);
    } while (!e.gcd(p.subtract(BigInt.ONE)).equals(BigInt.ONE));
    do {
      q = BigInt.probablePrime(BIT_LENGTH / 2, random);
    } while (!e.gcd(q.subtract(BigInt.ONE)).equals(BigInt.ONE));

    // 2. Compute n = pq
    n = p.multiply(q);

    // 3. Compute λ(n) = lcm(λ(p), λ(q)) = lcm(p − 1, q − 1),
    //    where λ is Carmichael’s totient function. This value is kept private. */
    BigInt p2 = p.subtract(BigInt.ONE);
    BigInt q2 = q.subtract(BigInt.ONE);
    lambda = p2.divide(p2.gcd(q2)).multiply(q2);

    // 5. Determine d as d ≡ e^-1 (mod λ(n));
    //    i.e., d is the modular multiplicative inverse of e (modulo λ(n)).
    d = e.modInverse(lambda);
  }

  public String encrypt(String plainText) throws UnsupportedEncodingException {
    StringBuilder s = new StringBuilder();
    aes = new AES();

    // Encrypt key
    byte[] plainKey = aes.getKey();
    BigInt plainKeyInt = new BigInt(plainKey, (byte) 1);
    BigInt cipherKeyInt = plainKeyInt.modExp(e, n);
    byte[] cipherKey = cipherKeyInt.toByteArray();

    // Append key and key length
    String cipherKeyString = new String(Hex.encodeHex(cipherKey));
    String cipherKeyStringLength =
        padLeftStringWithZeros(Integer.toHexString(cipherKeyString.length()), 4);
    s.append(cipherKeyStringLength).append(cipherKeyString);

    // Encrypt initialization vector
    byte[] plainIV = aes.getInitVector();
    BigInt plainIVInt = new BigInt(plainIV, (byte) 1);
    BigInt cipherIVInt = plainIVInt.modExp(e, n);
    byte[] cipherIV = cipherIVInt.toByteArray();

    // Append IV and IV length
    String cipherIVString = new String(Hex.encodeHex(cipherIV));
    String cipherIVStringLength =
        padLeftStringWithZeros(Integer.toHexString(cipherIVString.length()), 4);
    s.append(cipherIVStringLength).append(cipherIVString);


    // Append text encrypted with AES
    String cipherText = aes.encrypt(plainText);
    s.append(cipherText);
    return s.toString();
  }

  public String decrypt(String cipherText) throws DecoderException, UnsupportedEncodingException {
    int cursor = 0;

    // Get key length and key
    int cipherKeyStringLength =
        Integer.parseInt(cipherText.substring(cursor, cursor += 4), 16);
    String cipherKeyString =
        cipherText.substring(cursor, cursor += cipherKeyStringLength);

    // Decrypt key
    byte[] cipherKey = Hex.decodeHex(cipherKeyString.toCharArray());
    BigInt cipherKeyInt = new BigInt(cipherKey, (byte) 1);
    BigInt plainKeyInt = cipherKeyInt.modExp(d, n);
    byte[] plainKey = leftTruncateOrPadByteArray(plainKeyInt.toByteArray(), 16);

    // Get IV length and IV
    int cipherIVStringLength =
        Integer.parseInt(cipherText.substring(cursor, cursor += 4), 16);
    String cipherIVString =
        cipherText.substring(cursor, cursor += cipherIVStringLength);

    // Decrypt IV
    byte[] cipherIV = Hex.decodeHex(cipherIVString.toCharArray());
    BigInt cipherIVInt = new BigInt(cipherIV, (byte) 1);
    BigInt plainIVInt = cipherIVInt.modExp(d, n);
    byte[] plainIV = leftTruncateOrPadByteArray(plainIVInt.toByteArray(), 16);

    // Decrypt text
    aes = new AES(plainKey, plainIV);
    String message = cipherText.substring(cursor, cipherText.length());
    return aes.decrypt(message);
  }

  private static byte[] leftTruncateOrPadByteArray(byte[] b, int n) {
    if (b.length > n) {
      return Arrays.copyOfRange(b, Math.max(b.length - n, 0), b.length);
    } else if (b.length < n) {
      byte[] bPadded = new byte[n];
      System.arraycopy(b, 0, bPadded, Math.max(0, n - b.length), b.length);
      return bPadded;
    }
    return b;
  }
  
  static String padLeftStringWithZeros(String s, int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; ++i)
      sb.append('0');
    sb.append(s);
    return sb.substring(s.length());
  }
}
