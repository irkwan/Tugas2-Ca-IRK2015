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

  private String getRandomPosBigInt(int n) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < getRandomInt() % n && s.length() < n; ++i) {
      s.append(Integer.toUnsignedLong(getRandomInt()));
    }
    if (s.toString().equals(""))
      s.append(Integer.toUnsignedLong(getRandomInt()));
    return s.toString();
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
  void toBinaryString() {
    StringBuilder s = new StringBuilder();
    if (getRandomBoolean()) {
      s.append("-");
    }
    for (int i = 0; i < getRandomInt() % 10; ++i) {
      s.append(Integer.toUnsignedLong(getRandomInt()));
    }
    if (s.toString().equals("") || s.toString().equals("-"))
      s.append(Integer.toUnsignedLong(getRandomInt()));

    BigInt b1 = new BigInt(s.toString());
    BigInteger b2 = new BigInteger(s.toString());
    System.out.println(b1.toBinaryString());
    System.out.println(b2.toString(2));
    assertEquals(b1.toBinaryString(), b2.toString(2), "String value mismatch");
  }

  @Test
  void add() {
    BigInt b1 = new BigInt("99999999999999999999");
    BigInt b2 = new BigInt("11111111111111111111");
    BigInt b3 = new BigInt("111111111111111111110");
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

    b1 = new BigInt("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).add(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.add(b2).toString(),
        "Larger + larger addition value mismatch");

    for (int i = 0; i < 1000; ++i) {
      b1 = new BigInt(getRandomPosBigInt(100));
      b2 = new BigInt(getRandomPosBigInt(80));
      b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).add(new BigInteger(b2.toBinaryString(), 2)).toString(10));
      assertEquals(b3.toString(), b1.add(b2).toString(),"TC" + i);
    }
  }

  @Test
  void subtract() {
    BigInt b1 = new BigInt("9999999999");
    BigInt b2 = new BigInt("1111111111");
    BigInt b3 = new BigInt("8888888888");
    BigInt b4;
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

    b1 = new BigInt("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).subtract(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.subtract(b2).toString(),
        "Larger - larger subtraction value mismatch");

    for (int i = 0; i < 1000; ++i) {
      b1 = new BigInt(getRandomPosBigInt(100));
      b2 = new BigInt(getRandomPosBigInt(80));
      b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).subtract(new BigInteger(b2.toBinaryString(), 2)).toString(10));
      assertEquals(b3.toString(), b1.subtract(b2).toString(),"TC" + i);
    }
  }

  @Test
  void shiftLeft() {
    int n = 16;
    BigInt b1 = new BigInt("1");
    BigInt b2 = b1.shiftLeft(n);
    BigInt b3 = new BigInt("65536");
    assertEquals(b3.toString(), b2.toString(),
        "Single word left shift mismatch");

    n = 10;
    b1 = new BigInt("1000000000000000000");
    b2 = b1.shiftLeft(n);
    b3 = new BigInt("1024000000000000000000");
    assertEquals(b3.toString(), b2.toString(),
        "Multi word left shift mismatch");

    n = 100;
    b1 = new BigInt("123456789123456789123456789123456789123456789");
    b2 = b1.shiftLeft(n);
    b3 = new BigInt(
        "156500072834599941898774713720503211384503211228003138549903269485728497664");
    assertEquals(b3.toString(), b2.toString(),
        "Large word left shift mismatch");
  }

  @Test
  void shiftRight() {
    int n = 16;
    BigInt b1 = new BigInt("65536");
    BigInt b2 = b1.shiftRight(n);
    BigInt b3 = new BigInt("1");
    assertEquals(b3.toString(), b2.toString(),
        "Single word right shift mismatch");

    n = 10;
    b1 = new BigInt("1024000000000000000000");
    b2 = b1.shiftRight(n);
    b3 = new BigInt("1000000000000000000");
    assertEquals(b3.toString(), b2.toString(),
        "Multi word right shift mismatch");

    n = 100;
    b1 = new BigInt(
        "156500072834599941898774713720503211384503211228003138549903269485728497664");
    b2 = b1.shiftRight(n);
    b3 = new BigInt("123456789123456789123456789123456789123456789");
    assertEquals(b3.toString(), b2.toString(),
        "Large word left shift mismatch");
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

    for (int i = 0; i < 1000; ++i) {
      b1 = new BigInt(getRandomPosBigInt(100));
      b2 = new BigInt(getRandomPosBigInt(80));
      b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).multiply(new BigInteger(b2.toBinaryString(), 2)).toString(10));
      assertEquals(b3.toString(), b1.multiply(b2).toString(),"TC" + i);
    }
  }

  @Test
  void divide() {
    BigInt b1 = new BigInt("1000");
    BigInt b2 = new BigInt("100");
    BigInt b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC1");

    b1 = new BigInt("17179869184");
    b2 = new BigInt("8589934592");
    b3 = new BigInt("2");
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC2");

    b1 = new BigInt("231584178474632390847141970017375815706539969331281128078915168015826259279872");
    b2 = new BigInt("158456325028528675187087900672");
    b3 = new BigInt("1461501637330902918203684832716283019655932542976");
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC3");

    b1 = new BigInt("123456789123456789");
    b2 = new BigInt("98765432198765");
    b3 = new BigInt("1249");
    assertEquals(b3.toBinaryString(), b1.divide(b2).toBinaryString(),"TC4");

    b1 = new BigInt("123456789012345678901234567890");
    b2 = new BigInt("98765432109876543210");
    b3 = new BigInt("1249999988");
    assertEquals(b3.toBinaryString(), b1.divide(b2).toBinaryString(),"TC5");

    b1 = new BigInt("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC6");

    b1 = new BigInt("123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC7");

    b1 = new BigInt("123456789123456789123456789123456789123456789");
    b2 = new BigInt("123456789123456789123456789123456789123456789");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC8");

    b1 = new BigInt("1826821235914201983");
    b2 = new BigInt("-4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC9");

    b1 = new BigInt("-1826821235914201983");
    b2 = new BigInt("4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC10");

    b1 = new BigInt("-1826821235914201983");
    b2 = new BigInt("-4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).divide(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.divide(b2).toString(),"TC10");

    for (int i = 0; i < 1000; ++i) {
      String s1 = getRandomPosBigInt(100);
      String s2 = getRandomPosBigInt(100);
      b1 = new BigInt(s1);
      b2 = new BigInt(s2);
      b3 = new BigInt(new BigInteger(s1).divide(new BigInteger(s2)).toString(10));
      assertEquals(b3.toString(), b1.divide(b2).toString(),"TC." + i);
    }
  }

  @Test
  void testWithJavaBigInteger() {
    BigInteger a = new BigInteger("1826821235914201983");
    BigInteger b = new BigInteger("4150004966");
    BigInteger c = a.divide(b);
    System.out.println(a);
    System.out.println(b);
    System.out.println(c);
  }
}