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
    System.arraycopy(obj.parts, 0, parts, 0, obj.parts.length);
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
    BigNumber ZERO = new BigNumber(getSize(), 0);
    BigNumber ONE = new BigNumber(getSize(), 1);

    if (modulus.equalTo(ONE)) {
      return ZERO;
    }
    else {
      BigNumber result = ONE;
      BigNumber base = modulo(modulus);
      BigNumber exp = new BigNumber(getSize(), exponent);

      while (exp.moreThan(ZERO)) {
        if (exp.getBit(0) == true) {
          result = result.multiply(base);
          result = result.modulo(modulus);
        }
        base = base.multiply(base);
        base = base.modulo(modulus);
        exp = exp.rightBitShift(1);
      }
      return result;
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
    BigNumber ONE = new BigNumber(getSize() * 2, 1);
    BigNumber TWO = new BigNumber(getSize() * 2, 2);
    BigNumber n = new BigNumber(getSize() * 2, this);
    BigNumber N_MINUS_ONE = n.subtract(ONE);
    BigNumber d = N_MINUS_ONE;
    int r = 0;
    while (d.getBit(0) == false) {
      d = d.rightBitShift(1);
      ++r;
    }

    witnessLoop: for (int i = 0; i < k; ++i) {
      BigNumber a = generateRandom(TWO, n.subtract(TWO));
      BigNumber x = a.modularExponent(d, n);

      if (x.equalTo(ONE) || x.equalTo(N_MINUS_ONE)) {
        continue witnessLoop;
      }
      for (int j = 0; j < r - 1; ++j) {
        x = x.modularExponent(TWO, n);
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

  public static BigNumber generatePrime (BigNumber lowerBound, BigNumber upperBound) {
    BigNumber result;
    do {
      result = generateRandom(lowerBound, upperBound);
      result.setBit(0);
    }
    while (!result.isProbablePrime(10));

    return result;
  }

  public static void main(String[] args) {
    BigNumber a = new BigNumber(8, 0);
    BigNumber b = getMaxValue(8);

    BigNumber x = new BigNumber(16, generatePrime(a, b));
    System.out.println(x.toHexString());

    BigNumber y = new BigNumber(16, generatePrime(a, b));
    System.out.println(y.toHexString());

    System.out.println(x.multiply(y).toHexString());
  }
}
