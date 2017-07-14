package rsa;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.SecureRandom;
import java.util.Random;

@SuppressWarnings("ALL")
public class BigNumber implements Serializable {
  public static final BigNumber ZERO = fromInt(0);
  public static final BigNumber ONE = fromInt(1);
  public static final BigNumber TWO = fromInt(2);
  //<editor-fold desc="Description">
  private static int[] SMALL_PRIMES = new int[]{
    3, 5, 7, 11, 13, 17, 19,
    23, 29, 31, 37, 41, 43, 47, 53,
    59, 61, 67, 71, 73, 79, 83, 89,
    97, 101, 103, 107, 109, 113, 127, 131,
    137, 139, 149, 151, 157, 163, 167, 173,
    179, 181, 191, 193, 197, 199, 211, 223,
    227, 229, 233, 239, 241, 251, 257, 263,
    269, 271, 277, 281, 283, 293, 307, 311,
    313, 317, 331, 337, 347, 349, 353, 359,
    367, 373, 379, 383, 389, 397, 401, 409,
    419, 421, 431, 433, 439, 443, 449, 457,
    461, 463, 467, 479, 487, 491, 499, 503,
    509, 521, 523, 541, 547, 557, 563, 569,
    571, 577, 587, 593, 599, 601, 607, 613,
    617, 619, 631, 641, 643, 647, 653, 659,
    661, 673, 677, 683, 691, 701, 709, 719,
    727, 733, 739, 743, 751, 757, 761, 769,
    773, 787, 797, 809, 811, 821, 823, 827,
    829, 839, 853, 857, 859, 863, 877, 881,
    883, 887, 907, 911, 919, 929, 937, 941,
    947, 953, 967, 971, 977, 983, 991, 997,
  };
  //</editor-fold>
  private int[] parts;

  private BigNumber(int size) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = 0;
    }
  }

  private BigNumber(int[] parts) {
    this.parts = parts.clone();
  }

  public static BigNumber fromInt (int value) {
    BigNumber result = new BigNumber(1);
    result.parts[0] = value;
    return result;
  }

  public static BigNumber fromIntArray (int[] array) {
    return new BigNumber(array);
  }

  public static BigNumber fromByteArray (byte[] array) {
    if (array.length == 0) {
      return ZERO;
    }
    else {
      IntBuffer intBuffer = ByteBuffer.wrap(array).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
      int[] parts = new int[intBuffer.capacity()];
      int bufferSize = intBuffer.capacity();
      for (int i = 0; i < bufferSize; ++i) {
        parts[i] = intBuffer.get(bufferSize - i - 1);
      }
      return new BigNumber(parts);
    }
  }

  public static BigNumber generateRandom (int size) {
    BigNumber result = new BigNumber(size);
    Random random = new SecureRandom();
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = random.nextInt();
    }
    return result;
  }

  public static BigNumber generateRandom (BigNumber bound) {
    BigNumber maxValue = getMaxValue(bound.getSize() * 32);
    BigNumber result = generateRandom(bound.getSize());

    if (bound.equalTo(maxValue)) {
      return result;
    }
    else {
      BigNumber modulus = bound.add(ONE);
      BigNumber uniformBound = maxValue.subtract(maxValue.modulo(modulus));

      while (result.moreOrEqualTo(uniformBound)) {
        result = generateRandom(bound.getSize());
      }
      return result.modulo(modulus);
    }
  }

  public static BigNumber generateRandom (BigNumber lowerBound, BigNumber upperBound) {
    return generateRandom(upperBound.subtract(lowerBound)).add(lowerBound);
  }

  public static BigNumber getMaxValue (int bits) {
    BigNumber result = new BigNumber((bits + 31) / 32);
    for (int i = 0; i < result.getSize() - 1; ++i) {
      result.parts[i] = 0xFFFFFFFF;
    }
    result.parts[result.getSize() - 1] = 0xFFFFFFFF >>> (32 - bits % 32);
    return result;
  }

  public byte[] toByteArray () {
    ByteBuffer byteBuffer = ByteBuffer.allocate(getSize() * 4);
    for (int i = 0; i < getSize(); ++i) {
      byteBuffer.asIntBuffer().put(getSize() - i - 1, getPart(i));
    }
    byte[] result = new byte[byteBuffer.remaining()];
    byteBuffer.get(result);
    return result;
  }

  public BigNumber resize (int size) {
    BigNumber result = new BigNumber(size);
    System.arraycopy(parts, 0, result.parts, 0, size < getSize() ? size : getSize());
    return result;
  }

  public BigNumber copy () {
    BigNumber result = new BigNumber(getSize());
    System.arraycopy(parts, 0, result.parts, 0, getSize());
    return result;
  }

  public boolean moreThan (BigNumber y) {
    return compare(y) > 0;
  }

  public boolean moreOrEqualTo (BigNumber y) {
    return compare(y) >= 0;
  }

  public boolean equalTo (BigNumber y) {
    return compare(y) == 0;
  }

  public boolean notEqualTo (BigNumber y) {
    return compare(y) != 0;
  }

  public boolean lessOrEqualTo (BigNumber y) {
    return compare(y) <= 0;
  }

  public boolean lessThan (BigNumber y) {
    return compare(y) < 0;
  }

  public boolean isNegative () {
    return getBit(getSize() * 32 - 1) == true;
  }

  public BigNumber inverse() {
    BigNumber result = this.copy();
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = ~result.parts[i];
    }
    return result;
  }

  public BigNumber leftPartShift(int shift) {
    BigNumber result = new BigNumber(getSize());
    System.arraycopy(parts, 0, result.parts, shift, getSize() - shift);
    return result;
  }

  public BigNumber rightPartShift(int shift) {
    BigNumber result = new BigNumber(getSize());
    System.arraycopy(parts, shift, result.parts, 0, getSize() - shift);
    return result;
  }

  public int compare(BigNumber y) {
    for (int i = getSize() - 1; i >= 0; --i) {
      if (getPartAsLong(i) < y.getPartAsLong(i)) {
        return -1;
      }
      else if (getPartAsLong(i) > y.getPartAsLong(i)) {
        return 1;
      }
    }
    return 0;
  }

  public int getSize() {
    return parts.length;
  }

  public String toHexString() {
    StringBuilder builder = new StringBuilder();
    for (int i = getSize() - 1; i >= 0; --i) {
      builder.append(String.format("%08X", parts[i]));
    }
    return builder.toString();
  }

  public void setLongPart(long value, int shift) {
    parts[shift] = (int) value;
    if (shift + 1 < getSize()) {
      parts[shift + 1] = (int) (value >>> 32);
    }
  }

  public long getLongPart (int shift) {
    return (shift + 1 < getSize() ? Integer.toUnsignedLong(parts[shift + 1]) << 32 : 0) | Integer.toUnsignedLong(parts[shift]);
  }

  public long getPartAsLong(int shift) {
    return shift < getSize() ? Integer.toUnsignedLong(parts[shift]) : 0;
  }

  public void setPart(int value, int shift) {
    parts[shift] = value;
  }

  public int getPart(int shift) {
    return shift < getSize() ? parts[shift] : 0;
  }

  public BigNumber getParts(int start, int length) {
    BigNumber result = new BigNumber(length);
    System.arraycopy(parts, start, result.parts, 0, length);
    return result;
  }

  public BigNumber leftBitShift (int shift) {
    if (shift < 32) {
      BigNumber result = new BigNumber(getSize());
      for (int i = 0; i < result.getSize(); ++i) {
        result.setLongPart(getPartAsLong(i) << shift | result.getPartAsLong(i), i);
      }
      return result;
    }
    else {
      return leftPartShift(shift / 32).leftBitShift(shift % 32);
    }
  }

  public BigNumber rightBitShift (int shift) {
    if (shift < 32) {
      BigNumber result = new BigNumber(getSize());
      for (int i = 0; i < result.getSize(); ++i) {
        result.setLongPart(getLongPart(i) >> shift, i);
      }
      return result;
    }
    else {
      return rightPartShift(shift / 32).rightBitShift(shift % 32);
    }
  }

  public boolean getBit (int shift) {
    return (getPart(shift / 32) >> (shift % 32) & 1) == 1;
  }

  public void setBit (int shift) {
    setPart(getPart(shift / 32) | (1 << (shift % 32)), shift / 32);
  }

  public void clearBit (int shift) {
    setPart(getPart(shift / 32) & ~(1 << (shift % 32)), shift / 32);
  }

  public BigNumber add (long y) {
    BigNumber result = new BigNumber(getSize());
    result.setLongPart(getPartAsLong(0) + y, 0);
    for (int i = 1; i < result.getSize(); ++i) {
      result.setLongPart(getPartAsLong(i) + result.getPartAsLong(i), i);
    }
    return result;
  }

  public BigNumber add (BigNumber y) {
    BigNumber result = new BigNumber(getSize());
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(getPartAsLong(i) + y.getPartAsLong(i) + result.getPartAsLong(i), i);
    }
    return result;
  }

  public BigNumber negate () {
    return inverse().add(1);
  }

  public BigNumber subtract (BigNumber y) {
    return add(y.resize(getSize()).negate());
  }

  public BigNumber multiply (long y) {
    BigNumber result = new BigNumber(getSize());
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(getPartAsLong(i) * y + result.getPartAsLong(i), i);
    }
    return result;
  }

  public BigNumber multiply (BigNumber y) {
    BigNumber result = new BigNumber(getSize());
    for (int i = 0; i < result.getSize(); ++i) {
      result = result.add(multiply(y.getPartAsLong(i)).leftPartShift(i));
    }
    return result;
  }

  public BigNumber[] divideAndModulo (BigNumber y) {
    if (y.equalTo(ZERO)) {
      throw new ArithmeticException("Divide by zero error");
    }

    BigNumber quotient = ZERO.resize(getSize());
    BigNumber remainder = ZERO.resize(getSize());
    BigNumber divisor = y.resize(getSize());

    for (int i = getSize() * 32 - 1; i >= 0; --i) {
      remainder = remainder.leftBitShift(1);
      if (getBit(i)) {
        remainder.setBit(0);
      }
      if (remainder.moreOrEqualTo(divisor)) {
        remainder = remainder.subtract(divisor);
        quotient.setBit(i);
      }
    }

    return new BigNumber[]{quotient, remainder};
  }

  public BigNumber divide (BigNumber y) {
    return divideAndModulo(y)[0];
  }

  public BigNumber modulo (BigNumber y) {
    return divideAndModulo(y)[1];
  }

  public BigNumber modularExponent (BigNumber exponent, BigNumber modulus) {
    if (modulus.equalTo(ONE)) {
      return ZERO;
    }
    else {
      BigNumber result = ONE.resize(modulus.getSize() * 2);
      BigNumber modu = modulus.resize(modulus.getSize() * 2);
      BigNumber base = resize(modulus.getSize() * 2).modulo(modu);
      BigNumber exp = exponent.resize(modulus.getSize() * 2);

      while (exp.moreThan(ZERO)) {
        if (exp.getBit(0) == true) {
          result = result.multiply(base).modulo(modu);
        }
        base = base.multiply(base).modulo(modu);
        exp = exp.rightBitShift(1);
      }
      return result.resize(modulus.getSize());
    }
  }

  public BigNumber modularInverse (BigNumber modulus) {
    BigNumber t0 = ZERO.resize(modulus.getSize());
    BigNumber t1 = ONE.resize(modulus.getSize());
    BigNumber r0 = modulus;
    BigNumber r1 = resize(modulus.getSize());

    while (r1.notEqualTo(ZERO)) {
      BigNumber quotient = r0.divide(r1);
      BigNumber temp;
      temp = t1;
      t1 = t0.subtract(quotient.multiply(t1));
      t0 = temp;
      temp = r1;
      r1 = r0.subtract(quotient.multiply(r1));
      r0 = temp;
    }

    if (r0.moreThan(ONE)) {
      throw new ArithmeticException("Number not invertible error");
    }
    if (t0.isNegative()) {
      t0 = t0.add(modulus);
    }
    return t0;
  }

  public BigNumber greatestCommonDivisor (BigNumber other) {
    BigNumber a = this;
    BigNumber b = other;

    while (b.notEqualTo(ZERO)) {
      BigNumber temp = b;
      b = a.modulo(b);
      a = temp;
    }

    return a;
  }

  public boolean isPrimeSmallPrimesTest (int limit) {
    for (int i = 0; i < limit; ++i) {
      if (modulo(fromInt(SMALL_PRIMES[i])).equalTo(ZERO)) {
        return false;
      }
    }
    return true;
  }

  public boolean isPrimeMillerRabinTest (int k) {
    BigNumber N_MINUS_ONE = subtract(ONE);
    BigNumber d = N_MINUS_ONE;

    int r = 0;
    while (d.getBit(0) == false) {
      d = d.rightBitShift(1);
      ++r;
    }

    witnessLoop: for (int i = 0; i < k; ++i) {
      BigNumber a = BigNumber.generateRandom(TWO, subtract(TWO));
      BigNumber x = a.modularExponent(d, this);

      if (x.equalTo(ONE) || x.equalTo(N_MINUS_ONE)) {
        continue witnessLoop;
      }
      for (int j = 0; j < r - 1; ++j) {
        x = x.modularExponent(TWO, this);
        if (x.equalTo(ONE)) {
          return false;
        }
        else if (x.equalTo(N_MINUS_ONE)) {
          continue witnessLoop;
        }
      }
      return false;
    }

    return true;
  }
}


