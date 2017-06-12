package org.felixlimanta.RSAEncryptor.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 10/06/17.
 */
class RSATest {

  private static String[] messages;

  private static final String n =
      "86468221314284253641269699516965004963310878720668294892080973630779878458113190898959535278476674868478369808967838022740732742788381128803652403208205595648027627154129744348408490564661211487162153402490810206180153476233951742673411340721231551690270500956247763595344890849123432636361058193538756577707";
  private static final String e = "65537";
  private static final String d =
      "27423977510842048828304794838181120751100878429718339922171431060356366078568010885916709958203410676087662780784915348912484557200175411641489800065370050615835584851730339393601216883301353362936557674602356544634963001542056703497691640265022907922100102030465651941501355942091729786247486623988199009013";

  private static SecureRandom random;
  private static RSA rsa;

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
  static void setUpAll() {
    random = new SecureRandom();
    System.out.println("Working Directory = " +
        System.getProperty("user.dir"));

    messages = new String[5];
    messages[0] = readFile("src/test/resources/test1.txt");
    messages[1] = readFile("src/test/resources/test2.txt");
    messages[2] = readFile("src/test/resources/test3.txt");
    messages[3] = readFile("src/test/resources/test4.txt");
    messages[4] = readFile("src/test/resources/test5.txt");
  }

  @Test
  void generateKeys() {
    rsa = new RSA();

    BigInteger p = new BigInteger(rsa.getP().toBinaryString(), 2);
    BigInteger q = new BigInteger(rsa.getQ().toBinaryString(), 2);
    assertAll(
        () -> assertTrue(p.isProbablePrime(100), "P not prime"),
        () -> assertTrue(q.isProbablePrime(100), "Q not prime")
    );

    BigInteger n = new BigInteger(rsa.getN().toBinaryString(), 2);
    assertEquals(p.multiply(q).toString(), n.toString(), "N != P * Q");

    BigInteger p2 = p.subtract(BigInteger.ONE);
    BigInteger q2 = q.subtract(BigInteger.ONE);
    BigInteger l = new BigInteger(rsa.getLambda().toBinaryString(), 2);
    assertEquals(p2.divide(p2.gcd(q2)).multiply(q2).toString(), l.toString(), "L != LCM(P-1, Q-1)");

    BigInteger e = new BigInteger(rsa.getE().toBinaryString(), 2);
    assertAll(
        () -> assertTrue(e.gcd(p).equals(BigInteger.ONE), "E not coprime with P"),
        () -> assertTrue(e.gcd(q).equals(BigInteger.ONE), "E not coprime with Q")
    );

    BigInteger d = new BigInteger(rsa.getD().toBinaryString(), 2);
    assertEquals(e.modInverse(l).toString(), d.toString(), "D not modular inverse of E");
  }

  @Test
  void encryptHelloWorld() {
    rsa = new RSA();

    String message = "hello world";
    String encrypted, decrypted;
    try {
      encrypted = rsa.encrypt(message);
      System.out.println("Encrypted: " + encrypted);
      decrypted = rsa.decrypt(encrypted);
      System.out.println("Decrypted: " + decrypted);
    } catch (Exception e) {
      decrypted = "";
      e.printStackTrace();
    }
    assertEquals(message, decrypted,
        "n = " + rsa.getN() + "\ne = " + rsa.getE() + "\nd = " + rsa.getD() + "\n");
  }

  @Test
  void encryptLongTexts() {
    final int k = 100;
    rsa = new RSA(n, e, d);
    try {
      for (int i = 0; i < k; ++i) {
        System.out.print(i + " ");
        for (int j = 0; j < messages.length; ++j) {
          String encrypted = rsa.encrypt(messages[j]);
          String key1 = Arrays.toString(rsa.getAes().getKey());
          String iv1 = Arrays.toString(rsa.getAes().getInitVector());

          String decrypted = rsa.decrypt(encrypted);
          assertEquals(messages[j], decrypted,
              "n = " + rsa.getN() + "\ne = " + rsa.getE() + "\nd = " + rsa.getD() + "\n"
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