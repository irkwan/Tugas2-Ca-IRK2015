package rsa;

import java.io.*;

public class RSAPrivateKey implements Serializable {
  private BigNumber modulus;
  private BigNumber publicExponent;
  private BigNumber privateExponent;
  private BigNumber prime1;
  private BigNumber prime2;
  private BigNumber exponent1;
  private BigNumber exponent2;
  private BigNumber coefficient;

  public RSAPrivateKey (BigNumber modulus, BigNumber publicExponent, BigNumber privateExponent, BigNumber prime1,
                        BigNumber prime2, BigNumber exponent1, BigNumber exponent2, BigNumber coefficient) {
    this.modulus = modulus;
    this.publicExponent = publicExponent;
    this.privateExponent = privateExponent;
    this.prime1 = prime1;
    this.prime2 = prime2;
    this.exponent1 = exponent1;
    this.exponent2 = exponent2;
    this.coefficient = coefficient;
  }

  public BigNumber decryptionPrimitive(BigNumber cipherText) {
    return cipherText.modularExponent(privateExponent, modulus);
  }

  /*
  public BigNumber decryptionChineseRemainderTheorem (BigNumber cipherText) {
    BigNumber m1 = cipherText.modularExponent(exponent1.resize(exponent1.getSize() * 2), prime1);
    BigNumber m2 = cipherText.modularExponent(exponent2.resize(exponent1.getSize() * 2), prime2);
    return m2.add(coefficient.multiply(m1.subtract(m2)).modulo(prime1).multiply(prime2));
  }*/

  public int getModulusByteCount () {
    return modulus.getSize() * 4;
  }

  public static RSAPrivateKey fromFile (File file) throws IOException, ClassNotFoundException, ClassCastException {
    return (RSAPrivateKey) new ObjectInputStream(new FileInputStream(file)).readObject();
  }

  public RSAPublicKey toPublicKey () {
    return new RSAPublicKey(modulus, publicExponent);
  }

  public void toFile (File file) throws IOException {
    new ObjectOutputStream(new FileOutputStream(file)).writeObject(this);
  }

  public String getModulusText () {
    return modulus.toHexString();
  }

  public String getPublicExponentText () {
    return publicExponent.toHexString();
  }

  public String getPrivateExponentText () {
    return privateExponent.toHexString();
  }

  public String getPrime1Text () {
    return prime1.toHexString();
  }

  public String getPrime2Text () {
    return prime2.toHexString();
  }

  public String getExponent1Text () {
    return exponent1.toHexString();
  }

  public String getExponent2Text () {
    return exponent2.toHexString();
  }

  public String getCoefficientText () {
    return coefficient.toHexString();
  }
}
