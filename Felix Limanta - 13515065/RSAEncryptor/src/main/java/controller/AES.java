package org.felixlimanta.RSAEncryptor.controller;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Handles AES-128 encryption process
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-12
 */
public class AES {

  private byte[] key;
  private byte[] initVector;

  //region Construction
  //------------------------------------------------------------------------------------------------

  /**
   * Generates and constructs a new AES key
   */
  public AES() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(128);

      key = keyGen.generateKey().getEncoded();
      initVector = keyGen.generateKey().getEncoded();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Constructs an AES key given a key and an initialization vector
   *
   * @param key AES key
   * @param initVector AES initialization vector
   */
  public AES(byte[] key, byte[] initVector) {
    this.key = Arrays.copyOf(key, key.length);
    this.initVector = Arrays.copyOf(initVector, initVector.length);
  }

  /**
   * Copy constructor
   *
   * @param aes Original AES key
   */
  public AES(AES aes) {
    this(aes.getKey(), aes.getInitVector());
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //------------------------------------------------------------------------------------------------

  /**
   * @return AES key
   */
  public byte[] getKey() {
    return key;
  }

  /**
   * @return AES initialization vector
   */
  public byte[] getInitVector() {
    return initVector;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Encryption and decryption
  //------------------------------------------------------------------------------------------------

  /**
   * Encrypts a string with AES-128 encryption algorithm
   *
   * @param plainText Text to be encrypted
   * @return Encrypted test
   */
  public String encrypt(String plainText) {
    try {
      IvParameterSpec iv = new IvParameterSpec(initVector);
      SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

      byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
      return Base64.encodeBase64String(cipherBytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Decrypts a string encrypted with AES-128
   *
   * @param cipherText Encrypted string
   * @return Decrypted string
   */
  public String decrypt(String cipherText) {
    try {
      IvParameterSpec iv = new IvParameterSpec(initVector);
      SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

      byte[] plainBytes = cipher.doFinal(Base64.decodeBase64(cipherText));
      return new String(plainBytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
