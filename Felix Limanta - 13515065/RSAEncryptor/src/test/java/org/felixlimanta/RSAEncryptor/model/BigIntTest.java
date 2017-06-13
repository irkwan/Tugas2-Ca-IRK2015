package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 07/06/17.
 */
class BigIntTest {

  private org.felixlimanta.RSAEncryptor.model.BigInt b;

  private static SecureRandom random;
  static int n;

  @BeforeAll
  static void setUpAll() {
    random = new SecureRandom();
  }

  private boolean getRandomBoolean() {
    return random.nextBoolean();
  }

  private int getRandomInt() {
    return random.nextInt();
  }

  private String getRandomPosBigInt(int n) {
    byte[] b = new byte[n / 8];
    random.nextBytes(b);
    BigInteger b2 = new BigInteger(1, b);
    return b2.toString();
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
    final int k = 1000000;
    for (int i = 0; i < k; ++i) {
      StringBuilder s = new StringBuilder();
      if (getRandomBoolean()) {
        s.append("-");
      }
      for (int j = 0; j < getRandomInt() % 10; ++i) {
        s.append(Integer.toUnsignedLong(getRandomInt()));
      }
      if (s.toString().equals("") || s.toString().equals("-"))
        s.append(Integer.toUnsignedLong(getRandomInt()));

      b = new BigInt(s.toString());
      assertEquals(s.toString(), b.toString(), "String value mismatch");
    }
  }

  @Test
  void toStringTest() {
    int k = 100;
    for (int i = 0; i < k; ++i) {
      BigInteger exp = BigInteger.probablePrime(1024, random);
      BigInt act = new BigInt(exp.toString());
      assertEquals(exp.toString(), act.toString(), "toString mismatch");
    }
  }

  @Test
  void toBinaryString() {
    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      StringBuilder s = new StringBuilder();
      if (getRandomBoolean()) {
        s.append("-");
      }
      for (int j = 0; j < getRandomInt() % 10; ++i) {
        s.append(Integer.toUnsignedLong(getRandomInt()));
      }
      if (s.toString().equals("") || s.toString().equals("-"))
        s.append(Integer.toUnsignedLong(getRandomInt()));

      BigInt b1 = new BigInt(s.toString());
      BigInteger b2 = new BigInteger(s.toString());
      assertEquals(b1.toBinaryString(), b2.toString(2), "String value mismatch");
    }
  }

  @Test
  void isOdd() {
    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      String s = getRandomPosBigInt(1024);
      BigInteger exp = new BigInteger(s);
      BigInt act = new BigInt(s);

      assertEquals(exp.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE), act.isOdd(),
          "Different oddness: " + s);
    }
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

    for (int i = 0; i < 10000; ++i) {
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

    for (int i = 0; i < 10000; ++i) {
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

    final int max = 1 << 16;
    for (int i = 0; i < 10000; ++i) {
      String s1 = getRandomPosBigInt(10);
      n = Math.min(max, Math.abs(getRandomInt()));
      BigInteger exp = new BigInteger(s1).shiftLeft(n);
      BigInt act = new BigInt(s1).shiftLeft(n);
      assertEquals(exp.toString(2), act.toBinaryString(),
          s1 + " << " + n + " shift mismatch");
    }
  }

  @Test
  void shiftRight() {
    int n = 16;
    BigInt b1 = new BigInt("65536");
    BigInt b2 = b1.shiftRight(n);
    BigInt b3 = new BigInt("1");
    assertEquals(b3.toBinaryString(), b2.toBinaryString(),
        "Single word right shift mismatch");

    n = 10;
    b1 = new BigInt("1024000000000000000000");
    b2 = b1.shiftRight(n);
    b3 = new BigInt("1000000000000000000");
    assertEquals(b3.toBinaryString(), b2.toBinaryString(),
        "Multi word right shift mismatch");

    n = 100;
    b1 = new BigInt(
        "156500072834599941898774713720503211384503211228003138549903269485728497664");
    b2 = b1.shiftRight(n);
    b3 = new BigInt("123456789123456789123456789123456789123456789");
    assertEquals(b3.toBinaryString(), b2.toBinaryString(),
        "Large word left shift mismatch");

    for (int i = 0; i < 10000; ++i) {
      String s1 = getRandomPosBigInt(100);
      n = Math.abs(getRandomInt());
      BigInteger exp = new BigInteger(s1).shiftRight(n);
      BigInt act = new BigInt(s1).shiftRight(n);
      assertEquals(exp.toString(2), act.toBinaryString(),
          s1 + " >> " + n + " shift mismatch");
    }
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

    for (int i = 0; i < 10000; ++i) {
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

    for (int i = 0; i < 10000; ++i) {
      String s1 = getRandomPosBigInt(100);
      String s2 = getRandomPosBigInt(100);
      b1 = new BigInt(s1);
      b2 = new BigInt(s2);
      b3 = new BigInt(new BigInteger(s1).divide(new BigInteger(s2)).toString(10));
      assertEquals(b3.toString(), b1.divide(b2).toString(),"TC." + i);
    }
  }

  @Test
  void remainder() {
    BigInt b1 = new BigInt("1000");
    BigInt b2 = new BigInt("100");
    BigInt b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC1");

    b1 = new BigInt("17179869184");
    b2 = new BigInt("8589934592");
    b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC2");

    b1 = new BigInt("231584178474632390847141970017375815706539969331281128078915168015826259279872");
    b2 = new BigInt("158456325028528675187087900672");
    b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC3");

    b1 = new BigInt("123456789123456789");
    b2 = new BigInt("98765432198765");
    b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toBinaryString(), b1.remainder(b2).toBinaryString(),"TC4");

    b1 = new BigInt("123456789012345678901234567890");
    b2 = new BigInt("98765432109876543210");
    b3 = new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toBinaryString(), b1.remainder(b2).toBinaryString(),"TC5");

    b1 = new BigInt("123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC6");

    b1 = new BigInt("123456789123456789123456789123456789123456789");
    b2 = new BigInt("987654321987654321987654321987654321987654321987654321987654321987654321");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC7");

    b1 = new BigInt("123456789123456789123456789123456789123456789");
    b2 = new BigInt("123456789123456789123456789123456789123456789");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC8");

    b1 = new BigInt("1826821235914201983");
    b2 = new BigInt("-4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC9");

    b1 = new BigInt("-1826821235914201983");
    b2 = new BigInt("4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC10");

    b1 = new BigInt("-1826821235914201983");
    b2 = new BigInt("-4150004966");
    b3 =  new BigInt(new BigInteger(b1.toBinaryString(), 2).remainder(new BigInteger(b2.toBinaryString(), 2)).toString(10));
    assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC10");

    for (int i = 0; i < 10000; ++i) {
      String s1 = getRandomPosBigInt(100);
      String s2 = getRandomPosBigInt(100);
      b1 = new BigInt(s1);
      b2 = new BigInt(s2);
      b3 = new BigInt(new BigInteger(s1).remainder(new BigInteger(s2)).toString(10));
      assertEquals(b3.toString(), b1.remainder(b2).toString(),"TC." + i);
    }
  }

  @Test
  void modExp() {
    assertEquals(BigInt.ONE.toBinaryString(),
        new BigInt(5).modExp(new BigInt(117), new BigInt(19)).toBinaryString(),
        "5 ^ 117 % 19 == 1");

    final int k = 10000;
    int n = 0;
    for (int i = 0; i < k; ++i) {
      System.out.print(i + ". ");
      String s1 = getRandomPosBigInt(50);
      String s2 = getRandomPosBigInt(10);
      String s3 = getRandomPosBigInt(30);

      BigInteger exp = new BigInteger(s1).modPow(new BigInteger(s2),new BigInteger(s3));
      BigInt act = new BigInt(s1).modExp(new BigInt(s2), new BigInt(s3));
      assertEquals(exp.toString(2), act.toBinaryString(),
      s1 + " ^ " + s2 + " % " + s3 + " mismatch");
      if (exp.toString(2).equals(act.toBinaryString())) {
        System.out.println("v   " + s1 + " ^ " + s2 + " % " + s3);
        n++;
      } else {
        System.out.println("    " + s1 + " ^ " + s2 + " % " + s3);
      }
    }
    assertTrue(n >= k * 0.95,
        "Multi word test: n = " + n + ", k = " + k + ", error = " + (100 - (n * 100 / k)) + "%");
  }

  @Test
  void gcd() {
    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      String s1 = BigInteger.probablePrime(1024, random).subtract(BigInteger.ONE).toString();
      String s2 = BigInteger.probablePrime(1024, random).subtract(BigInteger.ONE).toString();
      BigInteger exp = new BigInteger(s1).gcd(new BigInteger(s2));
      BigInt act = new BigInt(s1).gcd(new BigInt(s2));
      assertEquals(exp.toString(), act.toString(),
          "GCD(" + s1 + ", " + s2 + ") mismatch");
      //System.out.println(i + ". GCD(" + s1 + ", " + s2 + ")");
    }
  }

  @Test
  void lcm() {
    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      String s1 = getRandomPosBigInt(1024);
      String s2 = getRandomPosBigInt(1024);

      BigInteger exp = new BigInteger(s1).
          divide(new BigInteger(s1).gcd(new BigInteger(s2))).multiply(new BigInteger(s2));
      BigInt act = new BigInt(s1).lcm(new BigInt(s2));
      assertEquals(exp.toString(), act.toString(),
          "LCM(" + s1 + ", " + s2 + ") mismatch");
      //System.out.println(i + ". GCD(" + s1 + ", " + s2 + ")");
    }
  }

  @Test
  void modInverse() {
    BigInteger exp = new BigInteger("10").modInverse(new BigInteger("17"));
    BigInt act = new BigInt("10").modInverse(new BigInt("17"));
    assertEquals(exp.toString(2), act.toBinaryString(),
        "Different modInverse for 10 % 17");

    final int k = 10000;
    for (int i = 0; i < k; ++i) {
      System.out.println(i);
      String s1 = getRandomPosBigInt(128);
      String s2 = new BigInteger(s1).nextProbablePrime().toString();
      exp = new BigInteger(s1).modInverse(new BigInteger(s2));
      act = new BigInt(s1).modInverse(new BigInt(s2));
      assertEquals(exp.toString(2), act.toBinaryString(),
          "Different modInverse for " + s1 + " % " + s2);
    }
  }

  @Test
  void byteArrayOperation() {
    final int k = 100;
    for (int i = 0; i < k; ++i) {
      byte[] bytes = new byte[16];
      random.nextBytes(bytes);

      String e = getRandomPosBigInt(1024);
      String m = getRandomPosBigInt(1024);

      BigInteger exp = new BigInteger(1, bytes).modPow(new BigInteger(e), new BigInteger(m));
      BigInt act = new BigInt(bytes, (byte) 1).modExp(new BigInt(e), new BigInt(m));
      assertEquals(exp.toString(2), act.toBinaryString());
    }
  }

  @Test
  void primeValidation() {
    final int k = 100;
    for (int i = 0; i < k; ++i) {
      BigInteger b1 = BigInteger.probablePrime(1024, random);
      String s = b1.toString();
      BigInt b2 = new BigInt(s);
      boolean exp = b1.isProbablePrime(100);
      boolean act = b2.isProbablePrime(100, random);
      assertEquals(exp, act,b1.toString() + " primality test mismatch");
      System.out.println(i + ". " + s);
    }
  }

  @Test
  void primeGeneration() {
    final int k = 1000;
    for (int i = 0; i < k; ++i) {
      BigInt b1 = BigInt.probablePrime(1024, random);
      BigInteger b2 = new BigInteger(b1.toBinaryString(), 2);
      String s = b2.toString();
      assertTrue(b2.isProbablePrime(100), "Composite number generated");
      System.out.println(i + ". " + s);
    }
  }

  @Test
  void nextProbablePrime() {
    final int k = 100;
    for (int i = 0; i < k; ++i) {
      BigInt b1 = BigInt.probablePrime(128, random);
      BigInteger exp = new BigInteger(b1.toBinaryString(), 2).nextProbablePrime();
      BigInt act = b1.nextProbablePrime();
      assertEquals(exp.toString(2), act.toBinaryString(), "Diffrrent next primes");
    }
  }

  @Test
  void testWithJavaBigInteger() {
    BigInteger a = new BigInteger("-5");
    BigInteger b = new BigInteger("2");
    BigInteger c = a.divide(b);
    BigInteger d = a.remainder(b);
    System.out.println(a + " = " + b + " * " + c + " + " + d);

    if (d.compareTo(BigInteger.ZERO) == -1) {
      c = c.subtract(BigInteger.ONE);
      d = b.abs().add(d);
      System.out.println(b.multiply(c).add(d) + " = " + b + " * " + c + " + " + d);
    }
  }
}