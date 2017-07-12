package main;

import java.math.BigInteger;
import java.util.Random;

public class RSA {

	private BigInteger p;
	private BigInteger q;
	private BigInteger n;
	private BigInteger phi;
	private BigInteger e;
	private BigInteger d;
	private int bitLength = 64;

	public RSA() {
		Random rand = new Random();
		p = BigInteger.probablePrime(bitLength, rand);
		q = BigInteger.probablePrime(bitLength, rand);
		n = p.multiply(q);
		phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = BigInteger.probablePrime(bitLength / 2, rand);
		while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
			e.add(BigInteger.ONE);
		}
		d = e.modInverse(phi);
	}

	public BigInteger encrypt(BigInteger message) {
		return message.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger encrypted) {
		return encrypted.modPow(d, n);
	}

	public String toString() {
		String s = "n = " + n + "\n" + "e = " + e + "\n" + "d = " + d;
		return s;
	}

	public static void main(String[] args) {
		RSA rsa = new RSA();
		Message m = new Message("D:\\Roselina_2\\Java Program\\RSA\\src\\external_file\\message.txt");
		BigInteger message = m.getMessage();
		BigInteger encrypt = rsa.encrypt(message);
		BigInteger decrypt = rsa.decrypt(encrypt);
		System.out.println("encrypted code : " + encrypt);
		System.out.println("decrypted code : " + decrypt);
		System.out.println("decrypted text : " + m.decodeMessage(decrypt));
	}

}