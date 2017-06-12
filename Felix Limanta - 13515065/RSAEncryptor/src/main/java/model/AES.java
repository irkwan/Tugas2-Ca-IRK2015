package org.felixlimanta.RSAEncryptor.model;

import java.security.SecureRandom;
import javax.crypto.Cipher;
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
    key = new byte[16];
    random.nextBytes(key);

    initVector = new byte[16];
    random.nextBytes(initVector);
  }

  public AES(byte[] key, byte[] initVector) {
    this.key = key;
    this.initVector = initVector;
  }

  public AES(String key, String initVector) {
    this.key = key.getBytes();
    this.initVector = initVector.getBytes();
  }

  public String getKey() {
    return new String(key);
  }

  public String getInitVector() {
    return new String(initVector);
  }

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
