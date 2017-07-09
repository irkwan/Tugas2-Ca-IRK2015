package rsa;

public class RSAPrivateKey {
  private int version;
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
    version = 0;
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

  public int getModulusByteCount () {
    return modulus.getSize() * 4;
  }

  public void writeToFile () {

  }
}
