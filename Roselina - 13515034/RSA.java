public class RSA {

	private BigNum p;
	private BigNum q;
	private BigNum n;
	private BigNum phi;
	private BigNum e;
	private BigNum d;
	private int bitLength;

	public RSA(int messageLength) {
		bitLength = messageLength * 8;
		p = BigNum.probablePrime(bitLength);
		q = BigNum.probablePrime(bitLength);
		n = p.multiply(q);
		phi = (p.subtract(BigNum.ONE)).multiply(q.subtract(BigNum.ONE));
		e = BigNum.probablePrime(bitLength / 2);
		while (phi.gcd(e).compareTo(BigNum.ONE) > 0 && e.compareTo(phi) < 0) {
			e.add(BigNum.ONE);
		}
		d = e.modInverse(phi);
	}

	public BigNum encrypt(BigNum message) {
		return message.modPow(e, n);
	}

	public BigNum decrypt(BigNum encrypted) {
		return encrypted.modPow(d, n);
	}

	public String toString() {
		String s = "n = " + n + "\n" + "e = " + e + "\n" + "d = " + d;
		return s;
	}

	public static void main(String[] args) {
		Message m = new Message("message.txt");
		RSA rsa = new RSA(m.getMessageLength());
		BigNum message = m.getMessage();
		BigNum encrypt = rsa.encrypt(message);
		BigNum decrypt = rsa.decrypt(encrypt);
		System.out.print("encrypted code : ");
		encrypt.printHex();
		System.out.print("decrypted code : ");
		decrypt.printHex();
		System.out.println("decrypted text : " + m.decodeMessage(decrypt));
	}

}
