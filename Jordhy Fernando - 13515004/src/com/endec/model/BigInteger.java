package com.endec.model;

import java.util.ArrayList;

/*
 * File name          : BigInteger.java
 * Created on         : 18/06/17
 * Modified on        : 18/06/17
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
        result[i].sign = 1;
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
