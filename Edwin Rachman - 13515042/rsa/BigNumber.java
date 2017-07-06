package rsa;

import java.security.SecureRandom;
import java.util.Random;

@SuppressWarnings("ALL")
public class BigNumber {
  private int[] parts;

  public BigNumber(int size, int value, int shift) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = i == shift ? value : 0;
    }
  }

  public BigNumber(int size, int value) {
    this(size, value, 0);
  }

  public BigNumber(int size) {
    this(size, 0, 0);
  }

  public BigNumber(int[] parts) {
    this.parts = parts.clone();
  }

  public BigNumber (int size, BigNumber obj) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = 0;
    }
    System.arraycopy(obj.parts, 0, parts, 0, size < obj.parts.length ? size : obj.parts.length);
  }

  public BigNumber(BigNumber obj) {
    parts = new int[obj.parts.length];
    System.arraycopy(obj.parts, 0, parts, 0, obj.parts.length);
  }

  public boolean moreThan(BigNumber y) {
    return compare(y) > 0;
  }

  public boolean moreOrEqualTo(BigNumber y) {
    return compare(y) >= 0;
  }

  public boolean equalTo(BigNumber y) {
    return compare(y) == 0;
  }

  public boolean lessOrEqualTo(BigNumber y) {
    return compare(y) <= 0;
  }

  public boolean lessThan(BigNumber y) {
    return compare(y) < 0;
  }

  public boolean isNegative () {
    return getBit(getSize() * 32 - 1) == true;
  }

  public BigNumber inverse() {
    BigNumber result = new BigNumber(this);
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = ~result.parts[i];
    }
    return result;
  }

  public BigNumber leftPartShift(int shift) {
    BigNumber result = new BigNumber(getSize());
    for (int i = 0; i < shift; ++i) {
      result.parts[i] = 0;
    }
    System.arraycopy(parts, 0, result.parts, shift, getSize() - shift);
    return result;
  }

  public BigNumber rightPartShift(int shift) {
    BigNumber result = new BigNumber(getSize());
    for (int i = shift; i < getSize(); ++i) {
      result.parts[i] = 0;
    }
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
      builder.append(String.format("%08X ", parts[i]));
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
    return add(y.negate());
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
    BigNumber quotient = new BigNumber(getSize(), 0);
    BigNumber remainder = new BigNumber(getSize(), 0);
    BigNumber divisor = new BigNumber(getSize(), y);

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
    BigNumber ZERO = new BigNumber(getSize() * 2, 0);
    BigNumber ONE = new BigNumber(getSize() * 2, 1);

    if (modulus.equalTo(ONE)) {
      return ZERO;
    }
    else {
      BigNumber result = ONE;
      BigNumber modu = new BigNumber(getSize() * 2, modulus);
      BigNumber base = new BigNumber(getSize() * 2, this).modulo(modu);
      BigNumber exp = new BigNumber(getSize() * 2, exponent);

      while (exp.moreThan(ZERO)) {
        if (exp.getBit(0) == true) {
          result = result.multiply(base).modulo(modu);
        }
        base = base.multiply(base).modulo(modu);
        exp = exp.rightBitShift(1);
      }
      return new BigNumber(getSize(), result);
    }
  }

  public BigNumber modularInverse (BigNumber modulus) {
    BigNumber ZERO = new BigNumber(getSize(), 0);
    BigNumber ONE = new BigNumber(getSize(), 1);

    BigNumber t0 = ZERO;
    BigNumber t1 = ONE;
    BigNumber r0 = modulus;
    BigNumber r1 = this;

    while (!r1.equalTo(ZERO)) {
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
      return null;
    }
    if (t0.isNegative()) {
      t0 = t0.add(modulus);
    }
    return t0;
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
    BigNumber maxValue = getMaxValue(bound.getSize());
    BigNumber result = generateRandom(bound.getSize());

    if (bound.equalTo(maxValue)) {
      return result;
    }
    else {
      BigNumber modulus = bound.add(new BigNumber(1, 1));
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

  public static BigNumber getMaxValue (int size) {
    BigNumber result = new BigNumber(size);
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = 0xFFFFFFFF;
    }
    return result;
  }

  public boolean isProbablePrime (int k) {
    BigNumber ONE = new BigNumber(getSize(), 1);
    BigNumber TWO = new BigNumber(getSize(), 2);
    BigNumber N_MINUS_ONE = subtract(ONE);
    BigNumber d = N_MINUS_ONE;
    int r = 0;
    while (d.getBit(0) == false) {
      d = d.rightBitShift(1);
      ++r;
    }

    witnessLoop: for (int i = 0; i < k; ++i) {
      BigNumber a = generateRandom(TWO, subtract(TWO));
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

  public BigNumber greatestCommonDivisor (BigNumber other) {
    BigNumber ZERO = new BigNumber(getSize(), 0);
    BigNumber a = this;
    BigNumber b = other;

    while (!b.equalTo(ZERO)) {
      BigNumber temp = b;
      b = a.modulo(b);
      a = temp;
    }

    return a;
  }

  public static BigNumber generatePrime (BigNumber lowerBound, BigNumber upperBound) {
    BigNumber TWO = new BigNumber(upperBound.getSize(), 2);
    BigNumber result = generateRandom(lowerBound, upperBound);
    result.setBit(0);
    while (!result.isProbablePrime(10)) {
      result = result.add(TWO);
    }

    return result;
  }

  public static void main(String[] args) {
/*
    rsa.BigNumber base = new rsa.BigNumber(new int[] {0x2, 0x0, 0x0, 0x0});
    System.out.println(base.toHexString());

    rsa.BigNumber exponent = new rsa.BigNumber(new int[] {0x10001, 0x0, 0x0, 0x0});
    System.out.println(exponent.toHexString());

    rsa.BigNumber modulus = new rsa.BigNumber(new int[] {0x998ddef1, 0x4b7f11ba, 0x0, 0x0});
    System.out.println(modulus.toHexString());

    rsa.BigNumber result = base.modularExponent(exponent, modulus);
    System.out.println(result.toHexString());

*/

    BigNumber ONE = new BigNumber(32, 1);

    BigNumber a = new BigNumber(16, 0);
    BigNumber b = getMaxValue(16);

    BigNumber p = new BigNumber(32, generatePrime(a, b));
    System.out.println(p.toHexString());

    BigNumber q = new BigNumber(32, generatePrime(a, b));
    System.out.println(q.toHexString());

    BigNumber n = p.multiply(q);
    System.out.println(n.toHexString());

    BigNumber phi = new BigNumber(32, p.subtract(ONE).multiply(q.subtract(ONE)));
    System.out.println(phi.toHexString());

    BigNumber e = new BigNumber(32, 0x10001);
    System.out.println(e.toHexString());

    System.out.println(e.greatestCommonDivisor(phi).toHexString());

    BigNumber d = e.modularInverse(phi);
    System.out.println(d.toHexString());

    BigNumber m = new BigNumber(32, 123);
    System.out.println(m.toHexString());

    BigNumber c = m.modularExponent(e, n);
    System.out.println(c.toHexString());

    BigNumber out = c.modularExponent(d, n);
    System.out.println(out.toHexString());
  }
}


