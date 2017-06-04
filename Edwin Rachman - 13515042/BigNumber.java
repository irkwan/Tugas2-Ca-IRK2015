import java.util.ArrayList;

public class BigNumber {
  private long[] parts;

  public BigNumber (long value) {
    parts = new long[2];
    parts[0] = value;
    parts[1] = value > 0 ? 0 : -1;
  }

  public boolean isNegative () {
    return parts[1] < 0;
  }

  public String getHexString () {
    return "0x" + String.format("%016X", parts[1]) + String.format("%016X", parts[0]);
  }

  public static void main (String[] args) {
    System.out.println((new BigNumber(-17)).getHexString());
  }
}
