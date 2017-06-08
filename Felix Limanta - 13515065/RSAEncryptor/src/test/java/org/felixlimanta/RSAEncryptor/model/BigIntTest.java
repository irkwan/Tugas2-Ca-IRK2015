package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 07/06/17.
 */
class BigIntTest {

  private org.felixlimanta.RSAEncryptor.model.BigInt b;

  private static SecureRandom random;

  private boolean getRandomBoolean() {
    return ThreadLocalRandom.current().nextBoolean();
  }

  private int getRandomInt() {
    return ThreadLocalRandom.current().nextInt();
  }

  @Test
  void intArrayConstructor() {
    int len = Math.abs(getRandomInt()) % 10 + 1;
    int[] digits = new int[len];
    for (int i = 0; i < len; ++i) {
      digits[i] = getRandomInt();
    }
    b = new BigInt(digits);
    System.out.println(b);
    System.out.println(b.toString(true));
  }

  @Test
  void stringConstructor() {
    StringBuilder s = new StringBuilder();
    if (getRandomBoolean()) {
      s.append("-");
    }
    for (int i = 0; i < getRandomInt() % 10; ++i) {
      s.append(Integer.toUnsignedLong(getRandomInt()));
    }
    if (s.toString().equals("") || s.toString().equals("-"))
      s.append(Integer.toUnsignedLong(getRandomInt()));
    System.out.println(s.toString());

    b = new BigInt(s.toString());
    System.out.println(b.toString());
    System.out.println(b.toString(true));
    assertEquals(s.toString(), b.toString(), "String value mismatch");
  }

  @Test
  void add() {
    BigInt b1 = new BigInt("9999999999");
    BigInt b2 = new BigInt("1111111111");
    BigInt b3 = new BigInt("11111111110");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Large + large addition value mismatch");

    b1 = new BigInt("12345678901234567890");
    b2 = new BigInt("111111111111");
    b3 = new BigInt("12345679012345679001");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Large + small addition value mismatch");

    b1 = new BigInt("111111111111");
    b2 = new BigInt("12345678901234567890");
    b3 = new BigInt("12345679012345679001");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Small + large addition value mismatch");

    b1 = new BigInt("-12345678901234567890");
    b2 = new BigInt("-111111111111");
    b3 = new BigInt("-12345679012345679001");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Neg + neg addition value mismatch");

    b1 = new BigInt("12345678901234567890");
    b2 = new BigInt("-111111111111");
    b3 = new BigInt("12345678790123456779");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Large pos + small neg addition value mismatch");

    b1 = new BigInt("111111111111");
    b2 = new BigInt("-12345678901234567890");
    b3 = new BigInt("-12345678790123456779");
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Small pos + large neg addition value mismatch");
  }

  @Test
  void subtract() {
    BigInt b1 = new BigInt("9999999999");
    BigInt b2 = new BigInt("1111111111");
    BigInt b3 = new BigInt("8888888888");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Large - large subtraction value mismatch");

    b1 = new BigInt("12345678901234567890");
    b2 = new BigInt("111111111111");
    b3 = new BigInt("12345678790123456779");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Large - small subtraction value mismatch");

    b1 = new BigInt("111111111111");
    b2 = new BigInt("12345678901234567890");
    b3 = new BigInt("-12345678790123456779");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Small - large subtraction value mismatch");

    b1 = new BigInt("-12345678901234567890");
    b2 = new BigInt("-111111111111");
    b3 = new BigInt("-12345678790123456779");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Neg - neg subtraction value mismatch");

    b1 = new BigInt("12345678901234567890");
    b2 = new BigInt("-111111111111");
    b3 = new BigInt("12345679012345679001");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Large pos - small neg subtraction value mismatch");

    b1 = new BigInt("111111111111");
    b2 = new BigInt("-12345678901234567890");
    b3 = new BigInt("12345679012345679001");
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Small pos - large neg subtraction value mismatch");
  }

  @Test
  void multiply() {
    BigInt b1 = new BigInt("11111");
    BigInt b2 = new BigInt("22222");
    BigInt b3 = new BigInt("246908642");
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "Small pos * small pos multiplication value mismatch");

    b1 = new BigInt("123456789987654321");
    b2 = new BigInt("987654321123456789");
    b3 = new BigInt("121932632103337905662094193112635269");
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "medium pos * medium pos multiplication value mismatch");

    b2 = BigInt.ZERO;
    b3 = BigInt.ZERO;
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "something * zero multiplication value mismatch");

    b1 = new BigInt("-123456789987654321");
    b2 = new BigInt("-987654321123456789");
    b3 = new BigInt("121932632103337905662094193112635269");
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "medium neg * medium neg multiplication value mismatch");

    b1 = new BigInt("123456789987654321");
    b2 = new BigInt("-987654321123456789");
    b3 = new BigInt("-121932632103337905662094193112635269");
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "medium pos * medium neg multiplication value mismatch");

    b1 = new BigInt("-123456789987654321");
    b2 = new BigInt("987654321123456789");
    b3 = new BigInt("-121932632103337905662094193112635269");
    assertEquals(b3.toString(), b1.multiply(b2).toString(),
        "medium neg * medium pos multiplication value mismatch");
  }

  void testWithJavaBigInteger() {
    BigInteger a = new BigInteger("123456789987654321");
    BigInteger b = new BigInteger("987654321123456789");
    BigInteger c = a.multiply(b);
    System.out.println(c);
  }
}