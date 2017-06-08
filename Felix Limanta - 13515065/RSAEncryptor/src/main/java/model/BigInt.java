package org.felixlimanta.RSAEncryptor.model;

import java.util.Arrays;

/**
 * Created by ASUS on 07/06/17.
 */
public class BigInt implements Comparable<BigInt> {

  private static final int RADIX = 10;
  private static final int DIGITS_PER_INT = 9;
  private static final int BASE = 1000000000;
  private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;

  /**
   * This mask is used to obtain the value of an int as if it were unsigned.
   * Usage: <code>long a = int b & LONG_MASK</code>
   */
  final static long LONG_MASK = 0xffffffffL;

  private static final int KARATSUBA_THRESHOLD = 50;

  private byte sign;
  private int[] mag;

  public static BigInt ZERO = new BigInt(new int[0], (byte) 0);




  //region Constructor
  //------------------------------------------------------------------------------------------------

  public BigInt(int[] val) {
    if (val.length == 0)
      throw new NumberFormatException("Zero length BigInteger");

    if (val[0] < 0) {
      mag = makePositive(val);
      sign = -1;
    } else {
      mag = stripLeadingZeros(val);
      if (mag.length == 0)
        sign = 0;
      else
        sign = 1;
    }

    if (mag.length >= MAX_MAG_LENGTH)
      checkRange();
  }

  BigInt(int[] mag, byte sign) {
    this.sign = (mag.length == 0 ? 0 : sign);
    this.mag = mag;
    if (mag.length >= MAX_MAG_LENGTH)
      checkRange();
  }

  public BigInt(String val) {
    this(valueOf(val));
  }

  public BigInt(long val) {
    this(valueOf(val));
  }

  public BigInt(BigInt b) {
    mag = Arrays.copyOf(b.mag, b.mag.length);
    sign = b.sign;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Conversion
  //------------------------------------------------------------------------------------------------

  public static BigInt valueOf(String val) {
    int strLen = val.length();
    if (strLen == 0)
      throw new NumberFormatException("Zero length BigInteger");

    byte sign;
    int shift;
    if (val.charAt(0) == '-') {
      strLen--;
      sign = -1;
      shift = 1;
    } else {
      sign = 1;
      shift = 0;
    }

    int intLen = (strLen - 1) / DIGITS_PER_INT + 1;
    int firstLen = strLen - (intLen - 1) * DIGITS_PER_INT;
    int[] digits = new int[intLen];
    for (int i = 0; i < intLen; ++i) {
      String block = val.substring(
          Math.max(firstLen + (i - 1) * DIGITS_PER_INT + shift, shift),
          firstLen + (i * DIGITS_PER_INT) + shift
      );
      digits[i] = Integer.parseInt(block);
    }
    return new BigInt(digits, sign);
  }

  public static BigInt valueOf(long val) {
    if (val == 0)
      return ZERO;

    byte sign;
    if (val < 0) {
      val = -val;
      sign = -1;
    } else {
      sign = 1;
    }

    int[] mag;
    int highWord = (int)(val >>> 32);
    if (highWord == 0) {
      mag = new int[1];
      mag[0] = (int) val;
    } else {
      mag = new int[2];
      mag[0] = highWord;
      mag[1] = (int) val;
    }
    return new BigInt(mag, sign);
  }

  /**
   * Returns decimal representation of this BigInt object. The minus '-' sign is appended
   * when appropriate.
   *
   * @return Decimal string representation of this BigInt
   */
  @Override
  public String toString() {
    if (sign == 0)
      return "0";

    StringBuilder s = new StringBuilder();
    if (sign < 0)
      s.append('-');
    s.append(Integer.toUnsignedString(mag[0]));
    for (int i = 1; i < mag.length; ++i)
      s.append(
          String.format("%09d", Integer.toUnsignedLong(mag[i]))
      );
    return s.toString();
  }

  /**
   * toString function for debugging. Generates string with sign and array contents
   *
   * @param debug True to print debug data, false to print decimal string
   * @return String representation of this BigInt
   */
  String toString(boolean debug) {
    if (debug)
      return "BigInt{" +
          "sign=" + sign +
          ", mag=" + Arrays.toString(mag) +
          '}';
    else
      return toString();
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Comparison
  //------------------------------------------------------------------------------------------------

  @Override
  public int compareTo(BigInt val) {
    if (this.sign == val.sign) {
      switch (sign) {
        case 1:
          return compareMagnitude(val);
        case -1:
          return val.compareMagnitude(this);
        default:
          return 0;
      }
    }
    return sign > val.sign ? 1 : -1;
  }

  private int compareMagnitude(BigInt val) {
    int[] m1 = mag;
    int len1 = m1.length;
    int[] m2 = val.mag;
    int len2 = m2.length;
    if (len1 < len2)
      return -1;
    if (len1 > len2)
      return 1;
    for (int i = 0; i < len1; i++) {
      int a = m1[i];
      int b = m2[i];
      if (a != b)
        return ((a & LONG_MASK) < (b & LONG_MASK)) ? -1 : 1;
    }
    return 0;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Unary operators
  //------------------------------------------------------------------------------------------------

  public BigInt abs() {
    return (sign >= 0 ? this : this.negate());
  }

  public BigInt negate() {
    return new BigInt(this.mag, (byte) -this.sign);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Addition and Subtraction
  //------------------------------------------------------------------------------------------------

  public BigInt add(BigInt val) {
    if (this.sign == 0)
      return val;
    else if (val.sign == 0)
      return this;

    if (this.sign == val.sign)
      return new BigInt(add(this.mag, val.mag), sign);

    byte cmp = (byte) compareMagnitude(val);
    if (cmp == 0)
      return ZERO;

    int[] resultMag;
    if (cmp > 0)
      resultMag = subtract(this.mag, val.mag);
    else
      resultMag = subtract(val.mag, this.mag);
    resultMag = stripLeadingZeros(resultMag);

    if (cmp == sign)
      return new BigInt(resultMag, (byte) 1);
    else
      return new BigInt(resultMag, (byte) -1);
  }

  private static int[] add(int[] x, int[] y) {
    // If x is shorter, swap the two arrays
    if (x.length < y.length) {
      int[] tmp = x;
      x = y;
      y = tmp;
    }

    int xi = x.length;
    int yi = y.length;
    int result[] = new int[xi];
    long sum = 0;
    if (yi == 1) {
      sum = (x[--xi] & LONG_MASK) + (y[0] & LONG_MASK);
      result[xi] = (int) sum;
    } else {
      // Add common parts of both numbers
      long carry = 0;
      while (yi > 0) {
        sum = (x[--xi] & LONG_MASK) + (y[--yi] & LONG_MASK) + carry;
        result[xi] = (int) (sum % BASE);
        carry = sum / BASE;
      }
    }

    // Copy remainder of longer number while carry propagates
    boolean carry = (sum >>> 32 != 0);
    while (xi > 0 && carry) {
      carry = ((result[--xi] = x[xi] + 1) == 0);
    }

    // Copy remainder of longer number
    while (xi > 0)
      result[--xi] = x[xi];

    // Grow result if necessary
    if (carry) {
      int grown[] = new int[result.length + 1];
      System.arraycopy(result, 0, grown, 1, result.length);
      grown[0] = 1;
      return grown;
    }
    return result;
  }

  public BigInt subtract(BigInt val) {
    if (val.sign == 0)
      return this;
    else if (this.sign == 0)
      return val.negate();

    if (this.sign + val.sign == 0)
      return new BigInt(add(mag, val.mag), this.sign);

    byte cmp = (byte) compareMagnitude(val);
    if (cmp == 0)
      return ZERO;

    int[] resultMag;
    if (cmp > 0)
      resultMag = subtract(this.mag, val.mag);
    else
      resultMag = subtract(val.mag, this.mag);
    resultMag = stripLeadingZeros(resultMag);

    if (cmp == sign)
      return new BigInt(resultMag, (byte) 1);
    else
      return new BigInt(resultMag, (byte) -1);
  }

  private static int[] subtract(int[] large, int[] small) {
    int li = large.length;
    int[] result = new int[li];
    int si = small.length;
    long diff = 0;

    // Subtract common parts of both numbers
    while (si > 0) {
      diff = (large[--li] & LONG_MASK) - (small[--si] & LONG_MASK) + (diff >>> 32);
      result[li] = (int) diff;
    }

    // Subtract remainder of longer number while borrow propagates
    boolean borrow = (diff >> 32 != 0);
    while (li > 0 && borrow) {
      result[--li] = large[li] - 1;
      borrow = result[li] == -1;
    }

    // Copy remainder of longer number
    while (li > 0)
      result[--li] = large[li];

    return result;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Multiplication
  //------------------------------------------------------------------------------------------------

  public BigInt multiply(BigInt val) {
    if (this.sign == 0 || val.sign == 0)
      return ZERO;

    int xlen = mag.length;
    int ylen = val.mag.length;

    //if (xlen < KARATSUBA_THRESHOLD || ylen < KARATSUBA_THRESHOLD) {
    byte resultSign;
    if (sign == val.sign)
      resultSign = 1;
    else
      resultSign = -1;

    if (ylen == 1)
      return multiplyByInt(mag, val.mag[0], resultSign);
    if (xlen == 1)
      return multiplyByInt(val.mag, mag[0], resultSign);

    int[] result = multiplyToLen(mag, xlen, val.mag, ylen);
    result = stripLeadingZeros(result);
    return new BigInt(result, resultSign);
    //}
  }

  private static BigInt multiplyByInt(int[] x, int y, byte sign) {
    if (Integer.bitCount(y) == 1)
      return new BigInt(shiftLeft(x, Integer.numberOfTrailingZeros(y)), sign);

    int xlen = x.length;
    int[] result = new int[xlen + 1];
    long carry = 0;
    long yl = y & LONG_MASK;

    int ri = result.length - 1;
    for (int i = xlen - 1; i >= 0; --i) {
      long product = (x[i] & LONG_MASK) * yl + carry;
      result[ri--] = (int) (product % BASE);
      carry = product / BASE;
    }
    if (carry == 0L) {
      result = Arrays.copyOfRange(result, 1, result.length);
    } else {
      result[ri] = (int) (carry % BASE);
    }

    return new BigInt(result, sign);
  }

  private static int[] multiplyToLen(int[] x, int xlen, int[] y, int ylen) {
    final int xi = xlen - 1;
    final int yi = ylen - 1;
    int[] z = new int[xlen + ylen];

    long carry = 0;
    for (int j = yi, k = xi + yi + 1; j >= 0; --j, --k) {
      long product = (x[xi] & LONG_MASK) * (y[j] & LONG_MASK) + carry;
      z[k] = (int) (product % BASE);
      carry = product / BASE;
    }
    z[xi] = (int) carry;

    for (int i = xi - 1; i >= 0; --i) {
      carry = 0;
      for (int j = yi, k = yi + i + 1; j >= 0; --j, --k) {
        long product = (x[i] & LONG_MASK) * (y[j] & LONG_MASK) +
            (z[k] & LONG_MASK) + carry;
        z[k] = (int)(product % BASE);
        carry = product / BASE;
      }
      z[i] = (int) carry;
    }
    return z;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Shifts
  //------------------------------------------------------------------------------------------------

  private static int[] shiftLeft(int[] mag, int n) {
    int nInts = n >>> 5;
    int nBits = n & 0x1f;
    int magLen = mag.length;
    int newMag[];

    if (nBits == 0) {
      newMag = new int[magLen + nInts];
      System.arraycopy(mag, 0, newMag, 0, magLen);
    } else {
      int i = 0;
      int highBits = mag[0] >>> (32 - nBits);
      if (highBits != 0) {
        newMag = new int[magLen + nInts + 1];
        newMag[i++] = highBits;
      } else {
        newMag = new int[magLen + nInts];
      }
      int j=0;
      while (j < magLen-1)
        newMag[i++] = mag[j++] << nBits | mag[j] >>> (32 - nBits);
      newMag[i] = mag[j] << nBits;
    }
    return newMag;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Private utility functions for construction and conversion
  //------------------------------------------------------------------------------------------------

  private void checkRange() {
    if (mag.length > MAX_MAG_LENGTH ||
        mag.length == MAX_MAG_LENGTH && mag[0] < 0)
      reportOverflow();
  }

  private static void reportOverflow() {
    throw new ArithmeticException("BigInteger would overflow supported range");
  }

  /**
   * Strips leading zero
   *
   * @param val Original int array
   * @return Int array stripped of leading zeros
   */
  private static int[] stripLeadingZeros(int[] val) {
    final int len = val.length;
    int keep;

    // Find first nonzero byte
    for (keep = 0; keep < len && val[keep] == 0; ++keep);
    return Arrays.copyOfRange(val, keep, len);
  }

  /**
   * Converts a negative array to positive through 2's complement
   *
   * @param val Array representing a negative 2's complement number.
   * @return Unsigned array whose value is -val.
   */
  private static int[] makePositive(int[] val) {
    final int len = val.length;
    int keep, k;

    // Find first non-sign (0xffffffff) int
    for (keep = 0; keep < len && val[keep] == -1; ++keep);

    // Allocate output array
    // Allocate one extra output byte if all non-sign bytes = 0x00
    for (k = keep; k < len && val[k] == 0; ++k);
    int extraInt = (k == len) ? 1 : 0;
    int[] result = new int[len - keep + extraInt];

    // Copy 1's complement to output
    for (int i = keep; i < len; ++i)
      result[i - keep + extraInt] = ~val[i];

    // Add 1 to 1's complement to get 2's complement
    for (int i = result.length - 1; ++result[i] == 0; --i);

    return result;
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
