/*
 * Nama : Catherine Almira
 * NIM  : 13515111
 */

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.ArrayList;


public class RSAGenerator {
  private BigInteger primeP;
  private BigInteger primeQ;
  private BigInteger enKey; //kunci enkripsi
  private BigInteger deKey; //kunci dekripsi

  private static BigInteger one = new BigInteger("1");

  /*
   * Konstruktor tanpa parameter.
   * Tidak digenerate secara random.
   */

  public RSAGenerator() {
    
    primeP = new BigInteger("47");
    primeQ = new BigInteger("71");
    enKey = new BigInteger("79");
    deKey = enKey.modInverse((primeP.subtract(one).multiply(primeQ.subtract(one))));
  }

  /*
   * Konstruktor dengan parameter n sebagai jumlah digit dari primeP dan primeQ yang diharapkan. 
   */

  public RSAGenerator(int n) {
    primeP = BigInteger.generateRandomPrime(n);
    primeQ = BigInteger.generateRandomPrime(n);
    while (primeP.compareTo(primeQ) == 0) {
      primeQ = BigInteger.generateRandomPrime(n);
    }
    BigInteger nMin = (primeP.subtract(one)).multiply(primeQ.subtract(one));
    BigInteger temp = new BigInteger("3");
    BigInteger two = new BigInteger("2");
    BigInteger four = new BigInteger("4");
    BigInteger six = new BigInteger("6");
    enKey = new BigInteger("3");
    while (BigInteger.gcd(nMin, enKey).compareTo(one) != 0) {
      if (temp.compareTo(one) == 0) {
      	temp = new BigInteger("5");
      	enKey = enKey.add(four);
      } else {
        temp = BigInteger.mod(temp.add(two), six);
        enKey = enKey.add(two);
      }
    }
    deKey = enKey.modInverse(nMin);
  }

  /*
   * Membaca file yang bernama filename dan menyimpan dalam array of byte.
   */

  public static byte[] readFile(String filename) throws IOException {
    File file = new File(filename);
    FileInputStream fin = null;
    byte[] fileContent = new byte[1];
    try {
      fin = new FileInputStream(file);
      fileContent = new byte[(int)file.length()];
      fin.read(fileContent);
      //String s = new String(fileContent);
      //System.out.println("File content: " + fileContent);
      fin.close();
    } catch (IOException e) {
      System.out.println("File not found!");
    }
    return fileContent;
  }

  /*
   * Mengembalikan BigInteger yang berasal dari array byte.
   */

  public static BigInteger convertASCIIToBigInt(byte[] fileContent) {
    //String s = fileContent.toString();
    //System.out.println(s);
  	String s = "";
  	for (int i = 0; i < fileContent.length; i++) {
  	  s = s + Byte.toString((byte)fileContent[i]);
  	}
  	BigInteger temp = new BigInteger(s);
  	return temp;
  }

  

  /*
   * Mengembalikan BigInteger hasil enkripsi message.
   */

  public BigInteger encrypt(BigInteger message) {
    BigInteger modulus = primeP.multiply(primeQ);
    return BigInteger.modPow(message, enKey, modulus);
  }

  /*
   * Mengembalikan BigInteger hasil dekripsi encrypted message.
   */

  public BigInteger decrypt(BigInteger encrypted) {
  	BigInteger modulus = primeP.multiply(primeQ);
  	return BigInteger.modPow(encrypted, deKey, modulus);
  }

  /*
   * Mengembalikan String hasil enkripsi message.
   */

  public String encrypt(String message){
	return this.encrypt(new BigInteger(message)).toString();
  }

  /*
   * Mengembalikan String hasil dekripsi encrypted message.
   */

  public String decrypt(String message){
    return this.decrypt(new BigInteger(message)).toString();
  }

  public static void main(String args[]) {
  	try {
  	  byte[] fileContent = readFile("sample.txt");
  	  BigInteger content = convertASCIIToBigInt(fileContent);
  	  ArrayList<BigInteger> contentBlock = content.convertToSmallBlock(3);
  	  RSAGenerator rsa = new RSAGenerator();
  	  ArrayList<BigInteger> encrypted = new ArrayList<BigInteger>();
  	  ArrayList<BigInteger> decrypted = new ArrayList<BigInteger>();
  	  //Proses enkripsi
  	  for (int i = 0; i < contentBlock.size(); i++) {
        encrypted.add(rsa.encrypt(contentBlock.get(i)));
  	  }
  	  //Preses dekripsi
  	  for (int i = 0; i < encrypted.size(); i++) {
  	  	//System.out.println(encrypted.get(i));
  	  	decrypted.add(rsa.decrypt(encrypted.get(i)));
  	  }
  	  
  	  //String decrypted = rsa.decrypt(encrypted);
  	  System.out.println(decrypted);
  	} catch (IOException e) {
      System.out.println("File not found!");
    }

  	

  }
}