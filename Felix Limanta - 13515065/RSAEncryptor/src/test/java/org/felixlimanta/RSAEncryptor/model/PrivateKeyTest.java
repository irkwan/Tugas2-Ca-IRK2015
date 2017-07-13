package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for {@code PrivateKey}.
 * Testing is done with the java.lang.BigInteger library, where the results produced by
 * this BigInt library is compared to results produced by java.lang.BigInteger.
 *
 * @author Felix Limanta
 * @version 1.0
 * @since 2017-06-13
 */
class PrivateKeyTest {
  // Default key values
  private static final String p =
      "129999102674138584032140494736339343697875789215698194883577275895841662825932"
          + "58159411740883416115339605886529549598089495119767779678082089585641392188727";
  private static final String q =
      "117137301071769140577285061146280969496921880580056111842990275332587656644920"
          + "32852766551912293752506420885675974405432381466841270896702618593481920111563";
  private static final String n =
      "152277440290004001082278011592257033856328641788327808535932362799781616368273"
          + "69954918882043598320972293645000264891738791280127813445000813180504970719045"
          + "06317879990201656773488986330492737494027210244875931055524962833362577257845"
          + "50414161201828447732788886974085448467394827357197210581015367612952590950301";
  private static final String e = "65537";
  private static final String d =
      "54032618753130262953260509644508311241161395859845110755738577150731977323283"
          + "34751616051123531091674171881039087232020686357686989605211886569511159216716"
          + "90726393324731575224822818306453595539411833748479466758590416530615079164557"
          + "71972525620002468875296607146567842645301639283071278314004163599249019033015";


  /**
   * Generates keys, then tests if key is admissible with java.lang.BigInteger
   */
  @Test
  void generateKey() {
    PrivateKey key = new PrivateKey();

    BigInteger p = new BigInteger(key.getP().toBinaryString(), 2);
    BigInteger q = new BigInteger(key.getQ().toBinaryString(), 2);
    assertAll(
        () -> assertTrue(p.isProbablePrime(100), "P not prime"),
        () -> assertTrue(q.isProbablePrime(100), "Q not prime")
    );

    BigInteger n = new BigInteger(key.getN().toBinaryString(), 2);
    assertEquals(p.multiply(q).toString(), n.toString(), "N != P * Q");

    BigInteger p2 = p.subtract(BigInteger.ONE);
    BigInteger q2 = q.subtract(BigInteger.ONE);
    BigInteger l = new BigInteger(key.getLambda().toBinaryString(), 2);
    assertEquals(p2.divide(p2.gcd(q2)).multiply(q2).toString(), l.toString(), "L != LCM(P-1, Q-1)");

    BigInteger e = new BigInteger(key.getE().toBinaryString(), 2);
    assertAll(
        () -> assertTrue(e.gcd(p).equals(BigInteger.ONE), "E not coprime with P"),
        () -> assertTrue(e.gcd(q).equals(BigInteger.ONE), "E not coprime with Q")
    );

    BigInteger d = new BigInteger(key.getD().toBinaryString(), 2);
    assertEquals(e.modInverse(l).toString(), d.toString(), "D not modular inverse of E");

    BigInteger dP = new BigInteger(key.getdP().toBinaryString(), 2);
    BigInteger dQ = new BigInteger(key.getdQ().toBinaryString(), 2);
    assertAll(
        () -> assertEquals(d.modInverse(p.subtract(BigInteger.ONE)).toString(), dP.toString(), "dP != D^-1 mod P-1"),
        () -> assertEquals(d.modInverse(q.subtract(BigInteger.ONE)).toString(), dQ.toString(), "dQ != D^-1 mod Q-1")
    );

    BigInteger qInv = new BigInteger(key.getqInv().toBinaryString(), 2);
    assertEquals(q.modInverse(p).toString(), qInv.toString(), "qInv != Q^-1 mod P");

    System.out.println(p);
    System.out.println(q);
    System.out.println(n);
    System.out.println(e);
    System.out.println(d);
  }

  /**
   * Tests PQ recovery.
   * Recovered PQ must be equal to the original PQ values.
   */
  @Test
  void recoverPQ() {
    PrivateKey key = new PrivateKey(new BigInt(n), new BigInt(e), new BigInt(d));

    BigInteger actP = new BigInteger(key.getP().toBinaryString(), 2);
    assertEquals(p, actP.toString(), "P mismatch");

    BigInteger actQ = new BigInteger(key.getQ().toBinaryString(), 2);
    assertEquals(q, actQ.toString(), "Q mismatch");
  }

  /**
   * Tests XML encoding and decoding.
   * Values parsed from XML must be equal to original values.
   */
  @Test
  void encodeDecode() {
    PrivateKey key = new PrivateKey(new BigInt(n), new BigInt(e), new BigInt(d));
    String xml = key.toXmlString();

    try {
      PrivateKey prv = PrivateKey.fromXmlString(xml);
      assertAll(
          () -> assertEquals(n, prv.getN().toString(), "Different n"),
          () -> assertEquals(e, prv.getE().toString(), "Different e"),
          () -> assertEquals(d, prv.getD().toString(), "Different d"),
          () -> assertEquals(p, prv.getP().toString(), "Different d"),
          () -> assertEquals(q, prv.getQ().toString(), "Different d"),
          () -> assertEquals(key.getdP().toString(), prv.getdP().toString(), "Different dP"),
          () -> assertEquals(key.getdQ().toString(), prv.getdQ().toString(), "Different dQ"),
          () -> assertEquals(key.getqInv().toString(), prv.getqInv().toString(), "Different qInv")
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}