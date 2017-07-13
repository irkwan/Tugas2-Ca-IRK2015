package org.felixlimanta.RSAEncryptor.controller;

import java.io.IOException;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.felixlimanta.RSAEncryptor.model.PublicKey;
import org.xml.sax.SAXException;

/**
 * Central controller class for RsaEncryptor application
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
public class RsaEncryptorController {
  private RSA rsa;

  public RsaEncryptorController() {
    rsa = new RSA();
  }

  //region Getters and setter
  //------------------------------------------------------------------------------------------------

  /**
   * @return {@code RSA} object for encryption/decryption
   */
  public RSA getRsa() {
    return rsa;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Keys
  //------------------------------------------------------------------------------------------------

  /**
   * Bridges key generation on {@code PrivateKey} class with user interface
   *
   * @return Code execution time
   */
  public long generateKeys() {
    long startTime = System.nanoTime();

    PrivateKey privateKey = new PrivateKey();
    PublicKey publicKey = privateKey.getPublicKey();
    rsa.setPrivateKey(privateKey);
    rsa.setPublicKey(publicKey);

    long endTime = System.nanoTime();
    return endTime - startTime;
  }

  /**
   * Sets {@code rsa}'s public key for encryption
   *
   * @param xml XML string containing public key
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  public void setRsaPublicKey(String xml) throws SAXException, IOException {
    PublicKey key = PublicKey.fromXmlString(xml);
    rsa.setPublicKey(key);
  }

  /**
   * Sets {@code rsa}'s private key for decryption
   *
   * @param xml XML string containing private key
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   */
  public void setRsaPrivateKey(String xml) throws SAXException, IOException {
    PrivateKey key = PrivateKey.fromXmlString(xml);
    rsa.setPrivateKey(key);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Encryption and decryption
  //------------------------------------------------------------------------------------------------

  /**
   * Bridges encryption on {@code RSA} class with user interface
   * encryptedText must be an array of String with length ≥ 2
   *
   * @param plainText Text to be encrypted
   * @param encryptedText Array to store encrypted text
   * @return Execution time
   * @throws NullPointerException Public key not set
   */
  public long encryptText(String plainText, String[] encryptedText)
      throws NullPointerException {
    assert encryptedText.length >= 2;
    long startTime = System.nanoTime();

    String[] s = rsa.encrypt(plainText);
    System.arraycopy(s, 0, encryptedText, 0, 2);

    long endTime = System.nanoTime();
    return endTime - startTime;
  }

  /**
   * Bridges decryption on {@code RSA} class with user interface
   *
   * @param manifest XML containing AES keys
   * @param encryptedText Encrypted text
   * @param plainText Array to store decrypted text
   * @return Execution time
   * @throws SAXException XML parsing error
   * @throws IOException XML parsing error
   * @throws NullPointerException Private key not set
   */
  public long decryptText(String manifest, String encryptedText, String[] plainText)
      throws SAXException, IOException, NullPointerException {
    assert plainText.length >= 1;
    long startTime = System.nanoTime();

    plainText[0] = rsa.decrypt(manifest, encryptedText);

    long endTime = System.nanoTime();
    return endTime - startTime;
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
