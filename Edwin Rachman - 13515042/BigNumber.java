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
    System.arraycopy(obj.parts, 0, parts, 0, obj.parts.length);
  }

  public BigNumber(BigNumber obj) {
    parts = new int[obj.parts.length];
    System.arraycopy(obj.parts, 0, parts, 0, obj.parts.length);
  }

  public BigNumber arithmeticExtend (int size) {
    if (size > getSize()) {
      BigNumber result = new BigNumber(size);
      System.arraycopy(parts, 0, result.parts, 0, parts.length);
      boolean isNeg = parts[getSize() - 1] < 0;
      for (int i = getSize(); i < result.getSize(); ++i) {
        result.parts[i] = isNeg ? -1 : 0;
      }
      return result;
    }
    else {
      return this;
    }
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
    return (shift + 1 < getSize() ? Integer.toUnsignedLong(parts[shift + 1]) << 32 : 0)| Integer.toUnsignedLong(parts[shift]);
  }

  public long getPartAsLong(int shift) {
    return shift < getSize() ? Integer.toUnsignedLong(parts[shift]) : 0;
  }

  public void setPart(int value, int shift) {
    parts[shift] = value;
  }

  public int getPart(int shift) {
    return parts[shift];
  }

  public BigNumber getParts(int start, int length) {
    BigNumber result = new BigNumber(length);
    System.arraycopy(parts, start, result.parts, 0, length);
    return result;
  }

  public static BigNumber partShift(BigNumber x, int shift) {
    BigNumber result = new BigNumber(x.getSize());
    for (int i = 0; i < shift; ++i) {
      result.parts[i] = 0;
    }
    System.arraycopy(x.parts, 0, result.parts, shift, x.getSize() - shift);
    return result;
  }

  public static BigNumber bitShift(BigNumber x, int shift) {
    if (shift < 32) {
      BigNumber result = new BigNumber(x.getSize());
      for (int i = 0; i < result.getSize(); ++i) {
        result.setLongPart(x.getPartAsLong(i) << shift | result.getPartAsLong(i), i);
      }
      return result;
    }
    else {
      return x.partShift(shift / 32).bitShift(shift % 32);
    }
  }

  public static BigNumber inverse(BigNumber x) {
    BigNumber result = new BigNumber(x);
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = ~result.parts[i];
    }
    return result;
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


  public static BigNumber add (BigNumber x, long y) {
    BigNumber result = new BigNumber(x.getSize());
    result.setLongPart(x.getPartAsLong(0) + y, 0);
    for (int i = 1; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) + result.getPartAsLong(i), i);
    }
    return result;
  }

  public static BigNumber add(BigNumber x, BigNumber y) {
    BigNumber result = new BigNumber(Math.max(x.getSize(), y.getSize()));
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) + y.getPartAsLong(i) + result.getPartAsLong(i), i);
    }
    return result;
  }

  public static BigNumber negate(BigNumber x) {
    return inverse(x).add(1);
  }

  public static BigNumber subtract(BigNumber x, BigNumber y) {
    return x.add(y.negate());
  }

  public static BigNumber multiply(BigNumber x, long y) {
    BigNumber result = new BigNumber(x.getSize());
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) * y + result.getPartAsLong(i), i);
    }
    return result;
  }

  public static BigNumber multiplyExpand(BigNumber x, long y) {
    BigNumber result = new BigNumber(x.getSize() + 1);
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) * y + result.getPartAsLong(i), i);
    }
    return result;
  }

  public static BigNumber multiply(BigNumber x, BigNumber y) {
    BigNumber result = new BigNumber(Math.max(x.getSize(), y.getSize()));
    for (int i = 0; i < result.getSize(); ++i) {
      result = result.add(x.multiply(y.getPartAsLong(i)).partShift(i));
    }
    return result;
  }

  public static BigNumber[] divideAndModulo (BigNumber x, BigNumber y) {
    int size = Math.max(x.getSize(), y.getSize());
    BigNumber quotient = new BigNumber(size, 0);
    BigNumber remainder = new BigNumber(size, 0);
    BigNumber divisor = new BigNumber(size, y);

    for (int i = x.getSize() * 32 - 1; i >= 0; --i) {
      remainder = remainder.bitShift(1);
      if (x.getBit(i)) {
        remainder.setBit(0);
      }
      if (remainder.moreOrEqualTo(divisor)) {
        remainder = remainder.subtract(divisor);
        quotient.setBit(i);
      }
    }

    return new BigNumber[]{quotient, remainder};
  }

  public static int compare (BigNumber x, BigNumber y) {
    for (int i = x.getSize() - 1; i >= 0; --i) {
      if (x.getPartAsLong(i) < y.getPartAsLong(i)) {
        return -1;
      }
      else if (x.getPartAsLong(i) > y.getPartAsLong(i)) {
        return 1;
      }
    }
    return 0;
  }

  public static boolean lessThan (BigNumber x, BigNumber y) {
    return compare(x, y) < 0;
  }

  public static boolean lessOrEqualTo (BigNumber x, BigNumber y) {
    return compare(x, y) <= 0;
  }

  public static boolean equalTo (BigNumber x, BigNumber y) {
    return compare(x, y) <= 0;
  }

  public static boolean moreOrEqualTo (BigNumber x, BigNumber y) {
    return compare(x, y) >= 0;
  }

  public static boolean moreThan (BigNumber x, BigNumber y) {
    return compare(x, y) > 0;
  }

  public BigNumber inverse() {
    return inverse(this);
  }

  public BigNumber partShift(int shift) {
    return partShift(this, shift);
  }

  public BigNumber bitShift (int shift) {
    return bitShift(this, shift);
  }

  public BigNumber add (long other) {
    return add(this, other);
  }

  public BigNumber add(BigNumber other) {
    return add(this, other);
  }

  public BigNumber negate() {
    return negate(this);
  }

  public BigNumber subtract(BigNumber other) {
    return subtract(this, other);
  }

  public BigNumber multiply(long other) {
    return multiply(this, other);
  }

  public BigNumber multiplyExpand(long other) {
    return multiplyExpand(this, other);
  }

  public BigNumber multiply(BigNumber other) {
    return multiply(this, other);
  }

  public BigNumber[] divideAndModulo (BigNumber other) {
    return divideAndModulo(this, other);
  }

  public BigNumber divide (BigNumber other) {
    return this.divideAndModulo(other)[0];
  }

  public BigNumber modulo (BigNumber other) {
    return this.divideAndModulo(other)[1];
  }

  public boolean lessThan (BigNumber other) {
    return lessThan(this, other);
  }

  public boolean lessOrEqualTo (BigNumber other) {
    return lessOrEqualTo(this, other);
  }

  public boolean equalTo (BigNumber other) {
    return equalTo(this, other);
  }

  public boolean moreOrEqualTo (BigNumber other) {
    return moreOrEqualTo(this, other);
  }

  public boolean moreThan (BigNumber other) {
    return moreThan(this, other);
  }


  /*public static BigNumber multiply (BigNumber x, BigNumber y) {
    if (x.getSize() == 1 || y.getSize() == 1) {
      return new BigNumber(2, x.getPartAsLong(0) * y.getPartAsLong(0));
    }
    else {


      BigNumber z0 = multiply(x0, y0);
      BigNumber z2 = multiply(x1, y1);
      BigNumber z1 = multiply(x0.add(x1), y0.add(y1)).subtract(z0.add(z1));

      return z2.partShift(m * 2).add(z1.partShift(m)).add(z0);
    }
  }*/


  public static BigNumber generateRandom(int size) {
    BigNumber result = new BigNumber(size);
    Random random = new Random();
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = random.nextInt();
    }
    return result;
  }

  public static void main(String[] args) {
    BigNumber n = BigNumber.generateRandom(16);
    BigNumber d = new BigNumber(16, BigNumber.generateRandom(4));
    BigNumber[] qd = n.divideAndModulo(d);
    System.out.println("n = " + n.toHexString());
    System.out.println("d = " + d.toHexString());
    System.out.println("q = " + qd[0].toHexString());
    System.out.println("r = " + qd[1].toHexString());
    System.out.println("n = d * q + r = " + d.multiply(qd[0]).add(qd[1]).toHexString());

    System.out.println(new BigNumber(1, 10).modulo(new BigNumber(1, 3)).toHexString());
  }
}
