package rsa;

import java.io.File;
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

  public static RSAKeyPair generateKeyPair (BigNumber prime1, BigNumber prime2, BigNumber publicExponent) throws RSAException {
    BigNumber phi;

    int size = prime1.getSize();
    prime1 = prime1.resize(size * 2);
    prime2 = prime2.resize(size * 2);
    if (prime1.lessThan(prime2)) {
      BigNumber temp = prime1;
      prime1 = prime2;
      prime2 = temp;
    }
    BigNumber PRIME1_MINUS_ONE = prime1.subtract(BigNumber.ONE);
    BigNumber PRIME2_MINUS_ONE = prime2.subtract(BigNumber.ONE);
    phi = PRIME1_MINUS_ONE.multiply(PRIME2_MINUS_ONE);

    if (publicExponent.greatestCommonDivisor(phi).notEqualTo(BigNumber.ONE)) {
      throw new RSAException("Public exponent and phi are not coprimes");
    }

    BigNumber modulus = prime1.multiply(prime2);
    BigNumber privateExponent = publicExponent.modularInverse(phi);

    return new RSAKeyPair(new RSAPublicKey(modulus, publicExponent),
        new RSAPrivateKey(modulus, publicExponent, privateExponent,
            prime1.resize(size), prime2.resize(size),
            publicExponent.modularInverse(PRIME1_MINUS_ONE).resize(size),
            publicExponent.modularInverse(PRIME2_MINUS_ONE).resize(size),
            prime2.modularInverse(prime1).resize(size)));
  }

  public static RSAKeyPair fromPublicKeyFile (File file) throws IOException, ClassNotFoundException {
    try {
      return new RSAKeyPair(RSAPublicKey.fromFile(file), null);
    }
    catch (ClassCastException ex) {
      throw ex;
    }
  }

  public static RSAKeyPair fromPrivateKeyFile (File file) throws IOException, ClassNotFoundException {
    try {
      RSAPrivateKey privateKey = RSAPrivateKey.fromFile(file);
      return new RSAKeyPair(privateKey.toPublicKey(), privateKey);
    }
    catch (ClassCastException ex) {
      throw ex;
    }
  }

  public void savePublicKeyFile (File file) throws IOException {
    publicKey.toFile(file);
  }

  public void savePrivateKeyFile (File file) throws IOException {
    privateKey.toFile(file);
  }

  public Message encrypt (Message message, String label, String labelCharsetName, String hashAlgorithm)
      throws NoSuchAlgorithmException, IOException, RSAException {
    message.encodeLengthCheck(hashAlgorithm, publicKey.getModulusByteCount());
    return Message.fromBigNumber(publicKey.encryptionPrimitive(
        message.encode(label, labelCharsetName, hashAlgorithm, publicKey.getModulusByteCount())
            .toBigNumber()));
  }

  public Message decrypt (Message message, String label, String labelCharsetName, String hashAlgorithm)
      throws NoSuchAlgorithmException, IOException, RSAException {
    message.decodeLengthCheck(hashAlgorithm, privateKey.getModulusByteCount());
    return Message.fromBigNumber(privateKey.decryptionPrimitive(message.toBigNumber()))
        .decode(label, labelCharsetName, hashAlgorithm, privateKey.getModulusByteCount());
  }

  public RSAPublicKey getPublicKey () {
    return publicKey;
  }

  public RSAPrivateKey getPrivateKey () {
    return privateKey;
  }

  public boolean hasPrivateKey () {
    return privateKey != null;
  }

}
