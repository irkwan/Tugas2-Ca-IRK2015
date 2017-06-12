package org.felixlimanta.RSAEncryptor.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 12/06/17.
 */
class AESTest {
  private static String[] messages;

  private static String readFile(String path) {
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, Charset.defaultCharset());
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @BeforeAll
  static void setUpMessages() {
    messages = new String[5];
    messages[0] = readFile("src/test/resources/test1.txt");
    messages[1] = readFile("src/test/resources/test2.txt");
    messages[2] = readFile("src/test/resources/test3.txt");
    messages[3] = readFile("src/test/resources/test4.txt");
    messages[4] = readFile("src/test/resources/test5.txt");
  }

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
                + "Key: " + aes.getKey() + "\n"
                + "Init Vector: " + aes.getInitVector());
      }
    }
  }

  private static String padLeftStringWithZeros(String s, int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; ++i)
      sb.append('0');
    sb.append(s);
    return sb.substring(s.length());
  }
}