package com.endec.model;

import java.security.SecureRandom;
import java.util.ArrayList;

/*
 * File name          : BigInteger.java
 * Created on         : 18/06/17
 * Modified on        : 19/06/17
 */

/**
 * Represents an immutable arbitrary-precision integer.
 *
 * @author Jordhy Fernando
 * @version 1.0
 */
public class BigInteger implements Comparable<BigInteger> {

  private ArrayList<Integer> digits; //Digits of this BigInteger in little-endian order.
  private int sign; //Sign of this BigInteger. -1 for negative, 0 for zero, and 1 for positive.
  private static final int BASE = 10; //Base number used to represents the BigInteger.
  private static BigInteger ONE = new BigInteger(1); //BigInteger representation for value 1.
  private static BigInteger TWO = new BigInteger(2); //BigInteger representation for value 2.
  private static BigInteger THREE = new BigInteger(3); //BigInteger representation for value 3.

  /**
   * Return the minimum integer between a and b.
   * @param a integer to be compared.
   * @param b integer to be compared.
   * @return min between a and b.
   */
  private static int min(int a, int b) {
    return (a < b ? a : b);
  }

  /**
   * Return the maximum integer between a and b.
   * @param a integer to be compared.
   * @param b integer to be compared.
   * @return maximal between a and b.
   */
  private static int max(int a, int b) {
    return (a > b ? a : b);
  }

  /**
   * Creates a BigInteger with value set to 0.
   */
  public BigInteger() {
    digits = new ArrayList<>();
    sign = 0;
  }

  /**
   * Creates a BigInteger from the specified integer.
   * @param val value of the specified integer.
   */
  public BigInteger(int val) {
    digits = new ArrayList<>();
    if (val < 0) {
      sign = -1;
      val = -val;
    } else if (val == 0) {
      sign = 0;
    } else {
      sign = 1;
    }
    while (val != 0) {
      digits.add(val % BASE);
      val /= BASE;
    }
  }

  /**
   * Creates a BigInteger from the specified decimal String representation of a BigInteger.
   * @param val decimal String representation of BigInteger.
   */
  public BigInteger(String val) {
    digits = new ArrayList<>();
    if (val.length() == 1 && val.charAt(0) == '0') {
      sign = 0;
    } else {
      int limit = 0;
      if (val.charAt(0) == '-') {
        sign = -1;
        limit = 1;
      } else {
        sign = 1;
      }
      for (int i = val.length() - 1; i >= limit; i--) {
        digits.add(val.charAt(i) - '0');
      }
    }
  }

  /**
   * Copy Constructor.
   * @param b BigInteger to be copied.
   */
  public BigInteger(BigInteger b) {
    digits = new ArrayList<>(b.digits);
    sign = b.sign;
  }

  /**
   * Returns a copy of this BigInteger.
   * @return copy of this BigInteger.
   */
  @Override
  public BigInteger clone() {
    return new BigInteger(this);
  }

  /**
   * Returns a BigInteger whose value is (-b).
   * @return -b
   */
  public static BigInteger negate(BigInteger b) {
    BigInteger result = new BigInteger(b);
    result.sign = - result.sign;
    return result;
  }

  /**
   * Returns a BigInteger whose value is (-this).
   * @return -this
   */
  public BigInteger negate() {
    return negate(this);
  }

  /**
   * Compares this BigInteger with the specified BigInteger.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return -1, 0, or 1 if this BigInteger is less than, equal to, or greater than the specified
   * BigInteger.
   */
  @Override
  public int compareTo(BigInteger b) {
    if (sign > b.sign) {
      return 1;
    }
    if (sign < b.sign) {
      return -1;
    }
    if (digits.size() < b.digits.size()) {
      if (sign == 1) {
        return -1;
      } else {
        return 1;
      }
    }
    if (digits.size() > b.digits.size()) {
      if (sign == 1) {
        return 1;
      } else {
        return -1;
      }
    }
    boolean found = false;
    int i = digits.size() - 1;
    while (i >= 0 && !found) {
      if (!digits.get(i).equals(b.digits.get(i))) {
        found = true;
      } else {
        i--;
      }
    }
    if (!found) {
      return 0;
    }
    if (sign == 1) {
      if (digits.get(i) > b.digits.get(i)) {
        return 1;
      } else {
        return -1;
      }
    } else {
      if (digits.get(i) > b.digits.get(i)) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  /**
   * Checks whether this BigInteger is less than the specified BigInteger or not.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return true if this BigInteger is less than the specified BigInteger. Otherwise false.
   */
  public boolean isLessThan(BigInteger b) {
    return (this.compareTo(b) == -1);
  }

  /**
   * Checks whether this BigInteger is greater than the specified BigInteger or not.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return true if this BigInteger is greater than the specified BigInteger. Otherwise false.
   */
  public boolean isGreaterThan(BigInteger b) {
    return (this.compareTo(b) == 1);
  }

  /**
   * Checks whether this BigInteger equals the specified BigInteger or not.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return true if this BigInteger equals the specified BigInteger. Otherwise false.
   */
  public boolean equals(BigInteger b) {
    return (this.compareTo(b) == 0);
  }

  /**
   * Checks whether this BigInteger is less than or equals the specified BigInteger or not.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return true if this BigInteger is less than or equals the specified BigInteger.
   * Otherwise false.
   */
  public boolean isLessThanOrEquals(BigInteger b) {
    return (this.compareTo(b) <= 0);
  }

  /**
   * Checks whether this BigInteger is greater than or equals the specified BigInteger or not.
   * @param b BigInteger which this BigInteger is to be compared with.
   * @return true if this BigInteger is greater than or equals the specified BigInteger.
   * Otherwise false.
   */
  public boolean isGreaterThanOrEquals(BigInteger b) {
    return (this.compareTo(b) >= 0);
  }

  /**
   * Returns a BigInteger whose value is (b1 + b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 + b2
   */
  public static BigInteger add(BigInteger b1, BigInteger b2) {
    if (b1.sign == 0) {
      return b2.clone();
    }
    if (b2.sign == 0) {
      return b1.clone();
    }
    if (b1.sign == -1 && b2.sign == 1) {
      return subtract(b2, negate(b1));
    }
    if (b1.sign == 1 && b2.sign == -1) {
      return subtract(b1, negate(b2));
    }
    BigInteger result = new BigInteger();
    result.sign = b1.sign;
    int carry = 0;
    for (int i = 0 ; i < min(b1.digits.size(), b2.digits.size()); i++) {
      result.digits.add((b1.digits.get(i) + b2.digits.get(i) + carry) % BASE);
      carry = (b1.digits.get(i) + b2.digits.get(i) + carry) / BASE;
    }
    if (b1.digits.size() > b2.digits.size()) {
      for (int i = b2.digits.size(); i < b1.digits.size(); i++) {
        result.digits.add((b1.digits.get(i) + carry) % BASE);
        carry = (b1.digits.get(i) + carry) / BASE;
      }
    } else if (b1.digits.size() < b2.digits.size()) {
      for (int i = b1.digits.size(); i < b2.digits.size(); i++) {
        result.digits.add((b2.digits.get(i) + carry) % BASE);
        carry = (b2.digits.get(i) + carry) / BASE;
      }
    }
    if (carry == 1){
      result.digits.add(carry);
    }
    return result;
  }

  /**
   * Returns a BigInteger whose value is (this + val).
   * @param val value to be added to this BigInteger.
   * @return this + val
   */
  public BigInteger add(BigInteger val) {
    return add(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b1 - b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 - b2
   */
  public static BigInteger subtract(BigInteger b1, BigInteger b2) {
    if (b2.sign == 0) {
      return b1.clone();
    }
    if (b1.sign == 0) {
      return negate(b2.clone());
    }
    if (b1.sign == -1 && b2.sign == 1) {
      return add(b1, negate(b2));
    }
    if (b1.sign == 1 && b2.sign == -1) {
      return add(b1, negate(b2));
    }
    if (b1.sign == -1 && b2.sign == -1) {
      return subtract(negate(b2), negate(b1));
    }
    if (b1.isLessThan(b2)) {
      return negate(subtract(b2, b1));
    }
    BigInteger result = new BigInteger();
    ArrayList<Integer> digits = new ArrayList<>(b1.digits);
    for (int i = 0; i < b2.digits.size(); i++) {
      int temp = digits.get(i) - b2.digits.get(i);
      if (temp < 0) {
        temp += BASE;
        digits.set(i + 1, b1.digits.get(i + 1) - 1);
      }
      result.digits.add(temp);
    }
    if (b1.digits.size() > b2.digits.size()) {
      for (int i = b2.digits.size(); i < b1.digits.size(); i++) {
        int temp = digits.get(i);
        if (temp < 0) {
          temp += BASE;
          digits.set(i + 1, b1.digits.get(i + 1) - 1);
        }
        result.digits.add(temp);
      }
    }
    //Remove trailing zeros
    while(result.digits.size() > 0 && result.digits.get(result.digits.size() - 1) == 0) {
      result.digits.remove(result.digits.size() - 1);
    }
    if (result.digits.size() == 0) {
      result.sign = 0;
    } else {
      result.sign = 1;
    }
    return result;
  }

  /**
   * Returns a BigInteger whose value is (this - val).
   * @param val value to be subtracted from this BigInteger.
   * @return this - val
   */
  public BigInteger subtract(BigInteger val) {
    return subtract(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b1 * b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 * b2
   */
  public static BigInteger multiply(BigInteger b1, BigInteger b2) {
    if (b1.sign == 0 || b2.sign == 0) {
      return new BigInteger();
    }
    if (b1.sign == -1 && b2.sign == 1) {
      return negate(karatsuba(negate(b1), b2));
    }
    if (b1.sign == 1 && b2.sign == -1) {
      return negate(karatsuba(b1, negate(b2)));
    }
    if (b1.sign == -1 && b2.sign == -1) {
      return karatsuba(negate(b1), negate(b2));
    }
    return karatsuba(b1, b2);
  }

  /**
   * Returns a BigInteger whose value is (this * val).
   * @param val value to be multiplied by this BigInteger.
   * @return this * val
   */
  public BigInteger multiply(BigInteger val) {
    return multiply(this, val);
  }

  /**
   * Karatsuba algorithm (multiplication algorithm), returns (b1 * b2).
   *
   * <p>Both b1 and b2 are positive BigInteger.
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 * b2
   */
  private static BigInteger karatsuba(BigInteger b1, BigInteger b2) {
    if (b1.sign == 0 || b2.sign == 0) {
      return new BigInteger();
    }
    if (b1.digits.size() == 1 && b2.digits.size() == 1) {
      return new BigInteger(b1.digits.get(0) * b2.digits.get(0));
    }
    int m = max(b1.digits.size(), b2.digits.size()) / 2;
    BigInteger[] x = b1.split(m);
    BigInteger[] y = b2.split(m);
    BigInteger z0 = karatsuba(x[0], y[0]);
    BigInteger z2 = karatsuba(x[1], y[1]);
    BigInteger z1 = karatsuba(add(x[1], x[0]), add(y[1], y[0])).subtract(z2).subtract(z0);
    return (add(add(z2.multiplyByBasePow(2 * m), z1.multiplyByBasePow(m)), z0));
  }

  /**
   * Split this BigInteger into the form (x<sub>1</sub> * 10<sup>index</sup> + x<sub>0</sub>) and
   * returns x<sub>1</sub> and x<sub>0</sub>.
   * @param index index / position to split this BigInteger.
   * @return x<sub>1</sub> and x<sub>0</sub> in the form of an array (index 0 for x<sub>0</sub>
   * and index 1 for x<sub>1</sub>).
   */
  private BigInteger[] split(int index) {
    BigInteger[] result = new BigInteger[2];
    for (int i = 0; i < 2; i++) {
      result[i] = new BigInteger();
    }
    for (int i = 0; i < min(index, digits.size()); i++) {
      result[0].digits.add(digits.get(i));
    }
    for (int i = index; i < digits.size(); i++) {
      result[1].digits.add(digits.get(i));
    }
    for (int i = 0; i < 2; i++) {
      if (result[i].digits.size() > 0) {
        result[i].sign = sign;
      }
    }
    return result;
  }

  /**
   * Returns a BigInteger whose value is (this * 10<sup>exp</sup>).
   * @param exponent exponent to which the base of this BigInteger is to be raised.
   * @return this * 10<sup>exp</sup>
   */
  private BigInteger multiplyByBasePow(int exponent) {
    if (digits.size() == 0) {
      return new BigInteger();
    }
    BigInteger result = new BigInteger();
    result.sign = sign;
    for (int i = 0; i < exponent; i++) {
      result.digits.add(0);
    }
    result.digits.addAll(digits);
    return result;
  }

  /**
   * Returns an array of two BigIntegers containing the quotient (b1 / b2)
   * followed by the remainder (b1 % b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return an array of two BigIntegers containing the quotient (b1 / b2)
   * followed by the remainder (b1 % b2).
   * @throws ArithmeticException if val is zero.
   */
  public static BigInteger[] divideAndMod(BigInteger b1, BigInteger b2) throws ArithmeticException {
    if (b2.sign == 0) {
      throw new ArithmeticException("division by zero");
    }
    BigInteger[] results = new BigInteger[2];
    for (int i = 0; i < 2; i++) {
      results[i] = new BigInteger();
    }
    if (b1.sign == 0) {
      return results;
    }
    for (int i = b1.digits.size() - 1; i >= max(b1.digits.size() - b2.digits.size(), 0); i--) {
      results[1].digits.add(0, b1.digits.get(i));
    }
    results[1].sign = 1;
    int pointer = b1.digits.size() - b2.digits.size();
    if (pointer >= 0) {
      BigInteger temp;
      if (b2.sign == -1) {
        temp = negate(b2.clone());
      } else {
        temp = b2.clone();
      }
      while (pointer >= 0) {
        int multiplier = 1;
        BigInteger multiplyResult = temp.multiply(new BigInteger(multiplier));
        BigInteger prev = new BigInteger();
        while (results[1].isGreaterThanOrEquals(multiplyResult)) {
          multiplier++;
          prev = multiplyResult;
          multiplyResult = temp.multiply(new BigInteger(multiplier));
        }
        if (multiplier > 1 || (multiplier == 1 && results[0].digits.size() != 0)) {
          results[0] = results[0].multiplyByBasePow(1);
          results[0] = results[0].add(new BigInteger(multiplier - 1));
        }
        results[1] = results[1].subtract(prev);
        pointer--;
        if (pointer >= 0) {
          results[1] = results[1].multiplyByBasePow(1);
          results[1] = results[1].add(new BigInteger(b1.digits.get(pointer)));
        }
      }
    }
    if (results[0].sign == 1 &&
        ((b1.sign == -1 && b2.sign == 1) || (b1.sign == 1 && b2.sign == -1))) {
      results[0].sign = -1;
    }

    if (results[1].sign == 1 &&
        ((b1.sign == -1 && b2.sign == 1) || (b1.sign == -1 && b2.sign == -1))) {
      results[1].sign = -1;
    }
    return results;
  }

  /**
   * Returns an array of two BigIntegers containing the quotient (this / val)
   * followed by the remainder (this % val).
   * @param val value used to divide this BigInteger.
   * @return an array of two BigIntegers containing the quotient (this / val)
   * followed by the remainder (this % val).
   * @throws ArithmeticException if val is zero.
   */
  public BigInteger[] divideAndMod(BigInteger val) throws ArithmeticException {
    return divideAndMod(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b1 / b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 / b2
   * @throws ArithmeticException if val is zero.
   */
  public static BigInteger divide(BigInteger b1, BigInteger b2) throws ArithmeticException {
    return divideAndMod(b1, b2)[0];
  }

  /**
   * Returns a BigInteger whose value is (this / val).
   * @param val value used to divide this BigInteger.
   * @return this / val
   * @throws ArithmeticException if val is zero.
   */
  public BigInteger divide(BigInteger val) throws ArithmeticException {
    return divide(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b1 % b2).
   * @param b1 left hand side operand.
   * @param b2 right hand side operand.
   * @return b1 % b2
   * @throws ArithmeticException if val is zero.
   */
  public static BigInteger mod(BigInteger b1, BigInteger b2) throws ArithmeticException {
    if (b2.sign == 0) {
      throw new ArithmeticException("division by zero");
    }
    if (b2.equals(ONE)) {
      return new BigInteger();
    }
    if (b1.equals(ONE)) {
      return b1.clone();
    }
    return divideAndMod(b1, b2)[1];
  }

  /**
   * Returns a BigInteger whose value is (this % val).
   * @param val value used to divide this BigInteger.
   * @return this % val
   * @throws ArithmeticException if val is zero.
   */
  public BigInteger mod(BigInteger val) throws ArithmeticException {
    return mod(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b<sup>exponent</sup>).
   * @param b the specified BigInteger.
   * @param exponent exponent to which the specified BigInteger is to be raised.
   * @return b<sup>exponent</sup>
   * @throws ArithmeticException if exponent is negative (yields non-integer value) or 0<sup>0</sup>.
   */
  public static BigInteger pow(BigInteger b, BigInteger exponent) throws ArithmeticException {
    if (exponent.sign == -1) {
      throw new ArithmeticException("exponent cannot be negative");
    }
    if (b.sign == 0 && exponent.sign == 0) {
      throw new ArithmeticException("indeterminate number");
    }
    if (exponent.sign == 0) {
      return new BigInteger(1);
    }
    if (b.sign == 0) {
      return new BigInteger();
    }
    if (exponent.equals(ONE)) {
      return b.clone();
    }
    if (exponent.isEven()) {
      return (pow(multiply(b, b), divide(exponent, TWO)));
    } else {
      return multiply(b, pow(multiply(b, b), divide(exponent, TWO)));
    }
  }

  /**
   * Returns a BigInteger whose value is (this<sup>exponent</sup>).
   * @param exponent exponent to which this BigInteger is to be raised.
   * @return this<sup>exponent</sup>
   * @throws ArithmeticException if exponent is negative (yields non-integer value) or 0<sup>0</sup>..
   */
  public BigInteger pow(BigInteger exponent) throws ArithmeticException {
    return pow(this, exponent);
  }

  /**
   * Checks whether this BigInteger is odd or not.
   * @return true if this BigInteger is odd. Otherwise false.
   */
  public boolean isOdd() {
    return (sign != 0 && digits.get(0) % 2 == 1);
  }

  /**
   * Checks whether this BigInteger is even or not.
   * @return true if this BigInteger is even. Otherwise false.
   */
  public boolean isEven() {
    return (sign == 0 || digits.get(0) % 2 == 0);
  }

  /**
   * Returns a BigInteger whose value is (b<sup>exponent</sup> % m).
   * @param b the specified BigInteger.
   * @param exponent exponent to which the specified BigInteger is to be raised.
   * @param m the modulus.
   * @return b<sup>exponent</sup> % m
   * @throws ArithmeticException if exponent is negative (yields non-integer value), 0<sup>0</sup>,
   * or m <= 0.
   */
  public static BigInteger modPow(BigInteger b, BigInteger exponent, BigInteger m)
      throws ArithmeticException {
    if (exponent.sign == -1) {
      throw new ArithmeticException("exponent cannot be negative");
    }
    if (m.sign <= 0) {
      throw new ArithmeticException("modulus must be > 0");
    }
    if (b.sign == 0 && exponent.sign == 0) {
      throw new ArithmeticException("indeterminate number");
    }
    if (exponent.sign == 0) {
      return new BigInteger(1).mod(m);
    }
    if (b.sign == 0) {
      return new BigInteger();
    }
    if (exponent.equals(ONE)) {
      return b.clone().mod(m);
    }
    if (exponent.isEven()) {
      return (modPow(multiply(b, b).mod(m), divide(exponent, TWO), m));
    } else {
      return multiply(b, modPow(multiply(b, b).mod(m), divide(exponent, TWO), m)).mod(m);
    }
  }

  /**
   * Returns a BigInteger whose value is (this<sup>exponent</sup> % m).
   * @param exponent exponent to which this BigInteger is to be raised.
   * @param m the modulus.
   * @return this<sup>exponent</sup> % m
   * @throws ArithmeticException if exponent is negative (yields non-integer value) or m is zero.
   */
  public BigInteger modPow(BigInteger exponent, BigInteger m) {
    return modPow(this, exponent, m);
  }

  /**
   * Returns a BigInteger whose value is GCD(b1, b2). Returns 0 if both Biginteger is zero.
   * @param b1 value which the GCD is to be computed.
   * @param b2 value which the GCD is to be computed.
   * @return GCD(b1, b2)
   */
  public static BigInteger gcd(BigInteger b1, BigInteger b2) {
    BigInteger temp1 = abs(b1);
    BigInteger temp2 = abs(b2);
    if (temp1.sign == 0) {
      return temp2.clone();
    }
    return gcd(temp2.mod(temp1), temp1);
  }

  /**
   * Returns a BigInteger whose value is GCD(this, val). Returns 0 if both Biginteger is zero.
   * @param val value with which the GCD is to be computed.
   * @return GCD(this, val)
   */
  public BigInteger gcd(BigInteger val) {
    return gcd(this, val);
  }

  /**
   * Returns a BigInteger whose value is abs(val).
   * @param val value which the absolute value is to be computed.
   * @return abs(val)
   */
  public static BigInteger abs(BigInteger val) {
    if (val.sign >= 0) {
      return val.clone();
    } else {
      return negate(val);
    }
  }

  /**
   * Returns a BigInteger whose value is abs(this).
   * @return abs(this)
   */
  public BigInteger abs() {
    return abs(this);
  }

  /**
   * Returns an array of BigInteger [d, x, y] such that d = GCD(a, b) and ax + by = d.
   * @param a value which the GCD is to be computed.
   * @param b value which the GCD is to be computed.
   * @return array of BigInteger [d, x, y] such that d = GCD(a, b) and ax + by = d.
   */
  public static BigInteger[] gcdExtended(BigInteger a, BigInteger b) {
    BigInteger[] results = gcdExtended2(abs(a), abs(b));
    if (a.sign == -1) {
      results[1] = multiply(results[1], new BigInteger(-1));
    }
    if (b.sign == -1) {
      results[2] = multiply(results[2], new BigInteger(-1));
    }
    return results;
  }

  /**
   * Returns an array of BigInteger [d, x, y] such that d = GCD(a, b) and ax + by = d.
   *
   * <p>Both a and b are positive BigInteger.
   * @param a value which the GCD is to be computed.
   * @param b value which the GCD is to be computed.
   * @return array of BigInteger [d, x, y] such that d = GCD(a, b) and ax + by = d.
   */
  private static BigInteger[] gcdExtended2(BigInteger a, BigInteger b) {
    BigInteger[] results = new BigInteger[3];
    if (a.sign == 0) {
      results[0] = b.clone();
      results[1] = new BigInteger();
      results[2] = new BigInteger(1);
      return results;
    }
    BigInteger[] vals = gcdExtended2(b.mod(a), a);
    results[0] = vals[0];
    results[2] = vals[1];
    results[1] = vals[2].subtract(multiply(vals[1], divide(b, a)));
    return results;
  }

  /**
   * Returns an array of BigInteger [d, x, y] such that d = GCD(this, val) and
   * this * x + val * y = d.
   * @param val value with which the GCD is to be computed.
   * @return array of BigInteger [d, x, y] s such that d = GCD(this, val) and
   * this * x + val * y = d.
   */
  public BigInteger [] gcdExtended(BigInteger val) {
    return gcdExtended(this, val);
  }

  /**
   * Returns a BigInteger whose value is (b<sup>-1</sup> mod m).
   * @param b the BigInteger which the modInverse is to be computed.
   * @param m the modulus.
   * @return b<sup>-1</sup> mod m
   * @throws ArithmeticException if m <= 0 or b is not relatively prime to m (has no multiplicative
   * inverse mod m).
   */
  public static BigInteger modInverse(BigInteger b, BigInteger m) throws ArithmeticException {
    if (m.sign <= 0) {
      throw new ArithmeticException("the modulo must be positive");
    }
    BigInteger[] temp = gcdExtended(b, m);
    if (!temp[0].equals(ONE)) {
      throw new ArithmeticException("this BigInteger has no multiplicative inverse mod m");
    }
    if (temp[1].sign == -1) {
      return add(m, temp[1]);
    }
    return temp[1];
  }

  /**
   * Returns a BigInteger whose value is (this<sup>-1</sup> mod m).
   * @param m the modulus.
   * @return this<sup>-1</sup> mod m
   * @throws ArithmeticException if m <= 0 or
   * this BigInteger is not relatively prime to m (has no multiplicative inverse mod m).
   */
  public BigInteger modInverse(BigInteger m) throws ArithmeticException {
    return modInverse(this, m);
  }

  /**
   * Returns a random BigInteger within the range [0, n].
   * @param n upper bound of the range.
   * @return random BigInteger within the range [0, n].
   * @throws IllegalArgumentException if n < 0.
   */
  public static BigInteger random(BigInteger n) throws IllegalArgumentException {
    if (n.sign <= 0) {
      throw new IllegalArgumentException("Invalid range");
    }
    BigInteger result = new BigInteger();
    SecureRandom random = new SecureRandom();
    int currDigit = n.digits.size() - 1;
    boolean sameVal = true;
    while(currDigit >= 0 && sameVal) {
      int val = random.nextInt(n.digits.get(currDigit) + 1);
      if (val != n.digits.get(currDigit)) {
        sameVal = false;
      }
      result.digits.add(0, val);
      currDigit--;
    }
    while(currDigit >= 0) {
      int val = random.nextInt(10);
      result.digits.add(0, val);
      currDigit--;
    }
    //Remove trailing zeros
    while(result.digits.size() > 0 && result.digits.get(result.digits.size() - 1) == 0) {
      result.digits.remove(result.digits.size() - 1);
    }
    if (result.digits.size() == 0) {
      result.sign = 0;
    } else {
      result.sign = 1;
    }
    return result;
  }

  /**
   * Returns a random BigInteger within the range [a, b].
   * @param a lower bound of the range.
   * @param b upper bound of the range.
   * @return random BigInteger within the range [a, b].
   * @throws IllegalArgumentException if a is greater than b.
   */
  public static BigInteger random(BigInteger a, BigInteger b) throws IllegalArgumentException {
    return add(a, random(subtract(b, a)));
  }
  
  /**
   * Returns true if b is probably prime, false if it's definitely composite.
   * If uncertainty <= 0, returns true.
   * @param b the BigInteger to be tested.
   * @param uncertainty a measure of uncertainty that the caller is willing to tolerate.
   * the probabilty that the BigInteger is not prime when
   * it returns true is (1/4<sup>uncertainty</sup>).
   * @return true if b is probably prime, false if it's definitely composite.
   */
  public static boolean isProbablePrime(BigInteger b, int uncertainty) {
    if (uncertainty <= 0) {
      return true;
    }
    if (b.isLessThan(TWO)) {
      return false;
    }
    if (b.isGreaterThan(THREE)) {
      if (b.isEven()) {
        return false;
      } else {
        return rabinMiller(b, uncertainty);
      }

    }
    return true;
  }

  /**
   * Returns true if this BigInteger is probably prime, false if it's definitely composite.
   * If uncertainty <= 0, returns true.
   * @param uncertainty a measure of uncertainty that the caller is willing to tolerate.
   * the probabilty that this BigInteger is not prime when
   * it returns true is (1/4<sup>uncertainty</sup>).
   * @return true if b is probably prime, false if it's definitely composite.
   */
  public boolean isProbablePrime(int uncertainty) {
    return isProbablePrime(this, uncertainty);
  }

  /**
   * Rabin Miller primality test.
   * @param b the BigInteger to be tested.
   * @param uncertainty a measure of uncertainty that the caller is willing to tolerate.
   * the probabilty that this BigInteger is not prime when
   * it returns true is (2<sup>-uncertainty</sup>).
   * @return true if b is probably prime, false if it's definitely composite.
   */
  private static boolean rabinMiller(BigInteger b, int uncertainty) {
    BigInteger d = subtract(b, ONE);
    BigInteger temp = subtract(b, ONE);
    int r = 0;
    do {
      d = divide(d, TWO);
      r++;
    }while (d.isEven());
    int k = 0;
    while (k < uncertainty) {
      BigInteger a = random(TWO, subtract(b, TWO));
      BigInteger x = modPow(a, d, b);
      if (x.equals(ONE) || x.equals(temp)) {
        k++;
        continue;
      }
      boolean cont = false;
      for (int i = 0; i < r - 1; i++) {
        x = modPow(x, TWO, b);
        if (x.equals(ONE)) {
          return false;
        }
        if (x.equals(temp)) {
          cont = true;
          break;
        }
      }
      if (cont) {
        k++;
        continue;
      }
      return false;
    }
    return true;
  }

  /**
   * Returns a positive BigInteger that is probably prime, with the specified number of digits.
   * The probability that the BigInteger is composite is (4<sup>-uncertainty</sup>).
   * @param digit number of digits for the generated probable prime BigInteger.
   * @param uncertainty a measure of uncertainty that the caller is willing to tolerate.
   * @return a positive BigInteger that is probably prime, with the specified number of digits.
   * @throws IllegalArgumentException if digit <= 0.
   */
  public static BigInteger probablePrime(int digit, int uncertainty)
      throws IllegalArgumentException {
    if (digit <= 0) {
      throw new IllegalArgumentException("digit must be > 0");
    }
    BigInteger lowerBound = ONE.multiplyByBasePow(digit - 1);
    BigInteger upperBound = new BigInteger(9);
    for (int i = 0; i < digit - 1; i++) {
      upperBound.digits.add(9);
    }
    BigInteger probablePrime = random(lowerBound, upperBound);
    while(!probablePrime.isProbablePrime(uncertainty)) {
      probablePrime = random(lowerBound, upperBound);
    }
    return probablePrime;
  }

  /**
   * Returns the String representation of this BigInteger.
   * @return String representation of this BigInteger.
   */
  public String toString() {
    if (sign == 0) {
      return "0";
    }
    StringBuilder result = new StringBuilder();
    if (sign == -1) {
      result.append('-');
    }
    for (int i = digits.size() - 1; i >= 0; i--) {
      result.append(digits.get(i));
    }
    return result.toString();
  }

}
