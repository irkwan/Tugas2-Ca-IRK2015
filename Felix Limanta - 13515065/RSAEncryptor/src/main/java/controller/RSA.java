package org.felixlimanta.RSAEncryptor.controller;

import java.io.IOException;
import java.util.Arrays;
import org.felixlimanta.RSAEncryptor.model.BigInt;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.felixlimanta.RSAEncryptor.model.PublicKey;
import org.felixlimanta.RSAEncryptor.model.XmlHelper;
import org.xml.sax.SAXException;

/**
 * Handles RSA-1024 encryption process coupled with AES-128
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-10
 */
public class RSA {

  private PublicKey publicKey;
  private PrivateKey privateKey;
  private AES aes;

  //region Getters and setter
  //------------------------------------------------------------------------------------------------

  /**
   * @param publicKey RSA public key
   */
  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  /**
   * @param privateKey RSA private key
   */
  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  /**
   * @return RSA public key
   */
  public PublicKey getPublicKey() {
    return publicKey;
  }

  /**
   * @return RSA private key
   */
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  /**
   * @return AES encryptor
   */
  public AES getAes() {
    return aes;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Encryption and decryption
  //------------------------------------------------------------------------------------------------

  /**
   * Encrypts plain text with AES-128, then encrypts AES key with RSA-1024
   *
   * @param plainText Text to be encrypted
   * @return Encrypted text
   */
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

    String manifest = XmlHelper.aesKeyToXmlString(cipherKey, cipherIV);
    String cipherText = aes.encrypt(plainText);
    return new String[] { manifest, cipherText };
  }

  /**
   * Decrypts AES key with RSA-1024, then decrypts encrypted text with AES-128
   *
   * @param manifest XML string containing encrypted AES key
   * @param cipherText Encrypted text
   * @return Decrypted text
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  public String decrypt(String manifest, String cipherText)
      throws SAXException, IOException {
    byte[][] aesKeys = XmlHelper.aesKeyFromXmlString(manifest);

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

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Private utility functions
  //------------------------------------------------------------------------------------------------

  /**
   * Left pads or truncates a byte array to a specified length
   *
   * @param b Original byte array
   * @param n New byte array length
   * @return Padded or truncated byte array
   */
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

  //------------------------------------------------------------------------------------------------
  //endregion
}
