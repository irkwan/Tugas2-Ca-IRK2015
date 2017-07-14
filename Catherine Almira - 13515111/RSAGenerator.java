/*
 * Nama : Catherine Almira
 * NIM  : 13515111
 * Kelas RSA Generator adalah kelas yang merepresentasikan kunci-kunci yang
 * diperlukan dalam enkripsi dan dekripsi suatu pesan dengan algoritma kriptografi
 * RSA.
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
    primeP = BigInteger.generateRandomPrime(20);
    primeQ = BigInteger.generateRandomPrime(20);
    while (primeP.compareTo(primeQ) == 0) {
      primeQ = BigInteger.generateRandomPrime(20);
    }
    modulus = primeP.multiply(primeQ);
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

  public static byte[] readFile(File file) throws IOException {
    //File file = new File(filename);
    FileInputStream fin = null;
    byte[] fileContent = new byte[1];
    try {
      fin = new FileInputStream(file);
      fileContent = new byte[(int)file.length()];
      fin.read(fileContent);
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
   * Mengembalikan array of biginteger yang dikonversi dari ascii.
   */

  public static BigInteger[] convertASCIIToBigInt2(byte[] fileContent) {
    BigInteger[] hasil = new BigInteger[fileContent.length];
    for (int i = 0; i < fileContent.length; i++) {
      hasil[i] = new BigInteger(Byte.toString((byte)fileContent[i]));
    }
    return hasil;
  }

  /*
   * Mengembalikan array of byte yang dikonversi dari BigInteger.
   */

  public static byte[] convertBigIntToASCII(BigInteger[] fileContent) {
  	byte[] hasil = new byte[fileContent.length];
  	for (int i = 0; i < fileContent.length; i++) {
  	  hasil[i] = Byte.valueOf(fileContent[i].toString());
  	}
  	return hasil;
  }

  /*
   * Mengembalikan string hasil konversi dari ascii.
   */

  public static String convertASCIIToText(byte[] message) {
  	String hasil = new String(message);
  	return hasil;
  }

  /*
   * Mengembalikan BigInteger hasil enkripsi message.
   * message diubah menjadi suatu BigInteger baru yang BigInteger sebelumnya ditambah
   * dengan bilangan random yang dikali dengan 256.
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
   * BigInteger hasil dekripsi dimod dengan 256 agar menghasilkan nilai yang sama dengan
   * saat sebelum dienkripsi.
   */

  public BigInteger decrypt(BigInteger encrypted) {
  	return BigInteger.mod(BigInteger.modPow(encrypted, deKey, modulus), mul);
  }

  public String getPrimeP() {
  	return primeP.toString();
  }

  public String getPrimeQ() {
  	return primeQ.toString();
  }

  public String getEnKey() {
  	return enKey.toString();
  }

  public String getDeKey() {
  	return deKey.toString();
  }
}