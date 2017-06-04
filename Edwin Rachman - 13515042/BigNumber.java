import java.util.ArrayList;

public class BigNumber {
  private int[] parts;

  public BigNumber (int size) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = 0;
    }
  }

  public BigNumber (int size, int value) {
    parts = new int[size];
    parts[0] = value;
    for (int i = 1; i < size; ++i) {
      parts[i] = 0;
    }
  }

  public BigNumber (int size, BigNumber obj) {
    parts = new int[size];
    for (int i = 0; i < size; ++i) {
      parts[i] = i < obj.parts.length ? obj.parts[i] : 0;
    }
  }

  public boolean isNegative () {
    return parts[parts.length - 1] < 0;
  }

  public String toHexString () {
    StringBuilder builder = new StringBuilder();
    for (int i = parts.length - 1; i >= 0; --i) {
      builder.append(String.format("%08X ", parts[i]));
    }
    return builder.toString();
  }

  public BigNumber add (int size, BigNumber other) {
    BigNumber result = new BigNumber(size);
    int carry = 0;
    for (int i = 0; i < size; ++i) {
      long sum = (i < parts.length ? Integer.toUnsignedLong(parts[i]) : 0) + (i < other.parts.length ? Integer.toUnsignedLong(other.parts[i]) : 0) + carry;
      result.parts[i] = (int) sum;
      carry = sum > 0xFFFFFFFFL ? 1 : 0;
    }
    return result;
  }

  public BigNumber inverse (int size) {
    BigNumber result = new BigNumber(size, this);
    for (int i = 0; i < size; ++i) {
      result.parts[i] = ~result.parts[i];
    }
    return result;
  }

  public BigNumber negate (int size) {
    return inverse(size).add(size, new BigNumber(size, 1));
  }

  public static void main (String[] args) {
    BigNumber x = new BigNumber(4, 0xFFFFFFFF);
    System.out.println(x.add(4, x).toHexString());
  }
}
