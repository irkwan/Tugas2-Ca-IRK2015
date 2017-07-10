package org.felixlimanta.RSAEncryptor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.felixlimanta.RSAEncryptor.model.BigInt;
import org.felixlimanta.RSAEncryptor.model.PrivateKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests RSA-1024 encryption process.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-10
 */
class RSATest {

  private static String[] messages;

  // Default key values for quickly testing encryption/decryption
  private static final String n =
      "86468221314284253641269699516965004963310878720668294892080973630779878458113"
          + "19089895953527847667486847836980896783802274073274278838112880365240320820559"
          + "56480276271541297443484084905646612114871621534024908102061801534762339517426"
          + "73411340721231551690270500956247763595344890849123432636361058193538756577707";
  private static final String e = "65537";
  private static final String d =
      "27423977510842048828304794838181120751100878429718339922171431060356366078568"
          + "01088591670995820341067608766278078491534891248455720017541164148980006537005"
          + "06158355848517303393936012168833013533629365576746023565446349630015420567034"
          + "97691640265022907922100102030465651941501355942091729786247486623988199009013";

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
    System.out.println("Working Directory = " +
        System.getProperty("user.dir"));

    messages = new String[6];
    messages[0] = readFile("src/test/resources/test1.txt");
    messages[1] = readFile("src/test/resources/test2.txt");
    messages[2] = readFile("src/test/resources/test3.txt");
    messages[3] = readFile("src/test/resources/test4.txt");
    messages[4] = readFile("src/test/resources/test5.txt");
    messages[5] = readFile("src/test/resources/test6.txt");
  }

  /**
   * Generates keys, then encrypts "hello world".
   * Message before and after encryption/decryption should be equal.
   */
  @Test
  void encryptHelloWorld() {
    RSA rsa = new RSA();

    String message = "hello world";
    String encrypted, decrypted;
    try {
      String[] s = rsa.encrypt(message);
      String manifest = s[0];
      encrypted = s[1];
      System.out.println("Manifest: " + manifest);
      System.out.println("Encrypted: " + encrypted);
      decrypted = rsa.decrypt(manifest, encrypted);
      System.out.println("Decrypted: " + decrypted);
    } catch (Exception e) {
      decrypted = "";
      e.printStackTrace();
    }
    assertEquals(message, decrypted,
        "n = " + rsa.getPrivateKey().getN() +
            "\ne = " + rsa.getPrivateKey().getE() +
            "\nd = " + rsa.getPrivateKey().getD() + "\n");
  }

  /**
   * Tests encryption process on large texts (3KB -- 9MB).
   * Message before and after encryption/decryption should be equal.
   */
  @Test
  void encryptLongTexts() {
    final int k = 100;
    PrivateKey key = new PrivateKey(new BigInt(n), new BigInt(e), new BigInt(d));
    RSA rsa = new RSA();
    rsa.setPrivateKey(key);
    rsa.setPublicKey(key.getPublicKey());

    try {
      for (int i = 0; i < k; ++i) {
        System.out.print(i + " ");
        for (int j = 0; j < messages.length; ++j) {
          String[] encrypted = rsa.encrypt(messages[j]);
          String key1 = Arrays.toString(rsa.getAes().getKey());
          String iv1 = Arrays.toString(rsa.getAes().getInitVector());

          String decrypted = rsa.decrypt(encrypted[0], encrypted[1]);
          assertEquals(messages[j], decrypted,
              "n = " + rsa.getPrivateKey().getN() +
                  "\ne = " + rsa.getPrivateKey().getE() +
                  "\nd = " + rsa.getPrivateKey().getD() + "\n"
                  + "Key1 = " + key1 + "\nIV2 = " + iv1 + "\n"
                  + "Key2 = " + Arrays.toString(rsa.getAes().getKey()) + "\nIV2 = " + Arrays
                  .toString(rsa.getAes().getInitVector()));
          System.out.print(j + " ");
        }
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}