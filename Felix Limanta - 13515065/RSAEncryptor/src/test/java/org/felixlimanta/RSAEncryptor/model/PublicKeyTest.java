package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 13/06/17.
 */
class PublicKeyTest {
  private static final String nStr =
      "86468221314284253641269699516965004963310878720668294892080973630779878458113190898959535278476674868478369808967838022740732742788381128803652403208205595648027627154129744348408490564661211487162153402490810206180153476233951742673411340721231551690270500956247763595344890849123432636361058193538756577707";
  private static final String eStr = "65537";

  @Test
  void encodeDeocde() {
    BigInt e = new BigInt(eStr);
    BigInt n = new BigInt(nStr);

    PublicKey pub = new PublicKey(n, e);
    String xml = pub.toXmlString();

    PublicKey pub2 = PublicKey.fromXmlString(xml);
    assertAll(
        () -> assertEquals(e.toString(), pub2.getE().toString(), "Different e"),
        () -> assertEquals(n.toString(), pub2.getN().toString(), "Different n")
    );
  }
}