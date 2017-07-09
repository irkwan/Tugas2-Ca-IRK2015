package rsa;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;


@SuppressWarnings("ALL")
public class RSAKeyPair {

  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

  private RSAKeyPair (RSAPublicKey publicKey, RSAPrivateKey privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  public static RSAKeyPair generateKeyPair (int bits) {
    BigNumber lowerBound = BigNumber.ZERO;
    BigNumber upperBound = BigNumber.getMaxValue(bits / 64);
    BigNumber prime1, prime2, phi;
    BigNumber publicExponent = BigNumber.fromInt(65537);

    do {
      prime1 = BigNumber.generatePrime(lowerBound, upperBound);
      prime2 = BigNumber.generatePrime(lowerBound, upperBound);
      prime1 = prime1.resize(prime1.getSize() * 2);
      prime2 = prime2.resize(prime2.getSize() * 2);
      phi = prime1.subtract(BigNumber.ONE).multiply(prime2.subtract(BigNumber.ONE));
    }
    while (!publicExponent.greatestCommonDivisor(phi).equalTo(BigNumber.ONE));

    BigNumber modulus = prime1.multiply(prime2);
    BigNumber privateExponent = publicExponent.modularInverse(phi);

    return new RSAKeyPair(new RSAPublicKey(modulus, publicExponent),
        new RSAPrivateKey(modulus, publicExponent, privateExponent, prime1, prime2,
            null, null, null));
  }

  public Message encrypt (Message message, String label, Charset labelCharset, String hashAlgorithm) throws NoSuchAlgorithmException, IOException, RSAException {
    return Message.fromBigNumber(publicKey.encryptionPrimitive(
        message.encode(label, labelCharset, hashAlgorithm, publicKey.getModulusByteCount())
            .toBigNumber()));
  }

  public Message decrypt (Message message, String label, Charset labelCharset, String hashAlgorithm) throws NoSuchAlgorithmException, IOException, RSAException {
    return Message.fromBigNumber(privateKey.decryptionPrimitive(message.toBigNumber()))
        .decode(label, labelCharset, hashAlgorithm, privateKey.getModulusByteCount());
  }

  public static void main (String[] args) throws Exception {
    Charset charset = Charset.forName("UTF-8");
    String algorithm = "SHA-1";
    RSAKeyPair rsaKeyPair = RSAKeyPair.generateKeyPair(512);

    Message plainText = Message.fromString("test", charset);
    System.out.println(plainText.toString(charset));

    Message cipherText1 = rsaKeyPair.encrypt(plainText, "", charset, algorithm);
    System.out.println(cipherText1.toHexString());

    Message cipherText2 = rsaKeyPair.encrypt(plainText, "", charset, algorithm);
    System.out.println(cipherText2.toHexString());

    Message output1 = rsaKeyPair.decrypt(cipherText1, "", charset, algorithm);
    System.out.println(output1.toString(charset));

    Message output2 = rsaKeyPair.decrypt(cipherText2, "", charset, algorithm);
    System.out.println(output2.toString(charset));
  }

}
