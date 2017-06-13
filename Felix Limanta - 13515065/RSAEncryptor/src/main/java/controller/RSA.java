package org.felixlimanta.RSAEncryptor.controller;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Arrays;
import org.apache.commons.codec.DecoderException;
import org.felixlimanta.RSAEncryptor.model.BigInt;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.felixlimanta.RSAEncryptor.model.XmlHelper;

/**
 * Created by ASUS on 10/06/17.
 */
public class RSA {
  private static final int BIT_LENGTH = 1024;
  private static final BigInt DEFAULT_E_VALUE = new BigInt("65537");
  private static final int MAX_PLAINTEXT_LENGTH = BIT_LENGTH / 8;
  private static SecureRandom random;

  private PrivateKey key;
  private AES aes;

  static {
    random = new SecureRandom();
  }

  public RSA() {
    key = new PrivateKey();
  }

  public RSA(BigInt n, BigInt e, BigInt d) {
    key = new PrivateKey(n, e, d);
  }

  public RSA(String n, String e, String d) {
    key = new PrivateKey(new BigInt(n), new BigInt(e), new BigInt(d));
  }

  BigInt getP() {
    return key.getP();
  }

  BigInt getQ() {
    return key.getQ();
  }

  BigInt getLambda() {
    return key.getLambda();
  }

  public BigInt getN() {
    return key.getN();
  }

  public BigInt getE() {
    return key.getE();
  }

  BigInt getD() {
    return key.getD();
  }

  AES getAes() {
    return aes;
  }

  public String[] encrypt(String plainText) throws UnsupportedEncodingException {
    aes = new AES();

    // Encrypt key
    byte[] plainKey = aes.getKey();
    BigInt plainKeyInt = new BigInt(plainKey, (byte) 1);
    BigInt cipherKeyInt = plainKeyInt.modExp(key.getE(), key.getN());
    byte[] cipherKey = cipherKeyInt.toByteArray();

    // Encrypt initialization vector
    byte[] plainIV = aes.getInitVector();
    BigInt plainIVInt = new BigInt(plainIV, (byte) 1);
    BigInt cipherIVInt = plainIVInt.modExp(key.getE(), key.getN());
    byte[] cipherIV = cipherIVInt.toByteArray();

    String manifest = XmlHelper.toXmlString(cipherKey, cipherIV);
    String cipherText = aes.encrypt(plainText);
    return new String[] { manifest, cipherText };
  }

  public String decrypt(String manifest, String cipherText)
      throws DecoderException, UnsupportedEncodingException {
    byte[][] aesKeys = XmlHelper.fromXmlString(manifest);

    // Decrypt key
    BigInt cipherKeyInt = new BigInt(aesKeys[0], (byte) 1);
    BigInt plainKeyInt = cipherKeyInt.modExp(key.getD(), key.getN());
    byte[] plainKey = leftTruncateOrPadByteArray(plainKeyInt.toByteArray(), 16);

    // Decrypt IV
    BigInt cipherIVInt = new BigInt(aesKeys[1], (byte) 1);
    BigInt plainIVInt = cipherIVInt.modExp(key.getD(), key.getN());
    byte[] plainIV = leftTruncateOrPadByteArray(plainIVInt.toByteArray(), 16);

    // Decrypt text
    aes = new AES(plainKey, plainIV);
    return aes.decrypt(cipherText);
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
