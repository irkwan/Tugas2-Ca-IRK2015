package org.felixlimanta.RSAEncryptor.model;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents an immutable arbitrary-precision integer.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-07
 */
public class BigInt implements Comparable<BigInt> {

  private static final int MAX_MAG_LENGTH = Integer.MAX_VALUE / Integer.SIZE + 1;

  /**
   * This mask is used to obtain the value of an int as if it were unsigned.
   * Usage: <code>long a = int b & LONG_MASK</code>
   */
  private final static long LONG_MASK = 0xffffffffL;

  private static final int KARATSUBA_THRESHOLD = 50;
  private static final int DEFAULT_PRIME_CERTAINTY = 100;

  /**
   * Array of primes below 100
   */
  private static final int[] primesBelow100 = new int[] {
      2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
      47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
  };

  private static final long bitsPerDigit[] = { 0, 0,
      1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672,
      3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633,
      4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210,
      5253, 5295
  };

  /**
   * The number of digits in a given radix that can fit in a positive Java integer,
   * i.e. the highest integer n such that radix<sup>n</sup>n < 2<sup>31</sup>.
   * Taken from the java.lang.BigInteger library.
   */
  private static final int digitsPerInt[] = {0, 0, 30, 19, 15, 13, 11,
      11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6,
      6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5
  };

  /**
   * i<sup>digitsPerInt[i]</sup>.
   * Taken from the java.lang.BigInteger library.
   */
  private static final int intRadix[] = {0, 0,
      0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800,
      0x75db9c97, 0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,
      0x19a10000, 0x309f1021, 0x57f6c100, 0xa2f1b6f,  0x10000000,
      0x18754571, 0x247dbc80, 0x3547667b, 0x4c4b4000, 0x6b5a6e1d,
      0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,  0x1269ae40,
      0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
      0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400
  };

  /**
   * Random object for random number generation
   */
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

  /**
   * Constructs a BigInt object from an array of integers.
   *
   * @param val Array of integers to be converted to a BigInteger
   */
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

  /**
   * Constructs a BigInt object with a given magnitude and sign
   *
   * @param mag Array of integers
   * @param sign Sign value
   */
  BigInt(int[] mag, byte sign) {
    this.sign = (mag.length == 0 ? 0 : sign);
    this.mag = mag;
    if (mag.length >= MAX_MAG_LENGTH)
      checkRange();
  }

  /**
   * Constructs a BigInt object with a given byte array and sign
   *
   * @param mag Byte array to be converted
   * @param sign Sign value
   */
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

  /**
   * Constructs a BigInt object from its decimal string representation
   *
   * @param val Decimal string representation of the BigInt
   */
  public BigInt(String val) {
    this(valueOf(val, 10));
  }

  /**
   * Constructs a BigInt object from a <code>long</code> value
   *
   * @param val Long value to be converted to a BigInt
   */
  public BigInt(long val) {
    this(valueOf(val));
  }

  /**
   * Copy constructor
   *
   * @param b BigInt to be copied
   */
  public BigInt(BigInt b) {
    mag = Arrays.copyOf(b.mag, b.mag.length);
    sign = b.sign;
  }

  /**
   * Constructs a BigInt with random value of specified bit length
   *
   * @param numBits Bit length of randomized BigInt value
   * @param rnd Random number generator
   */
  public BigInt(int numBits, Random rnd) {
    this(randomBits(numBits, rnd), (byte) 1);
  }

  /**
   * Returns a copy of this BigInt
   *
   * @return Copy of this BigInt
   */
  @Override
  public BigInt clone() {
    return new BigInt(this);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Conversion
  //------------------------------------------------------------------------------------------------

  /**
   * Conversion from string in a given radix to a BigInt object.
   *
   * @param val String representation of a number
   * @param radix Radix of val
   * @return a BigInt object with value val
   */
  // Adapted from the java.lang.BigInteger library.
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

  /**
   * Conversion from a long value to a BigInt object
   *
   * @param val long value to be converted
   * @return a BigInt object with value val
   */
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
   * Returns a decimal representation of this BigInt object. The minus '-' sign is appended
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

  /**
   * Returns a binary representation of this BigInt object. The minus '-' sign is appended
   * when appropriate.
   *
   * @return Binary string representation of this BigInt
   */
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

  /**
   * Converts this BigInt object to a byte array
   *
   * @return Byte array containing value of this BigInt
   */
  // Adapted from the java.lang.BigInteger library.
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

  /**
   * Converts a BigInt to an int.
   * If BigInt is larger than 32 bit, only the lowest 32 bit is taken.
   *
   * @return Integer value of BigInt
   */
  public int intValue() {
    return getInt(0);
  }

  /**
   * Converts a BigInt to an long.
   * If BigInt is larger than 64 bit, only the lowest 32 bit is taken.
   *
   * @return Long value of BigInt
   */
  public long longValue() {
    long result = 0;

    for (int i = 1; i >= 0; i--)
      result = (result << 32) + (getInt(i) & LONG_MASK);
    return result;
  }

  /**
   * Returns the specified int from magnitude array
   *
   * @param n Digit position in little endian, i.e. 0 is the least significant digit
   * @return Signed integer value
   */
  // Adapted from the java.lang.BigInteger library
  private int getInt(int n) {
    if (n < 0)
      return 0;
    if (n >= mag.length)
      return (sign < 0) ? -1 : 0;

    int magInt = mag[mag.length-n-1];
    return (sign >= 0 ? magInt :
        (n <= firstNonzeroIntNum() ? -magInt : ~magInt));
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Comparison
  //------------------------------------------------------------------------------------------------

  /**
   * Compares this BigInt with the specified BigInt.
   * Returns -1 if this < val, 0 if this == val, 1 if this > val.
   *
   * @param val BigInt to be compared to
   * @return -1, 0, 1
   */
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

  /**
   * Compares magnitudes between this and val.
   *
   * @param val BigInt to be compared to
   * @return -1, 0, 1
   */
  private int compareMagnitude(BigInt val) {
    return compareMagnitude(this.mag, val.mag);
  }

  /**
   * Compares magnitudes between this and val.
   * Returns -1 if x < y, 0 if x == y, 1 if x > y.
   *
   * @param x Left hand side of comparison
   * @param y Right hand side of comparison
   * @return -1, 0, 1
   */
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

  /**
   * Checks equality between this integer and val
   *
   * @param val BigInt to be compared to
   * @return true if this == val
   */
  public boolean equals(BigInt val) {
    return this.compareTo(val) == 0;
  }

  /**
   * Returns the hash code for this BigInteger.
   *
   * @return hash code for this BigInteger.
   */
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

  /**
   * Sign value getter
   *
   * @return Sign value
   */
  public int getSign() {
    return sign;
  }

  /**
   * Returns a BigInt whose value is |this|
   *
   * @return |this|
   */
  public BigInt abs() {
    return (sign >= 0 ? this : this.negate());
  }

  /**
   * Returns a BigInt whose value is -this.
   *
   * @return -this
   */
  public BigInt negate() {
    return new BigInt(this.mag, (byte) -this.sign);
  }

  /**
   * Returns a BigInt whose value is this + 1.
   *
   * @return this + 1
   */
  public BigInt increment() {
    return add(ONE);
  }

  /**
   * Adds 1 to val
   *
   * @param val Array of integers containing value
   * @return val + 1
   */
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

  /**
   * Returns a BigInt whose value is this + 1.
   *
   * @return this - 1
   */
  public BigInt decrement() {
    return subtract(ONE);
  }

  /**
   * Checks if this BigInt is odd or even
   *
   * @return True if BigInt is odd
   */
  public boolean isOdd() {
    return ((mag[mag.length - 1] & 1) != 0);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Bit operations
  //------------------------------------------------------------------------------------------------

  /**
   * Returns number of bits in a 2's complement representation, excluding the sign bit.
   *
   * @return Number of bits.
   */
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

  /**
   * Returns the index of the rightmost 1 bit in this BigInt (the number of trailing 0s)
   *
   * @return Index of lowest set bit
   */
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

  /**
   * Returns {@code true} if the {@code n}th bit is set
   *
   * @param n Index of bit to test
   * @return {@code true} if the {@code n}th bit is set
   */
  public boolean testBit(int n) {
    if (n < 0)
      throw new ArithmeticException("Negative bit address");

    return (getInt(n >>> 5) & (1 << (n & 31))) != 0;
  }

  /**
   * Generates array of random bytes of specified bit length.
   *
   * @param numBits Number of bits to be randomly generated
   * @param rnd Random generator
   * @return Random byte array
   */
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

  /**
   * Left shifts current BigInt object
   *
   * @param n Number of digits to shift
   * @return this << n
   */
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

  /**
   * Left shift implementation.
   *
   * @param mag Array of integers to shift
   * @param n Number of digits to shift
   * @return mag << n
   */
  // Adapted from the java.lang BigInteger library
  private static int[] shiftLeft(int[] mag, int n) {
    if (mag.length == 0)
      return mag;

    int nInts = n >>> 5;
    int nBits = n & 0x1f;
    int magLen = mag.length;
    int newMag[];

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

  /**
   * Arithmetic right shifts current BigInt object
   *
   * @param n Number of digits to shift
   * @return this >> n
   */
  public BigInt shiftRight(int n) {
    if (sign == 0)
      return ZERO;
    if (n > 0) {
      return shiftRightImpl(n);
    } else if (n == 0) {
      return this;
    } else {
      return new BigInt(shiftLeft(mag, -n), sign);
    }
  }

  /**
   * Right shift implementation.
   *
   * @param n Number of digits to shift
   * @return this >> n
   */
  // Adapted from the java.lang BigInteger library
  private BigInt shiftRightImpl(int n) {
    int nInts = n >>> 5;
    int nBits = n & 0x1f;
    int magLen = mag.length;
    int newMag[];

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

  /**
   * Returns a BigInt whose value is {@code this AND val}
   *
   * @param val Right hand side of {@code AND} operation
   * @return {@code this AND val}
   */
  public BigInt and(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          & val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  /**
   * Returns a BigInt whose value is {@code this OR val}
   *
   * @param val Right hand side of {@code OR} operation
   * @return {@code this OR val}
   */
  public BigInt or(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          | val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  /**
   * Returns a BigInt whose value is {@code this XOR val}
   *
   * @param val Right hand side of {@code XOR} operation
   * @return {@code this XOR val}
   */
  public BigInt xor(BigInt val) {
    final int len = Math.max((bitLength() >>> 5) + 1, (val.bitLength() >>> 5) + 1);
    int[] result = new int[len];
    for (int i = 0; i < result.length; ++i)
      result[i] = (getInt(result.length - i - 1)
          ^ val.getInt(result.length - i - 1));
    return new BigInt(result);
  }

  /**
   * Returns a BigInt whose value is {@code ~this}
   *
   * @return {@code ~this}
   */
  public BigInt not() {
    int[] result = new int[(bitLength() >>> 5) + 1];
    for (int i = 0; i < result.length; ++i)
      result[i] = ~getInt(result.length-i-1);
    return new BigInt(result);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Addition and Subtraction
  //------------------------------------------------------------------------------------------------

  /**
   * Returns a BigInt whose value is {@code this + val}.
   * @param val Right hand side of addition
   * @return {@code this + val}
   */
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

  /**
   * Returns integer array whose value is {@code x + y}
   *
   * @param x Left hand side of addition
   * @param y Right hand side of addition
   * @return {@code x + y}
   */
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

    // Copy remainder of longer number
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

  /**
   * Returns a BigInt whose value is {@code this - val}.
   * @param val Right hand side of subtraction
   * @return {@code this - val}
   */
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

  /**
   * Returns a positive integer array whose value is {@code large - small}.
   * Precondition: {@code large > small}
   *
   * @param large Left hand side of subtraction
   * @param small Right hand side of subtraction
   * @return {@code large - small}
   */
  private static int[] subtract(int[] large, int[] small) {
    assert compareMagnitude(large, small) >= 0;
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

  /**
   * Returns a BigInt whose value is {@code this * val}
   *
   * @param val Right hand side of multiplication
   * @return {@code this * val}
   */
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

      int[] result = multiplyTextbook(mag, xlen, val.mag, ylen);
      result = stripLeadingZeros(result);
      return new BigInt(result, resultSign);
    } else {
      return multiplyKaratsuba(val);
    }
  }

  /**
   * Multiplies integer array {@code x} by integer {@code y}.
   *
   * @param x Integer array to be multiplied with
   * @param y Integer to be multiplied by
   * @param sign Sign of resulting BigInt
   * @return {@code x * y}
   */
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

  /**
   * Multiplies integer array {@code x} with integer array {@code y}
   * using grade school textbook multiplication.
   *
   * @param x Left hand side of multiplication
   * @param xlen Length of {@code x}
   * @param y Right hand side of multiplication
   * @param ylen Length of {@code y}
   * @return {@code x * y}
   */
  private static int[] multiplyTextbook(int[] x, int xlen, int[] y, int ylen) {
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

  /**
   * Multiplies {@code this} with {@code val} using Karatsuba's multiplication algorithm.
   *
   * @param val Right hand side of multiplication
   * @return {@code this * val}
   */
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

  /**
   * Returns a BigInteger whose value is {@code this / val}
   * @param val Right hand side of division
   * @return {@code this / val}
   */
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

  /**
   * Returns a BigInteger whose value is {@code this / val}
   * @param val Right hand side of division
   * @return {@code this / val}
   */
  public BigInt divide(long val) {
    return divide(new BigInt(val));
  }

  /**
   * Returns a BigInteger whose value is {@code this % val}
   * @param val Right hand side of remainder operation
   * @return {@code this % val}
   */
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

  /**
   * Returns a BigInteger whose value is {@code this % val}
   * @param val Right hand side of remainder operation
   * @return {@code this % val}
   */
  public BigInt remainder(long val) {
    return remainder(new BigInt(val));
  }

  /**
   * Divides {@code this} by {@code val}, then returns an array of BigInt containing
   * the quotient ({@code this / val}) and the remainder ({@code this % val}).
   *
   * @param val Right hand side of division
   * @return Array of BigInt containing the quotient and the remainder
   */
  BigInt[] divideAndRemainder(BigInt val) {
    BigInt dividend = new BigInt(this);
    BigInt divisor = new BigInt(val);
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
   * @return GCD of {@code this} and {@code val}
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
   * Computes LCM with GCD, where LCM = {@code (this / GCD) * val}
   *
   * @param val Value to be LCMed with
   * @return LCM of {@code this} and {@code val}
   */
  public BigInt lcm(BigInt val) {
    BigInt gcd = this.gcd(val);
    if (gcd.equals(ZERO))
      return ZERO;
    return this.divide(gcd).multiply(val);
  }

  /**
   * Checks if {@code this} and {@code val} are coprimes
   * @param val Value to be checked with
   * @return {@code true} if {@code this} and {@code val} are coprimes
   */
  public boolean isCoprime(BigInt val) {
    return (this.gcd(val).equals(ONE));
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Modular arithmetic
  //------------------------------------------------------------------------------------------------

  /**
   * Returns a BigInteger whose value is {@code this MOD val}
   *
   * @param val Right hand side of modulus
   * @return {@code this MOD val}
   */
  public BigInt mod(BigInt val) {
    BigInt b = remainder(val);
    return (b.sign >= 0 ? b : b.add(val));
  }

  /**
   * Returns a BigInteger whose value is {@code this MOD val}
   *
   * @param val Right hand side of modulus
   * @return {@code this MOD val}
   */
  public BigInt mod(long val) {
    return mod(new BigInt(val));
  }

  /**
   * Finds the modular multiplicative inverse of this using extended Euclidean algorithm.
   * Assumes this and modulus are coprimes.
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


  /**
   * Returns a random probable prime number of specified bit length.
   *
   * @param bitLength Bit length of random prime
   * @return Random prime number
   */
  public static BigInt probablePrime(int bitLength) {
    return probablePrime(bitLength, random);
  }

  /**
   * Returns a random probable prime number of specified bit length.
   *
   * @param bitLength Bit length of random prime
   * @param random Random number generator
   * @return Random prime number
   */
  public static BigInt probablePrime(int bitLength, Random random) {
    if (bitLength < 2)
      throw new ArithmeticException("bitLength < 2");

    return generatePrime(bitLength, DEFAULT_PRIME_CERTAINTY, random);
  }

  /**
   * Generates a random probable prime number.
   *
   * @param bitLength Bit length of random prime
   * @param certainty Certainty of Miller-Rabbin test
   * @param rnd Random number generator
   * @return Random prime number
   */
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

  /**
   * Tests if {@code this} is a prime number. Test is more accurate
   * the higher {@code certainty} is.
   *
   * @return {@code true} if {@code this} passes the Miller-Rabbin test.
   */
  public boolean isProbablePrime() {
    return isProbablePrime(DEFAULT_PRIME_CERTAINTY, random);
  }

  /**
   * Tests if {@code this} is a prime number. Test is more accurate
   * the higher {@code certainty} is.
   *
   * @param certainty Certainty value.
   * @param random Random number generator
   * @return {@code true} if {@code this} passes the Miller-Rabbin test.
   */
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

  /**
   * Tests if {@code this} is a prime number. Test is more accurate
   * the higher {@code certainty} is.
   *
   * @param certainty Certainty value
   * @param random Random number generator
   * @return {@code true} if {@code this} passes the Miller-Rabbin test.
   */
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

  /**
   * Tests if {@code this} is a prime number using the Miller-Rabbin test.
   *
   * @param iterations Number of passes done with the Miller-Rabbin test.
   * @param rnd Random number generator
   * @return {@code true} if {@code this} passes the Miller-Rabbin test.
   */
  private boolean passesMillerRabin(int iterations, Random rnd) {
    // Find a and m such that m is odd and this == 1 + 2 ^ a * m
    BigInt thisMinusOne = this.decrement();
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
   * Generates a random BigInt in range [0, n - 1].
   *
   * @param n Upper limit (exclusive)
   * @param rnd RNG
   * @return Random BigInt
   */
  // Reference: http://stackoverflow.com/a/2290089
  public static BigInt getRandomBigInt(BigInt n, Random rnd) {
    BigInt r;
    do {
      r = new BigInt(n.bitLength(), rnd);
    } while (r.compareTo(n) >= 0);
    return r;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Exponents
  //------------------------------------------------------------------------------------------------

  /**
   * Performs modular exponentiaton (this ^ exp % mod)
   *
   * @param exp Exponent, must be non-negative
   * @param mod Modulus
   * @return this ^ exp % mod
   */
  // Reference: http://www.geeksforgeeks.org/modular-exponentiation-power-in-modular-arithmetic/
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
