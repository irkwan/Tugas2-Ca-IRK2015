import com.endec.model.BigInteger;
import com.endec.model.RSA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
 * Nama File          : EnDec.java
 * Tanggal dibuat     : 14/07/17
 * Tanggal perubahan  : 14/07/17
 */

/**
 * Main Program.
 *
 * @author Jordhy Fernando
 */
public class EnDec {
  public static void main(String[] args) {
    System.out.println("=================================");
    System.out.println(" EnDec (Encryptor and Decryptor)");
    System.out.println("=================================");
    System.out.println();

    boolean end = false;
    while(!end) {
      System.out.println("1. Encrypt a text");
      System.out.println("2. Decrypt a text");
      System.out.println("3. Exit");
      System.out.println();
      System.out.print("Input: ");
      Scanner scanner = new Scanner(System.in);
      int input = scanner.nextInt();
      scanner.nextLine();
      System.out.println();

      String filename = null;
      String outFilename = null;
      BufferedReader br = null;
      PrintWriter out = null;
      long startTime;
      RSA rsa;
      String currLine;
      long endTime;
      long totalTime;
      BigInteger multiplier = new BigInteger(256);

      switch (input) {
        case 1:
          System.out.print("Enter filename: ");
          filename = scanner.nextLine();
          System.out.print("Enter output filename: ");
          outFilename = scanner.nextLine();
          try {
            br = new BufferedReader(new FileReader(filename));
            System.out.println();

            System.out.println("Generating keys...");
            startTime = System.currentTimeMillis();
            rsa = new RSA(20);
            System.out.println("Keys generated");
            System.out.println(rsa);
            System.out.println();

            System.out.println("Encrypting " + filename + "...");
            out = new PrintWriter(outFilename);
            while ((currLine = br.readLine()) != null) {
              for (int i = 0; i < currLine.length(); i++) {
                BigInteger plaintext = new BigInteger((int)currLine.charAt(i));
                plaintext = plaintext.add(BigInteger.random(new BigInteger(1),
                    new BigInteger(10000)).multiply(multiplier));
                out.print(rsa.encrypt(plaintext).toString());
                if (i < currLine.length() - 1) {
                  out.print(" ");
                }
              }
              out.println();
            }

            out.close();
            out = new PrintWriter("keys.endec");
            out.println(rsa.getN());
            out.println(rsa.getE());
            out.println(rsa.getD());
            out.close();

            endTime   = System.currentTimeMillis();
            totalTime = endTime - startTime;
            System.out.println(filename + " encrypted");

            System.out.println();
            System.out.println("Keys saved to keys.endec");
            System.out.println("Encrypted text saved to" + outFilename);
            System.out.println("");
            System.out.println("Execution time: " + totalTime + " ms");
          } catch (IOException e) {
            System.out.println("File does not exist");
          } finally {
            try {
              if (br != null) {
                br.close();
              }
              if (out != null) {
                out.close();
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          break;

        case 2:
          System.out.print("Enter filename: ");
          filename = scanner.nextLine();
          System.out.print("Enter output filename: ");
          outFilename = scanner.nextLine();
          System.out.print("Enter keys file: ");
          String keyFileName = scanner.nextLine();
          br = null;
          out = null;
          try {
            br = new BufferedReader(new FileReader(filename));
            BufferedReader key = new BufferedReader(new FileReader(keyFileName));
            Scanner temp = new Scanner(key);
            BigInteger n = new BigInteger(temp.nextLine());
            BigInteger e = new BigInteger(temp.nextLine());
            BigInteger d = new BigInteger(temp.nextLine());

            System.out.println();
            startTime = System.currentTimeMillis();
            System.out.println("Decrypting " + filename + "...");
            out = new PrintWriter(outFilename);
            Scanner sc = new Scanner(br);
            while (sc.hasNextLine()) {
              currLine = sc.nextLine();
              Scanner sc2 = new Scanner(currLine);
              while(sc2.hasNext()) {
                BigInteger ciphertext = new BigInteger(sc2.next());
                out.print((char)Integer.parseInt(RSA.decrypt(ciphertext, d, n)
                    .mod(multiplier).toString()));
              }
              out.println();
            }
            endTime   = System.currentTimeMillis();
            totalTime = endTime - startTime;
            System.out.println(filename + " decrypted");
            System.out.println();
            System.out.println("Decrypted text saved to " + outFilename);
            System.out.println("Execution time: " + totalTime + " ms");
          } catch (IOException e) {
            System.out.println("File does not exist");
          } finally {
            try {
              if (br != null) {
                br.close();
              }
              if (out != null) {
                out.close();
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          break;

        case 3:
          end = true;
          break;

        default:
          System.out.println("Invalid command!");
      }
      System.out.println();
    }
  }
}
