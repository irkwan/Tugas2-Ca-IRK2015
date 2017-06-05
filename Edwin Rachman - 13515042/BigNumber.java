import java.util.ArrayList;

public class BigNumber {
  private int[] parts;

  public BigNumber (int size, int value, int shift) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = i == shift ? value : 0;
    }
  }

  public BigNumber (int size, int value) {
    this(size, value, 0);
  }

  public BigNumber (int size) {
    this(size, 0, 0);
  }

  public BigNumber (BigNumber obj) {
    parts = new int[obj.parts.length];
    System.arraycopy(obj.parts, 0, parts, 0, obj.parts.length);
  }

  public int getSize () {
    return parts.length;
  }

  public String toHexString () {
    StringBuilder builder = new StringBuilder();
    for (int i = getSize() - 1; i >= 0; --i) {
      builder.append(String.format("%08X ", parts[i]));
    }
    return builder.toString();
  }

  public void setLongPart (long value, int shift) {
    parts[shift] = (int) value;
    if (shift + 1 < getSize()) {
      parts[shift + 1] = (int) (value >>> 32);
    }
  }

  public long getPartAsLong (int shift) {
    return shift < getSize() ? Integer.toUnsignedLong(parts[shift]) : 0;
  }

  public static BigNumber add (BigNumber x, BigNumber y) {
    BigNumber result = new BigNumber(Math.max(x.getSize(), y.getSize()));
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) + y.getPartAsLong(i) + result.getPartAsLong(i), i);
    }
    return result;
  }

  public BigNumber add (BigNumber other) {
    return add(this, other);
  }

  public static BigNumber multiply (BigNumber x, long y) {
    BigNumber result = new BigNumber(x.getSize());
    for (int i = 0; i < result.getSize(); ++i) {
      result.setLongPart(x.getPartAsLong(i) * y + result.getPartAsLong(i), i);
    }
    return result;
  }

  public BigNumber multiply (long other) {
    return multiply(this, other);
  }

  public static BigNumber multiply (BigNumber x, BigNumber y) {
    BigNumber result = new BigNumber(Math.max(x.getSize(), y.getSize()));
    for (int i = 0; i < result.getSize(); ++i) {
      result = result.add(x.multiply(y.getPartAsLong(i)).shift(i));
    }
    return result;
  }

  public BigNumber multiply (BigNumber other) {
    return multiply(this, other);
  }

  public static BigNumber inverse (BigNumber x) {
    BigNumber result = new BigNumber(x);
    for (int i = 0; i < result.getSize(); ++i) {
      result.parts[i] = ~result.parts[i];
    }
    return result;
  }

  public BigNumber inverse () {
    return inverse(this);
  }

  public static BigNumber negate (BigNumber x) {
    return inverse(x).add(new BigNumber(1, 1));
  }

  public BigNumber negate () {
    return negate(this);
  }

  public static BigNumber substract (BigNumber x, BigNumber y) {
    return x.add(y.negate());
  }

  public BigNumber substract (BigNumber other) {
    return substract(this, other);
  }

  public static BigNumber shift (BigNumber x, int shift) {
    BigNumber result = new BigNumber(x);
    for (int i = 0; i < shift; ++i) {
      result.parts[i] = 0;
    }
    System.arraycopy(x.parts, 0, result.parts, shift, result.getSize() - shift);
    return result;
  }

  public BigNumber shift (int shift) {
    return shift(this, shift);
  }

  /*public static BigNumber multiply (BigNumber x, BigNumber y) {
    if (x.getSize() == 1 || y.getSize() == 1) {
      return new BigNumber(2, x.getPartAsLong(0) * y.getPartAsLong(0));
    }
    else {


      BigNumber z0 = multiply(x0, y0);
      BigNumber z2 = multiply(x1, y1);
      BigNumber z1 = multiply(x0.add(x1), y0.add(y1)).substract(z0.add(z1));

      return z2.shift(m * 2).add(z1.shift(m)).add(z0);
    }
  }*/

  public static void main (String[] args) {
    BigNumber x = new BigNumber(16, -1);
    x = x.multiply(x);
    x = x.multiply(x);
    x = x.multiply(x);
    x = x.multiply(x);
    x = x.substract(new BigNumber(16, -2));
    System.out.println(x.toHexString());
  }
}
