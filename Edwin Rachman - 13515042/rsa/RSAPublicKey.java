package rsa;

import java.io.*;

public class RSAPublicKey implements Serializable {
  private BigNumber modulus;
  private BigNumber publicExponent;

  public RSAPublicKey (BigNumber modulus, BigNumber publicExponent) {
    this.modulus = modulus;
    this.publicExponent = publicExponent;
  }

  public BigNumber encryptionPrimitive(BigNumber plainText) {
    return plainText.modularExponent(publicExponent, modulus);
  }

  public int getModulusByteCount () {
    return modulus.getSize() * 4;
  }

  public String getModulusText () {
    return modulus.toHexString();
  }

  public String getPublicExponentText () {
    return publicExponent.toHexString();
  }

  public static RSAPublicKey fromFile (File file) throws IOException, ClassNotFoundException {
    return (RSAPublicKey) new ObjectInputStream(new FileInputStream(file)).readObject();
  }

  public void toFile (File file) throws IOException {
    new ObjectOutputStream(new FileOutputStream(file)).writeObject(this);
  }
}
