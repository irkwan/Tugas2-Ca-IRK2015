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
   * Returns a BigInteger whose value is -b.
   * @return -b
   */
  public static BigInteger negate(BigInteger b) {
    BigInteger result = new BigInteger(b);
    result.sign = - result.sign;
    return result;
  }

  /**
   * Returns a BigInteger whose value is -this.
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
  public static BigInteger add (BigInteger b1, BigInteger b2) {
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
