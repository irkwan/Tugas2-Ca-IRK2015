package org.felixlimanta.RSAEncryptor.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests AES-128 encryption process.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-12
 */
class AESTest {
  private static String[] messages;

  /**
   * Reads entire file as string
   *
   * @param path File path
   * @return File contents as string
   */
  private static String readFile(String path) {
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, Charset.defaultCharset());
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * Reads texts to be encrypted from file
   */
  @BeforeAll
  static void setUpMessages() {
    messages = new String[6];
    messages[0] = readFile("src/test/resources/test1.txt");
    messages[1] = readFile("src/test/resources/test2.txt");
    messages[2] = readFile("src/test/resources/test3.txt");
    messages[3] = readFile("src/test/resources/test4.txt");
    messages[4] = readFile("src/test/resources/test5.txt");
    messages[5] = readFile("src/test/resources/test6.txt");
  }

  /**
   * Tests encryption process on large texts (3KB -- 9MB).
   * Message before and after encryption/decryption should be equal.
   */
  @Test
  void encryptDecrypt() {
    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      AES aes = new AES();
      for (String message : messages) {
        String encrypted = aes.encrypt(message);
        String decrypted = aes.decrypt(encrypted);
        assertEquals(message, decrypted,
            "Encryption-decryption failure\n"
                + "Key: " + new String(aes.getKey()) + "\n"
                + "Init Vector: " + new String(aes.getInitVector()));
      }
    }
  }
}