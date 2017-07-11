/*
 * Nama : Catherine Almira
 * NIM  : 13515111
 */

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.Random;

import java.util.Scanner;

public class RSAGenerator {
  private BigInteger primeP;
  private BigInteger primeQ;
  private BigInteger modulus;
  private BigInteger enKey; //kunci enkripsi
  private BigInteger deKey; //kunci dekripsi

  private static BigInteger one = new BigInteger("1");
  private static BigInteger mul = new BigInteger("256");
  
  /*
   * Konstruktor tanpa parameter.
   * Memilih 2 bilangan prima secara acak (primeP, primeQ), mengenerate kunci enkripsi
   * (enKey) dan kunci dekripsi (deKey). 
   */

  public RSAGenerator() {
    primeP = BigInteger.getPrimeNumber();
    primeQ = BigInteger.getPrimeNumber();
    while (primeP.compareTo(primeQ) == 0) {
      primeQ = BigInteger.getPrimeNumber();
    }
    modulus = primeP.multiply(primeQ);
    System.out.println("Prime number 1 : " + primeP);
    System.out.println("Prime number 2 : " + primeQ);
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
    System.out.println("Public key : " + enKey);
    deKey = enKey.modInverse(nMin);
    System.out.println("Private key : " + deKey);
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

  public static BigInteger[] convertASCIIToBigInt2(byte[] fileContent) {
    BigInteger[] hasil = new BigInteger[fileContent.length];
    for (int i = 0; i < fileContent.length; i++) {
      hasil[i] = new BigInteger(Byte.toString((byte)fileContent[i]));
    }
    return hasil;
  }

  public static byte[] convertBigIntToASCII(BigInteger[] fileContent) {
  	byte[] hasil = new byte[fileContent.length];
  	for (int i = 0; i < fileContent.length; i++) {
  	  hasil[i] = Byte.valueOf(fileContent[i].toString());
  	}
  	return hasil;
  }

  public static String convertASCIIToText(byte[] message) {
  	String hasil = new String(message);
  	return hasil;
  }

  /*
   * Mengembalikan BigInteger hasil enkripsi message.
   */

  public BigInteger encrypt(BigInteger message) {
  	BigInteger sub = new BigInteger("255");
  	BigInteger temp = BigInteger.divide(modulus.subtract(sub), sub);
  	int len = temp.toString().length();
  	if (len > 0) {
  	  len--;
  	}
    return BigInteger.modPow(message.add(BigInteger.generateRandom(len).multiply(mul)), enKey, modulus);
  }

  /*
   * Mengembalikan BigInteger hasil dekripsi encrypted message.
   */

  public BigInteger decrypt(BigInteger encrypted) {
  	return BigInteger.mod(BigInteger.modPow(encrypted, deKey, modulus), mul);
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
  	  BigInteger[] content2 = convertASCIIToBigInt2(fileContent); //dalam bentuk array BigInteger
      //start timer
  	  long startTime = System.currentTimeMillis();
  	  //generate public key dan private key
      RSAGenerator rsa = new RSAGenerator();
      //enkripsi
      System.out.println("Encrypting...");
      BigInteger[] encrypted = new BigInteger[content2.length];
      for (int i = 0; i < content2.length; i++) {
        encrypted[i] = rsa.encrypt(content2[i]);
        System.out.println(encrypted[i]);
      }
      //stop timer (enkripsi selesai)
      long endTime = System.currentTimeMillis();
      System.out.println("Decrypting...");
      //dekripsi
      BigInteger[] decrypted = new BigInteger[encrypted.length];
      for (int i = 0; i < encrypted.length; i++) {
        decrypted[i] = rsa.decrypt(encrypted[i]);
        System.out.println(decrypted[i]);
      }
      System.out.println("Finish");
      System.out.println(convertASCIIToText(convertBigIntToASCII(decrypted)));
      System.out.println("Execution time " + (endTime - startTime) + " ms");
  	} catch (IOException e) {
      System.out.println("File not found!");
    }
    /*BigInteger b1 = new BigInteger("10007");
    BigInteger b2 = new BigInteger("10009");
    BigInteger b3 = new BigInteger("3");
    BigInteger nMin = (b1.subtract(one)).multiply(b2.subtract(one));
    System.out.println(nMin);
    System.out.println(BigInteger.divide(nMin, b3));
    BigInteger b = BigInteger.gcd(nMin, b3);
    System.out.println(b);*/
    //BigInteger b1 = new BigInteger("263718416706237632659782657716364816346912645179953791341");
    //BigInteger b2 = new BigInteger("1012312412");
    //BigInteger b3 = new BigInteger("123215123");
    //BigInteger b = BigInteger.generateRandomPrime(15);
  	//System.out.println(b);

  }
}