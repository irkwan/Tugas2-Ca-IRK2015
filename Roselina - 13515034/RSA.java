public class RSA {

	private BigNum p;
	private BigNum q;
	private BigNum n;
	private BigNum phi;
	private BigNum e;
	private BigNum d;
	private int bitLength = 320;

	public RSA() {
		p = BigNum.probablePrime(bitLength);
		q = BigNum.probablePrime(bitLength);
		n = p.multiply(q);
		phi = (p.subtract(BigNum.ONE)).multiply(q.subtract(BigNum.ONE));
		e = BigNum.probablePrime(bitLength / 2);
		d = e.modInverse(phi);
	}

	public BigNum encrypt(BigNum message) {
		return message.modPow(e, n);
	}

	public BigNum decrypt(BigNum encrypted) {
		return encrypted.modPow(d, n);
	}
	
	public String modulusToString() {
		String s = "" + n;
		return s;
	}
	
	public String privKeyToString() {
		String s = "" + d;
		return s;
	}
	
	public String pubKeyToString() {
		String s = "" + e;
		return s;
	}

	public static void main(String[] args) {
		Message m = new Message("D:\\Roselina_2\\Java Program\\RSA\\src\\external_file\\message.txt");
		double startTime, stopTime, elapsedTime;
		startTime = System.currentTimeMillis();
		RSA rsa = new RSA();
		BigNum message = m.getMessage();
		BigNum encrypt = rsa.encrypt(message);
		stopTime = System.currentTimeMillis();
		elapsedTime = (stopTime - startTime) / 1000;
		BigNum decrypt = rsa.decrypt(encrypt);
		System.out.println(rsa);
		System.out.println("encrypted code = " + encrypt);
		System.out.println("decrypted code = " + decrypt);
		System.out.println("decrypted text = " + Message.decodeMessage(decrypt));
		System.out.println("Execution Time = " + elapsedTime + " s");
	}

}