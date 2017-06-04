import java.util.ArrayList;

public class BigNumber {
  private int[] parts;

  public BigNumber (int n) {
    parts = new int[n];
    for (int i = 0; i < n; ++i) {
      parts[i] = 0;
    }
  }

  public BigNumber (int n, int value) {
    parts = new int[n];
    parts[0] = value;
    for (int i = 1; i < n; ++i) {
      parts[i] = 0;
    }
  }

  public boolean isNegative () {
    return parts[parts.length - 1] < 0;
  }

  public String getHexString () {
    StringBuilder builder = new StringBuilder();
    for (int i = parts.length - 1; i >= 0; --i) {
      builder.append(String.format("%08X ", parts[i]));
    }
    return builder.toString();
  }

  public BigNumber add (int n, BigNumber other) {
    BigNumber result = new BigNumber(n);
    int carry = 0;
    for (int i = 0; i < n; ++i) {
      long sum = (i < parts.length ? Integer.toUnsignedLong(parts[i]) : 0) + (i < other.parts.length ? Integer.toUnsignedLong(other.parts[i]) : 0) + carry;
      result.parts[i] = (int) sum;
      carry = Long.compareUnsigned(sum, 0xFFFFFFFL) > 0 ? 1 : 0;
    }
    return result;
  }
}
