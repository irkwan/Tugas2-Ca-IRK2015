package org.felixlimanta.RSAEncryptor.controller;

import java.io.IOException;
import java.util.Arrays;
import org.felixlimanta.RSAEncryptor.model.BigInt;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.felixlimanta.RSAEncryptor.model.PublicKey;
import org.felixlimanta.RSAEncryptor.model.XmlHelper;
import org.xml.sax.SAXException;

/**
 * Created by ASUS on 10/06/17.
 */
public class RSA {
  private static final int BIT_LENGTH = 1024;

  private PublicKey publicKey;
  private PrivateKey privateKey;
  private AES aes;

  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  BigInt getP() {
    return privateKey.getP();
  }

  BigInt getQ() {
    return privateKey.getQ();
  }

  BigInt getLambda() {
    return privateKey.getLambda();
  }

  public BigInt getN() {
    return privateKey.getN();
  }

  public BigInt getE() {
    return privateKey.getE();
  }

  BigInt getD() {
    return privateKey.getD();
  }

  AES getAes() {
    return aes;
  }

  public String[] encrypt(String plainText) {
    aes = new AES();

    // Encrypt privateKey
    byte[] plainKey = aes.getKey();
    BigInt plainKeyInt = new BigInt(plainKey, (byte) 1);
    BigInt cipherKeyInt = plainKeyInt.modExp(publicKey.getE(), publicKey.getN());
    byte[] cipherKey = cipherKeyInt.toByteArray();

    // Encrypt initialization vector
    byte[] plainIV = aes.getInitVector();
    BigInt plainIVInt = new BigInt(plainIV, (byte) 1);
    BigInt cipherIVInt = plainIVInt.modExp(publicKey.getE(), publicKey.getN());
    byte[] cipherIV = cipherIVInt.toByteArray();

    String manifest = XmlHelper.toXmlString(cipherKey, cipherIV);
    String cipherText = aes.encrypt(plainText);
    return new String[] { manifest, cipherText };
  }

  public String decrypt(String manifest, String cipherText)
      throws SAXException, IOException {
    byte[][] aesKeys = XmlHelper.fromXmlString(manifest);

    // Decrypt privateKey
    BigInt cipherKeyInt = new BigInt(aesKeys[0], (byte) 1);
    BigInt plainKeyInt = cipherKeyInt.modExp(privateKey.getD(), privateKey.getN());
    byte[] plainKey = leftTruncateOrPadByteArray(plainKeyInt.toByteArray(), 16);

    // Decrypt IV
    BigInt cipherIVInt = new BigInt(aesKeys[1], (byte) 1);
    BigInt plainIVInt = cipherIVInt.modExp(privateKey.getD(), privateKey.getN());
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
