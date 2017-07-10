package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@code PublicKey}.
 * Testing is done with the java.lang.BigInteger library, where the results produced by
 * this BigInt library is compared to results produced by java.lang.BigInteger.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
class PublicKeyTest {
  private static final String nStr =
      "86468221314284253641269699516965004963310878720668294892080973630779878458113"
          + "19089895953527847667486847836980896783802274073274278838112880365240320820559"
          + "56480276271541297443484084905646612114871621534024908102061801534762339517426"
          + "73411340721231551690270500956247763595344890849123432636361058193538756577707";
  private static final String eStr = "65537";

  /**
   * Tests XML encoding and decoding.
   * Values parsed from XML must be equal to original values.
   */
  @Test
  void encodeDecode() {
    BigInt e = new BigInt(eStr);
    BigInt n = new BigInt(nStr);

    PublicKey pub = new PublicKey(n, e);
    String xml = pub.toXmlString();

    try {
      PublicKey pub2 = PublicKey.fromXmlString(xml);
      assertAll(
          () -> assertEquals(e.toString(), pub2.getE().toString(), "Different e"),
          () -> assertEquals(n.toString(), pub2.getN().toString(), "Different n")
      );
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}