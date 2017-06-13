package org.felixlimanta.RSAEncryptor.model;

/**
 * Created by ASUS on 13/06/17.
 */
public class PublicKey {
  private static String[] tagNames = new String[] {
      "Modulus", "Exponent"
  };

  private BigInt e;
  private BigInt n;

  //region Construction
  //------------------------------------------------------------------------------------------------

  public PublicKey(BigInt n, BigInt e) {
    this.e = e;
    this.n = n;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //------------------------------------------------------------------------------------------------

  public BigInt getE() {
    return e;
  }

  public BigInt getN() {
    return n;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region To and From XML
  //------------------------------------------------------------------------------------------------

  public static PublicKey fromXmlString(String xml) {
    BigInt[] values = XmlHelper.fromXmlString(xml, tagNames);
    if (values != null)
      return new PublicKey(values[0], values[1]);
    return null;
  }

  public String toXmlString() {
    BigInt[] values = new BigInt[] { n, e };
    return XmlHelper.toXmlString(tagNames, values);
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
