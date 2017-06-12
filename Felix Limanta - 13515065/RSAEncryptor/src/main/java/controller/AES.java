package org.felixlimanta.RSAEncryptor.controller;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by ASUS on 12/06/17.
 */
public class AES {
  private static SecureRandom random;

  private byte[] key;
  private byte[] initVector;

  static {
    random = new SecureRandom();
  }

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

  public AES(byte[] key, byte[] initVector) {
    this.key = key;
    this.initVector = initVector;
  }

  public byte[] getKey() {
    return key;
  }

  public byte[] getInitVector() {
    return initVector;
  }

  public String encrypt(String plainText) {
    try {
      IvParameterSpec iv = new IvParameterSpec(initVector);
      SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

      byte[] cipherBytes = cipher.doFinal(plainText.getBytes());
      String cipherText = Base64.encodeBase64String(cipherBytes);
      return cipherText;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

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
}
