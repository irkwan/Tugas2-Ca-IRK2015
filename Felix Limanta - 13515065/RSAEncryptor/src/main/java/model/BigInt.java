package org.felixlimanta.RSAEncryptor.model;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import sun.misc.DoubleConsts;
import sun.misc.FloatConsts;

/**
 * Created by ASUS on 07/06/17.
 */
public class BigInt extends Number implements Comparable<BigInt> {

  private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;

  /**
   * This mask is used to obtain the value of an int as if it were unsigned.
   * Usage: <code>long a = int b & LONG_MASK</code>
   */
  private final static long LONG_MASK = 0xffffffffL;

  private static final int KARATSUBA_THRESHOLD = 50;
  private static final int DEFAULT_PRIME_CERTAINTY = 100;

  private static int[] primesBelow100 = new int[] {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
      47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };

  private static long bitsPerDigit[] = { 0, 0,
      1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672,
      3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633,
      4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210,
      5253, 5295
  };

  private static int digitsPerInt[] = {0, 0, 30, 19, 15, 13, 11,
      11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6,
      6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5
  };

  private static int intRadix[] = {0, 0,
      0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800,
      0x75db9c97, 0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,
      0x19a10000, 0x309f1021, 0x57f6c100, 0xa2f1b6f,  0x10000000,
      0x18754571, 0x247dbc80, 0x3547667b, 0x4c4b4000, 0x6b5a6e1d,
      0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,  0x1269ae40,
      0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
      0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400
  };

  private static SecureRandom random;


  private byte sign;
  private int[] mag;

  private int lowestSetBit;
  private int bitLength;
  private int firstNonzeroIntNum;

  public static BigInt ZERO = new BigInt(new int[0], (byte) 0);
  public static BigInt ONE = new BigInt(valueOf(1));
  public static BigInt TWO = new BigInt(valueOf(2));
  public static BigInt NEGATIVE_ONE = new BigInt(valueOf(-1));


  static {
    random = new SecureRandom();
  }

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

  public BigInt(byte[] mag, byte sign) {
    this.mag = stripLeadingZeros(mag);

    if (sign < -1 || sign > 1)
      throw(new NumberFormatException("Invalid signum value"));

    if (this.mag.length == 0) {
      this.sign = 0;
    } else {
      if (sign == 0)
        throw(new NumberFormatException("signum-magnitude mismatch"));
      this.sign = sign;
    }
    if (mag.length >= MAX_MAG_LENGTH) {
      checkRange();
    }
  }

  public BigInt(String val) {
    this(valueOf(val, 10));
  }

  public BigInt(long val) {
    this(valueOf(val));
  }

  public BigInt(BigInt b) {
    mag = Arrays.copyOf(b.mag, b.mag.length);
    sign = b.sign;
  }

  public BigInt(int numBits, Random rnd) {
    this(randomBits(numBits, rnd), (byte) 1);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Conversion
  //------------------------------------------------------------------------------------------------

  public static BigInt valueOf(String val, int radix) {
    int cursor = 0, numDigits;
    final int len = val.length();

    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
      throw new NumberFormatException("Radix out of range");
    if (len == 0)
      throw new NumberFormatException("Zero length BigInteger");

    // Check for at most one leading sign
    byte sign = 1;
    int index1 = val.lastIndexOf('-');
    int index2 = val.lastIndexOf('+');
    if (index1 >= 0) {
      if (index1 != 0 || index2 >= 0) {
        throw new NumberFormatException("Illegal embedded sign character");
      }
      sign = -1;
      cursor = 1;
    } else if (index2 >= 0) {
      if (index2 != 0) {
        throw new NumberFormatException("Illegal embedded sign character");
      }
      cursor = 1;
    }
    if (cursor == len)
      throw new NumberFormatException("Zero length BigInteger");

    // Skip leading zeros and compute number of digits in magnitude
    while (cursor < len &&
        Character.digit(val.charAt(cursor), radix) == 0) {
      cursor++;
    }

    int[] mag;
    if (cursor == len) {
      return ZERO;
    }
    numDigits = len - cursor;

    // Pre-allocate array of expected size. May be too large but can
    // never be too small. Typically exact.
    long numBits = ((numDigits * bitsPerDigit[radix]) >>> 10) + 1;
    if (numBits + 31 >= (1L << 32)) {
      reportOverflow();
    }
    int numWords = (int) (numBits + 31) >>> 5;
    int[] magnitude = new int[numWords];

    // Process first (potentially short) digit group
    int firstGroupLen = numDigits % digitsPerInt[radix];
    if (firstGroupLen == 0)
      firstGroupLen = digitsPerInt[radix];
    String group = val.substring(cursor, cursor += firstGroupLen);
    magnitude[numWords - 1] = Integer.parseInt(group, radix);
    if (magnitude[numWords - 1] < 0)
      throw new NumberFormatException("Illegal digit");

    // Process remaining digit groups
    int superRadix = intRadix[radix];
    int groupVal = 0;
    while (cursor < len) {
      group = val.substring(cursor, cursor += digitsPerInt[radix]);
      groupVal = Integer.parseInt(group, radix);
      if (groupVal < 0)
        throw new NumberFormatException("Illegal digit");
      destructiveMulAdd(magnitude, superRadix, groupVal);
    }
    // Required for cases where the array was overallocated.
    mag = stripLeadingZeros(magnitude);
    if (mag.length >= MAX_MAG_LENGTH) {
      checkRange(mag);
    }
    return new BigInt(mag, sign);
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
    if (sign == 0 || mag.length == 0)
      return "0";

    BigInt base = new BigInt(1000000000);
    BigInt b = this.abs();
    java.util.Stack<Integer> x = new java.util.Stack<>();
    while (!b.equals(ZERO)) {
      BigInt[] divMod = b.divideAndRemainder(base);
      try {
        x.push(divMod[1].mag[0]);
      } catch (ArrayIndexOutOfBoundsException e) {
        x.push(0);
      }
      b = divMod[0];
    }

    StringBuilder s = new StringBuilder();
    if (sign < 0)
      s.append('-');
    s.append(Integer.toUnsignedString(x.pop()));
    while (!x.empty()) {
      s.append(
          String.format("%09d", Integer.toUnsignedLong(x.pop()))
      );
    }
    return s.toString();
  }

  public String toBinaryString() {
    if (sign == 0)
      return "0";

    StringBuilder s = new StringBuilder();
    if (sign < 0)
      s.append('-');
    try {
      s.append(Integer.toBinaryString(mag[0]));
    } catch (ArrayIndexOutOfBoundsException e) {
      s.append(Integer.toBinaryString(0));
    }
    for (int i = 1; i < mag.length; ++i) {
      // Pad with zeros
      String group = Integer.toBinaryString(mag[i]);
      for (int j = 0; j < 32 - group.length(); ++j) {
        s.append('0');
      }
      s.append(group);
    }
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

  public byte[] toByteArray() {
    int byteLen = bitLength() / 8 + 1;
    byte[] byteArray = new byte[byteLen];

    for (int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 0; i >= 0; --i) {
      if (bytesCopied == 4) {
        nextInt = getInt(intIndex++);
        bytesCopied = 1;
      } else {
        nextInt >>>= 8;
        bytesCopied++;
      }
      byteArray[i] = (byte)nextInt;
    }
    return byteArray;
  }

  @Override
  public int intValue() {
    int result = 0;
    result = getInt(0);
    return result;
  }

  @Override
  public long longValue() {
    long result = 0;

    for (int i=1; i >= 0; i--)
      result = (result << 32) + (getInt(i) & LONG_MASK);
    return result;
  }

  @Override
  public float floatValue() {
    if (sign == 0) {
      return 0.0f;
    }

    int exponent = ((mag.length - 1) << 5) + 32 - Integer.numberOfLeadingZeros(mag[0]) - 1;

    // exponent == floor(log2(abs(this)))
    if (exponent < Long.SIZE - 1) {
      return longValue();
    } else if (exponent > Float.MAX_EXPONENT) {
      return sign > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }
    
    int shift = exponent - FloatConsts.SIGNIFICAND_WIDTH;

    int twiceSignifFloor;

    int nBits = shift & 0x1f;
    int nBits2 = 32 - nBits;

    if (nBits == 0) {
      twiceSignifFloor = mag[0];
    } else {
      twiceSignifFloor = mag[0] >>> nBits;
      if (twiceSignifFloor == 0) {
        twiceSignifFloor = (mag[0] << nBits2) | (mag[1] >>> nBits);
      }
    }

    int signifFloor = twiceSignifFloor >> 1;
    signifFloor &= FloatConsts.SIGNIF_BIT_MASK; // remove the implied bit
    
    boolean increment = (twiceSignifFloor & 1) != 0
        && ((signifFloor & 1) != 0 || abs().getLowestSetBit() < shift);
    int signifRounded = increment ? signifFloor + 1 : signifFloor;
    int bits = ((exponent + FloatConsts.EXP_BIAS))
        << (FloatConsts.SIGNIFICAND_WIDTH - 1);
    bits += signifRounded;
    bits |= sign & FloatConsts.SIGN_BIT_MASK;
    return Float.intBitsToFloat(bits);
  }

  @Override
  public double doubleValue() {
    if (sign == 0) {
      return 0.0;
    }

    int exponent = ((mag.length - 1) << 5) + 32 - Integer.numberOfLeadingZeros(mag[0]) - 1;

    // exponent == floor(log2(abs(this))Double)
    if (exponent < Long.SIZE - 1) {
      return longValue();
    } else if (exponent > Double.MAX_EXPONENT) {
      return sign > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }
    
    int shift = exponent - DoubleConsts.SIGNIFICAND_WIDTH;

    long twiceSignifFloor;

    int nBits = shift & 0x1f;
    int nBits2 = 32 - nBits;

    int highBits;
    int lowBits;
    if (nBits == 0) {
      highBits = mag[0];
      lowBits = mag[1];
    } else {
      highBits = mag[0] >>> nBits;
      lowBits = (mag[0] << nBits2) | (mag[1] >>> nBits);
      if (highBits == 0) {
        highBits = lowBits;
        lowBits = (mag[1] << nBits2) | (mag[2] >>> nBits);
      }
    }

    twiceSignifFloor = ((highBits & LONG_MASK) << 32)
        | (lowBits & LONG_MASK);

    long signifFloor = twiceSignifFloor >> 1;
    signifFloor &= DoubleConsts.SIGNIF_BIT_MASK; // remove the implied bit
    
    boolean increment = (twiceSignifFloor & 1) != 0
        && ((signifFloor & 1) != 0 || abs().getLowestSetBit() < shift);
    long signifRounded = increment ? signifFloor + 1 : signifFloor;
    long bits = (long) ((exponent + DoubleConsts.EXP_BIAS))
        << (DoubleConsts.SIGNIFICAND_WIDTH - 1);
    bits += signifRounded;

    bits |= sign & DoubleConsts.SIGN_BIT_MASK;
    return Double.longBitsToDouble(bits);
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
    return compareMagnitude(this.mag, val.mag);
  }

  private static int compareMagnitude(int[] x, int[] y) {
    int xlen = x.length;
    int ylen = y.length;

    if (xlen < ylen)
      return -1;
    if (xlen > ylen)
      return 1;
    for (int i = 0; i < xlen; ++i) {
      long a = Integer.toUnsignedLong(x[i]);
      long b = Integer.toUnsignedLong(y[i]);
      if (a < b) {
        return -1;
      } else if (a > b) {
        return 1;
      }
    }
    return 0;
  }

  public boolean equals(BigInt val) {
    return this.compareTo(val) == 0;
  }

  @Override
  public int hashCode() {
    int hashCode = 0;
    for (int n: mag)
      hashCode = (int)(31 * hashCode + (n & LONG_MASK));
    return hashCode * sign;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Unary operators
  //------------------------------------------------------------------------------------------------

  public int getSign() {
    return sign;
  }

  public BigInt abs() {
    return (sign >= 0 ? this : this.negate());
  }

  public BigInt negate() {
    return new BigInt(this.mag, (byte) -this.sign);
  }

  public BigInt increment() {
    if (this.compareTo(ZERO) == 0)
      return ONE;
    if (this.compareTo(NEGATIVE_ONE) == 0)
      return ZERO;

    if (this.sign == -1)
      return new BigInt(decrement(mag), sign);
    else
      return new BigInt(increment(mag), sign);
  }

  private static int[] increment(int[] val) {
    boolean overflow = true;
    for (int i = val.length - 1; i >= 0 && overflow; --i) {
      if (val[i] == -1) {
        val[i] = 0;
      } else {
        val[i]++;
        overflow = false;
      }
    }
    if (overflow) {
      val = new int[val.length + 1];
      val[0] = 1;
    }
    return val;
  }

  public BigInt decrement() {
    if (this.compareTo(ZERO) == 0)
      return NEGATIVE_ONE;
    else if (this.compareTo(ONE) == 0)
      return ZERO;

    if (this.sign == -1)
      return new BigInt(increment(mag), sign);
    else
      return new BigInt(decrement(mag), sign);
  }

  private static int[] decrement(int[] val) {
    int i = val.length - 1;
    while (val[i] == 0) {
      val[i--] = -1;
    }
    val[i]--;
    return val;
  }

  public boolean isOdd() {
    return ((mag[mag.length - 1] & 1) != 0);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Bit operations
  //------------------------------------------------------------------------------------------------

  public int bitLength() {
    int n = bitLength - 1;
    if (n == -1) { // bitLength not initialized yet
      int[] m = mag;
      int len = m.length;
      if (len == 0) {
        n = 0; // offset by one to initialize
      }  else {
        // Calculate the bit length of the magnitude
        int magBitLength = ((len - 1) << 5) + 32 - Integer.numberOfLeadingZeros(mag[0]);
        if (sign < 0) {
          // Check if magnitude is a power of two
          boolean pow2 = (Integer.bitCount(mag[0]) == 1);
          for (int i=1; i< len && pow2; i++)
            pow2 = (mag[i] == 0);

          n = (pow2 ? magBitLength -1 : magBitLength);
        } else {
          n = magBitLength;
        }
      }
      bitLength = n + 1;
    }
    return n;
  }

  public int getLowestSetBit() {
    int lsb = lowestSetBit - 2;
    if (lsb == -2) {  // lowestSetBit not initialized yet
      lsb = 0;
      if (sign == 0) {
        lsb -= 1;
      } else {
        // Search for lowest order nonzero int
        int i,b;
        for (i=0; (b = getInt(i)) == 0; i++);
        lsb += (i << 5) + Integer.numberOfTrailingZeros(b);
      }
      lowestSetBit = lsb + 2;
    }
    return lsb;
  }

  public BigInt setBit(int n) {
    if (n < 0)
      throw new ArithmeticException("Negative bit address");

    int intNum = n >>> 5;
    int[] result = new int[Math.max((bitLength() >>> 5) + 1, intNum+2)];

    for (int i=0; i < result.length; i++)
      result[result.length-i-1] = getInt(i);

    result[result.length-intNum-1] |= (1 << (n & 31));

    return new BigInt(result);
  }

  public boolean testBit(int n) {
    if (n < 0)
      throw new ArithmeticException("Negative bit address");

    return (getInt(n >>> 5) & (1 << (n & 31))) != 0;
  }

  private static byte[] randomBits(int numBits, Random rnd) {
    if (numBits < 0)
      throw new IllegalArgumentException("numBits must be non-negative");
    int numBytes = (int)(((long) numBits + 7) / 8); // avoid overflow
    byte[] randomBits = new byte[numBytes];

    // Generate random bytes and mask out excess bits
    if (numBytes > 0) {
      rnd.nextBytes(randomBits);
      int excessBits = 8 * numBytes - numBits;
      randomBits[0] &= (1 << (8 - excessBits)) - 1;
    }
    return randomBits;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Shifts
  //------------------------------------------------------------------------------------------------

  public BigInt shiftLeft(int n) {
    if (sign == 0)
      return ZERO;
    if (n > 0) {
      return new BigInt(shiftLeft(mag, n), sign);
    } else if (n == 0) {
      return this;
    } else {
      return shiftRightImpl(-n);
    }
  }

  private static int[] shiftLeft(int[] mag, int n) {
    if (mag.length == 0)
      return mag;

    int nInts = n >>> 5;
    int nBits = n & 0x1f;
    int magLen = mag.length;
    int newMag[] = null;

    if (nBits == 0) {
      newMag = new int[magLen + nInts];
      System.arraycopy(mag, 0, newMag, 0, magLen);
    } else {
      int i = 0;
      int nBits2 = 32 - nBits;
      int highBits = mag[0] >>> nBits2;
      if (highBits != 0) {
        newMag = new int[magLen + nInts + 1];
        newMag[i++] = highBits;
      } else {
        newMag = new int[magLen + nInts];
      }
      int j = 0;
      while (j < magLen-1)
        newMag[i++] = mag[j++] << nBits | mag[j] >>> nBits2;
      newMag[i] = mag[j] << nBits;
    }
    return newMag;
  }

  public BigInt shiftRight(int n) {
    if (sign == 0)
      return ZERO;
    if (n > 0) {
      return shiftRightImpl(n);
    } else if (n == 0) {
      return this;
    } else {
      // Possible int overflow in {@code -n} is not a trouble,
      // because shiftLeft considers its argument unsigned
      return new BigInt(shiftLeft(mag, -n), sign);
    }
  }

  private static int[] shiftRight(int[] mag, int n) {
    return new BigInt(mag, (byte) 1).shiftRightImpl(n).mag;
  }

  private BigInt shiftRightImpl(int n) {
    int nInts = n >>> 5;
    int nBits = n & 0x1f;
    int magLen = mag.length;
    int newMag[] = null;

    // Special case: entire contents shifted off the end
    if (nInts >= magLen)
      return (sign >= 0 ? ZERO : NEGATIVE_ONE);

    if (nBits == 0) {
      int newMagLen = magLen - nInts;
      newMag = Arrays.copyOf(mag, newMagLen);
    } else {
      int i = 0;
      int highBits = mag[0] >>> nBits;
      if (highBits != 0) {
        newMag = new int[magLen - nInts];
        newMag[i++] = highBits;
      } else {
        newMag = new int[magLen - nInts -1];
      }

      int nBits2 = 32 - nBits;
      int j=0;
      while (j < magLen - nInts - 1)
        newMag[i++] = (mag[j++] << nBits2) | (mag[j] >>> nBits);
    }

    if (sign < 0) {
      // Find out whether any one-bits were shifted off the end.
      boolean onesLost = false;
      for (int i=magLen-1, j=magLen-nInts; i >= j && !onesLost; i--)
        onesLost = (mag[i] != 0);
      if (!onesLost && nBits != 0)
        onesLost = (mag[magLen - nInts - 1] << (32 - nBits) != 0);

      if (onesLost)
        newMag = increment(newMag);
    }
    return new BigInt(newMag, sign);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Bitwise AND, OR, XOR, NOT
  //------------------------------------------------------------------------------------------------

  public BigInt and(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          & val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  public BigInt or(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          | val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  public BigInt xor(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          ^ val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  public BigInt not(BigInt val) {
    int[] result = new int[(bitLength() >>> 5) + 1];
    for (int i = 0; i < result.length; ++i)
      result[i] = ~getInt(result.length-i-1);
    return new BigInt(result);
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
        result[xi] = (int) (sum);
        carry = sum >>> 32;
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
      diff = (large[--li] & LONG_MASK) - (small[--si] & LONG_MASK) + (diff >> 32);
      result[li] = (int) diff;
    }

    // Subtract remainder of longer number while borrow propagates
    boolean borrow = (diff >> 32) != 0;
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

    if ((xlen < KARATSUBA_THRESHOLD) || (ylen < KARATSUBA_THRESHOLD)) {
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
    } else {
      return multiplyKaratsuba(val);
    }
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
      result[ri--] = (int) (product);
      carry = product >>> 32;
    }
    if (carry == 0L) {
      result = Arrays.copyOfRange(result, 1, result.length);
    } else {
      result[ri] = (int) (carry);
    }

    return new BigInt(result, sign);
  }

  private static int[] multiplyToLen(int[] x, int xlen, int[] y, int ylen) {
    if (xlen == 0 || ylen == 0)
      return ZERO.mag;

    final int xi = xlen - 1;
    final int yi = ylen - 1;
    int[] z = new int[xlen + ylen];

    long carry = 0;
    for (int j = yi, k = xi + yi + 1; j >= 0; --j, --k) {
      long product = (x[xi] & LONG_MASK) * (y[j] & LONG_MASK) + carry;
      z[k] = (int) (product);
      carry = product >>> 32;
    }
    z[xi] = (int) carry;

    for (int i = xi - 1; i >= 0; --i) {
      carry = 0;
      for (int j = yi, k = yi + i + 1; j >= 0; --j, --k) {
        long product = (x[i] & LONG_MASK) * (y[j] & LONG_MASK) +
            (z[k] & LONG_MASK) + carry;
        z[k] = (int)(product);
        carry = product >>> 32;
      }
      z[i] = (int) carry;
    }
    return z;
  }

  private BigInt multiplyKaratsuba(BigInt val) {
    int xlen = this.mag.length;
    int ylen = val.mag.length;

    // The number of ints in each half of the number.
    int half = (Math.max(xlen, ylen)+1) / 2;

    // xl and yl are the lower halves of x and y respectively,
    // xh and yh are the upper halves.
    BigInt xl = this.getLower(half);
    BigInt xh = this.getUpper(half);
    BigInt yl = this.getLower(half);
    BigInt yh = this.getUpper(half);

    BigInt p1 = xh.multiply(yh);  // p1 = xh * yh
    BigInt p2 = xl.multiply(yl);  // p2 = xl * yl

    // p3 = (xh + xl) * (yh + yl);
    BigInt p3 = xh.add(xl).multiply(yh.add(yl));

    // result = p1 * 2^(32 * 2 * half) + (p3 - p1 - p2) * 2^(32 * half) + p2
    BigInt result = p1.shiftLeft(32 * half)
        .add(p3.subtract(p1).subtract(p2))
        .shiftLeft(32 * half).add(p2);

    if (this.sign != val.sign)
      return result.negate();
    else
      return result;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Division and Remainder
  //------------------------------------------------------------------------------------------------

  public BigInt divide(BigInt val) {
    int cmp = this.abs().compareTo(val.abs());
    if (cmp == -1)
      return ZERO;
    else if (cmp == 0)
      return ONE;

    if (val.compareTo(ZERO) == 0)
      throw new ArithmeticException("Divisor is zero");
    else if (val.compareTo(ONE) == 0)
      return this;
    else if (val.compareTo(NEGATIVE_ONE) == 0)
      return this.negate();

    BigInt result = this.abs().divideAndRemainder(val.abs())[0];
    result.mag = stripLeadingZeros(result.mag);
    if (result.mag.length >= MAX_MAG_LENGTH)
      result.checkRange();
    result.sign = (byte)(this.sign * val.sign);
    return result;
  }

  public BigInt divide(long val) {
    return divide(new BigInt(val));
  }

  public BigInt remainder(BigInt val) {
    if (val.compareTo(ZERO) == 0)
      throw new ArithmeticException("Divisor is zero");
    if (val.compareTo(ONE) == 0 || val.compareTo(NEGATIVE_ONE) == 0)
      return ZERO;

    BigInt result = this.abs().divideAndRemainder(val.abs())[1];
    result.mag = stripLeadingZeros(result.mag);
    if (result.mag.length >= MAX_MAG_LENGTH)
      result.checkRange();
    result.sign = this.sign;
    return result;
  }

  public BigInt remainder(long val) {
    return remainder(new BigInt(val));
  }
  
  BigInt[] divideAndRemainder(BigInt y) {
    BigInt dividend = new BigInt(this);
    BigInt divisor = new BigInt(y);
    BigInt quotient = ZERO;
    int k = 0;
    while (dividend.compareTo(divisor) != -1) {
      divisor = divisor.shiftLeft(1);
      k++;
    }

    while (k-- > 0) {
      divisor = divisor.shiftRight(1);
      int cmp = dividend.compareTo(divisor);
      if (cmp >= 0) {
        dividend = dividend.subtract(divisor);
        quotient = quotient.shiftLeft(1).increment();
      } else {
        quotient = quotient.shiftLeft(1);
      }
    }
    return new BigInt[]{quotient, dividend};
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region GCD, LCM, Coprimes
  //------------------------------------------------------------------------------------------------

  /**
   * Computes GCD using Stein's algorithm
   *
   * @param val Value to be GCDed with
   * @return GCD of this and val
   */
  public BigInt gcd(BigInt val) {
    // GCD(0, v) == GCD(u, 0) == GCD(0, 0) == 0
    if (this.equals(ZERO) || val.equals(ZERO))
      return ZERO;

    BigInt u = this;
    BigInt v = val;
    /* Let shift := lg K, where K is the greatest power of 2
        dividing both u and v. */
    int shift;
    for (shift = 0; u.or(v).and(ONE).equals(ZERO); ++shift) {
      u = u.shiftRight(1);
      v = v.shiftRight(1);
    }

    // Make u odd
    while (!u.isOdd())
      u = u.shiftRight(1);

    do {
      // Make v odd
      while (!v.isOdd())
        v = v.shiftRight(1);

      // Swap if necessary so that u <= v
      if (u.compareTo(v) == 1) {
        BigInt t = v;
        v = u;
        u = t;
      }

      v = v.subtract(u);
    } while (!v.equals(ZERO));

    // Restore common factors of 2
    return u.shiftLeft(shift);
  }

  /**
   * Computes LCM with GCD, where LCM = (this / GCD) * val
   *
   * @param val Value to be LCMed with
   * @return LCM of this and val
   */
  public BigInt lcm(BigInt val) {
    BigInt gcd = this.gcd(val);
    if (gcd.equals(ZERO))
      return ZERO;
    return this.divide(gcd).multiply(val);
  }

  public boolean isCoprime(BigInt val) {
    return (this.gcd(val).equals(ONE));
  }


  //------------------------------------------------------------------------------------------------
  //endregion

  //region Modular arithmetic
  //------------------------------------------------------------------------------------------------

  public BigInt mod(BigInt val) {
    BigInt b = remainder(val);
    return (b.sign >= 0 ? b : b.add(val));
  }

  public BigInt mod(long val) {
    return mod(new BigInt(val));
  }

  /**
   * Finds the modular multiplicative inverse of this using extended Euclidean algorithm.
   * Assumes this and modulus are coprimes
   *
   * @param modulus modulus value
   * @return Modular multiplicative inverse
   */
  public BigInt modInverse(BigInt modulus) {
    if (modulus.compareTo(ZERO) != 1)
      throw new ArithmeticException("Modulus must be positive");
    else if (modulus.equals(ONE))
      return ZERO;

    if (!isCoprime(modulus))
      throw new ArithmeticException("This and modulus must be coprimes");

    BigInt val = this;
    BigInt mod = modulus;
    BigInt x0 = ZERO;
    BigInt x1 = ONE;

    while (val.compareTo(ONE) == 1) {
      BigInt quotient = val.divide(mod);
      BigInt t = mod;
      mod = val.mod(mod);
      val = t;

      t = x0;
      x0 = x1.subtract(quotient.multiply(x0));
      x1 = t;
    }

    // Make x1 positive
    if (x1.compareTo(ZERO) == -1)
      return x1.add(modulus);
    return x1;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Prime number operations
  //------------------------------------------------------------------------------------------------

  public static BigInt probablePrime(int bitLength) {
    return probablePrime(bitLength, random);
  }

  public static BigInt probablePrime(int bitLength, Random random) {
    if (bitLength < 2)
      throw new ArithmeticException("bitLength < 2");

    return generatePrime(bitLength, DEFAULT_PRIME_CERTAINTY, random);
  }

  private static BigInt generatePrime(int bitLength, int certainty, Random rnd) {
    long n = 1;
    for (int x: primesBelow100)
      n *= x;
    BigInt smallPrimeProduct = valueOf(n);
    
    int magLen = (bitLength + 31) >>> 5;
    int temp[] = new int[magLen];
    int highBit = 1 << ((bitLength+31) & 0x1f);  // High bit of high int
    int highMask = (highBit << 1) - 1;  // Bits to keep in high int

    while (true) {
      // Construct a candidate
      for (int i = 0; i < magLen; i++)
        temp[i] = rnd.nextInt();
      temp[0] = (temp[0] & highMask) | highBit;  // Ensure exact length
      if (bitLength > 2)
        temp[magLen-1] |= 1;  // Make odd if bitlen > 2

      BigInt p = new BigInt(temp, (byte) 1);

      // Do cheap "pre-test" if applicable
      if (bitLength > 6) {
        long r = p.remainder(smallPrimeProduct).longValue();
        boolean prime = true;
        for (int i = 0; i < primesBelow100.length && prime; ++i)
          prime = prime && (r % primesBelow100[i] != 0);
        if (!prime)
          continue;
      }

      // All candidates of bitLength 2 and 3 are prime by this point
      if (bitLength < 4)
        return p;

      // Do expensive test if we survive pre-test (or it's inapplicable)
      if (p.primeToCertainty(certainty, rnd))
        return p;
    }
  }

  public boolean isProbablePrime(int certainty) {
    return isProbablePrime(certainty, random);
  }

  public boolean isProbablePrime(int certainty, Random random) {
    if (certainty <= 0)
      return true;
    BigInt w = this.abs();
    if (w.equals(TWO))
      return true;
    if (!w.testBit(0) || w.equals(ONE))
      return false;

    return w.primeToCertainty(certainty, random);
  }

  boolean primeToCertainty(int certainty, Random random) {
    int rounds;
    int n = (Math.min(certainty, Integer.MAX_VALUE-1)+1)/2;

    int sizeInBits = this.bitLength();
    if (sizeInBits < 100) {
      rounds = 50;
      rounds = n < rounds ? n : rounds;
      return passesMillerRabin(rounds, random);
    }

    if (sizeInBits < 256) {
      rounds = 27;
    } else if (sizeInBits < 512) {
      rounds = 15;
    } else if (sizeInBits < 768) {
      rounds = 8;
    } else if (sizeInBits < 1024) {
      rounds = 4;
    } else {
      rounds = 2;
    }
    rounds = n < rounds ? n : rounds;

    return passesMillerRabin(rounds, random);
  }

  private boolean passesMillerRabin(int iterations, Random rnd) {
    // Find a and m such that m is odd and this == 1 + 2 ^ a * m
    BigInt thisMinusOne = this.subtract(ONE);
    BigInt m = thisMinusOne;
    int a = m.getLowestSetBit();
    m = m.shiftRight(a);

    // Do the tests
    if (rnd == null) {
      rnd = ThreadLocalRandom.current();
    }
    for (int i=0; i < iterations; i++) {
      // Generate a uniform random on (1, this)
      BigInt b;
      do {
        b = new BigInt(this.bitLength(), rnd);
      } while (b.compareTo(ONE) <= 0 || b.compareTo(this) >= 0);

      int j = 0;
      BigInt z = b.modExp(m, this);
      while (!((j == 0 && z.equals(ONE)) || z.equals(thisMinusOne))) {
        if (j > 0 && z.equals(ONE) || ++j == a)
          return false;
        z = z.modExp(TWO, this);
      }
    }
    return true;
  }


  /**
   * Finds the least prime number greater than this using Bertrand's postulate
   *
   * @return Next probable prime
   */
  public BigInt nextProbablePrime() {
    if (this.sign < 0)
      throw new ArithmeticException("start < 0: " + this);

    // Handle trivial cases
    if ((this.sign == 0) || this.equals(ONE))
      return TWO;

    BigInt result = this.increment();

    // Start at previous even number
    if (result.isOdd())
      result = result.decrement();

    // Looking for the next large prime
    int searchLen = result.bitLength() / 20 * 64;

    while (true) {
      BitSieve searchSieve = new BitSieve(result, searchLen);
      BigInt candidate = searchSieve.retrieve(result,
          DEFAULT_PRIME_CERTAINTY, random);
      if (candidate != null)
        return candidate;
      result = result.add(BigInt.valueOf(2 * searchLen));
    }
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Exponents
  //------------------------------------------------------------------------------------------------

  /**
   * Performs modular exponentiaton (this ^ exp % mod)
   *
   * <p>Reference:
   * <a href="http://www.geeksforgeeks.org/modular-exponentiation-power-in-modular-arithmetic/">
   *   GeeksForGeeks</a></p>
   *
   * @param exp Exponent, must be non-negative
   * @param mod Modulus
   * @return this ^ exp % mod
   */
  public BigInt modExp(BigInt exp, BigInt mod) {
    if (exp.sign == -1)
      throw new ArithmeticException("Negative exponent");

    if (mod.equals(ONE))
      return ZERO;

    BigInt result = ONE;
    BigInt base = this.mod(mod);

    while (exp.compareTo(ZERO) == 1) {
      if ((exp.mag[exp.mag.length - 1] & 1) == 1)
        result = result.multiply(base).mod(mod);
      base = base.multiply(base).mod(mod);
      exp = exp.shiftRight(1);
    }
    return result;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Common private utility functions
  //------------------------------------------------------------------------------------------------

  private void checkRange() {
    if (mag.length > MAX_MAG_LENGTH ||
        mag.length == MAX_MAG_LENGTH && mag[0] < 0)
      reportOverflow();
  }

  private static void checkRange(int[] mag) {
    if (mag.length > MAX_MAG_LENGTH ||
        mag.length == MAX_MAG_LENGTH && mag[0] < 0)
      reportOverflow();
  }

  private static void reportOverflow() {
    throw new ArithmeticException("BigInteger would overflow supported range");
  }

  private static void destructiveMulAdd(int[] x, int y, int z) {
    // Perform the multiplication word by word
    long yl = y & LONG_MASK;
    long zl = z & LONG_MASK;
    int len = x.length;

    long product;
    long carry = 0;
    for (int i = len - 1; i >= 0; --i) {
      product = yl * (x[i] & LONG_MASK) + carry;
      x[i] = (int) product;
      carry = product >>> 32;
    }

    // Perform the addition
    long sum = (x[len-1] & LONG_MASK) + zl;
    x[len-1] = (int)sum;
    carry = sum >>> 32;
    for (int i = len-2; i >= 0; i--) {
      sum = (x[i] & LONG_MASK) + carry;
      x[i] = (int)sum;
      carry = sum >>> 32;
    }
  }

  private static int[] stripLeadingZeros(byte a[]) {
    final int len = a.length;
    int keep;

    // Find first nonzero byte
    for (keep = 0; keep < len && a[keep] == 0; keep++);

    // Allocate new array and copy relevant part of input array
    int intLength = ((len - keep) + 3) >>> 2;
    int[] result = new int[intLength];
    int b = len - 1;
    for (int i = intLength-1; i >= 0; i--) {
      result[i] = a[b--] & 0xff;
      int bytesLeft = b - keep + 1;
      int bytesToTransfer = Math.min(3, bytesLeft);
      for (int j=8; j <= (bytesToTransfer << 3); j += 8)
        result[i] |= ((a[b--] & 0xff) << j);
    }
    return result;
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

    // Find first nonzero int
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

  private int getInt(int n) {
    if (n < 0)
      return 0;
    if (n >= mag.length)
      return (sign < 0) ? -1 : 0;

    int magInt = mag[mag.length-n-1];
    return (sign >= 0 ? magInt :
        (n <= firstNonzeroIntNum() ? -magInt : ~magInt));
  }

  private int firstNonzeroIntNum() {
    int fn = firstNonzeroIntNum - 2;
    if (fn == -2) { // firstNonzeroIntNum not initialized yet
      // Search for the first nonzero int
      int i;
      int mlen = mag.length;
      for (i = mlen - 1; i >= 0 && mag[i] == 0; i--);
      fn = mlen - i - 1;
      firstNonzeroIntNum = fn + 2; // offset by two to initialize
    }
    return fn;
  }

  private BigInt getLower(int n) {
    final int len = mag.length;
    if (len <= n)
      return abs();

    int lowerInts[] = Arrays.copyOfRange(mag, len - n, n);
    return new BigInt(stripLeadingZeros(lowerInts), (byte) 1);
  }

  private BigInt getUpper(int n) {
    final int len = mag.length;
    if (len <= n)
      return ZERO;

    int upperInts[] = Arrays.copyOf(mag, len - n);
    return new BigInt(stripLeadingZeros(upperInts), (byte) 1);
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
