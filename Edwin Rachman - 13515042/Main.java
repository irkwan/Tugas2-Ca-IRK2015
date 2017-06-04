
public class Main {
  public static void main (String[] args) {
    BigNumber x = new BigNumber(1, 0xFFFFFFFF);
    System.out.println(x.add(3, x).add(4, x).add(4, x).getHexString());
  }
}
