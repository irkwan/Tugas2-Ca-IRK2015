package org.felixlimanta.RSAEncryptor.model;

import java.io.IOException;
import org.xml.sax.SAXException;

/**
 * Represents an RSA public key.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
public class PublicKey {
  private static final String[] tagNames = new String[] {
      "Modulus", "Exponent"
  };

  private BigInt e;
  private BigInt n;

  //region Construction
  //------------------------------------------------------------------------------------------------

  /**
   * Constructs a {@code PublicKey} object given a modulus and encryption exponent
   *
   * @param n Modulus
   * @param e Encryption exponent
   */
  public PublicKey(BigInt n, BigInt e) {
    this.e = new BigInt(e);
    this.n = new BigInt(n);
  }

  /**
   * Copy constructor
   *
   * @param publicKey Original {@code PublicKey} object to be copied
   */
  public PublicKey(PublicKey publicKey) {
    this.n = new BigInt(publicKey.n);
    this.e = new BigInt(publicKey.e);
  }

  /**
   * Constructs a {@code PublicKey} object from a {@code PrivateKey} object.
   *
   * @param privateKey Source {@code PrivateKey} object
   */
  public PublicKey(PrivateKey privateKey) {
    this.e = new BigInt(privateKey.getE());
    this.n = new BigInt(privateKey.getN());
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Comparison
  //------------------------------------------------------------------------------------------------

  /**
   * Checks equality between {@code this} and {@code o}
   *
   * @param o Object to be compared to
   * @return true if this == o
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PublicKey))
      return false;

    PublicKey publicKey = (PublicKey) o;
    return (e.equals(publicKey.e) && n.equals(publicKey.n));
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //------------------------------------------------------------------------------------------------

  /**
   * @return Encryption exponent
   */
  public BigInt getE() {
    return e;
  }

  /**
   * @return Encryption modulus
   */
  public BigInt getN() {
    return n;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region To and From XML
  //------------------------------------------------------------------------------------------------

  /**
   * Parses a {@code PublicKey} object from an XML string representation
   *
   * @param xml XML string to be converted to a {@code PublicKey}
   * @return A {@code PublicKey} object from the XML string
   * @throws SAXException Error in parsing XML
   * @throws IOException Error in parsing XML
   */
  public static PublicKey fromXmlString(String xml) throws SAXException, IOException {
    BigInt[] values = XmlHelper.rsaKeyFromXmlString(xml, tagNames);
    if (values != null)
      return new PublicKey(values[0], values[1]);
    return null;
  }

  /**
   * Converts this {@code PublicKey} object to an XML string representation
   *
   * @return An XML string representation of this {@code PublicKey} object
   */
  public String toXmlString() {
    BigInt[] values = new BigInt[] { n, e };
    return XmlHelper.rsaKeyToXmlString(tagNames, values);
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
