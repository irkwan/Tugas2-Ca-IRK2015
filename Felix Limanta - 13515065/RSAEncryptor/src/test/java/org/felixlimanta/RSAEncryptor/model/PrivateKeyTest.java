package org.felixlimanta.RSAEncryptor.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

/**
 * Created by ASUS on 13/06/17.
 */
class PrivateKeyTest {
  private static final String p =
      "12999910267413858403214049473633934369787578921569819488357727589584166282593258159411740883416115339605886529549598089495119767779678082089585641392188727";
  private static final String q =
      "11713730107176914057728506114628096949692188058005611184299027533258765664492032852766551912293752506420885675974405432381466841270896702618593481920111563";
  private static final String n =
      "152277440290004001082278011592257033856328641788327808535932362799781616368273699549188820435983209722936450002648917387912801278134450008131805049707190450631787999020165677348898633049273749402721024487593105552496283336257725784550414161201828447732788886974085448467394827357197210581015367612952590950301";
  private static final String e = "65537";
  private static final String d =
      "54032618753130262953260509644508311241161395859845110755738577150731977323283347516160511235310916741718810390872320206863576869896052118865695111592167169072639332473157522482281830645359553941183374847946675859041653061507916455771972525620002468875296607146567842645301639283071278314004163599249019033015";


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

  @Test
  void recoverPQ() {
    PrivateKey key = new PrivateKey(new BigInt(n), new BigInt(e), new BigInt(d));

    BigInteger actP = new BigInteger(key.getP().toBinaryString(), 2);
    assertEquals(p, actP.toString(), "P mismatch");

    BigInteger actQ = new BigInteger(key.getQ().toBinaryString(), 2);
    assertEquals(q, actQ.toString(), "Q mismatch");
  }

  @Test
  void encodeDeocde() {
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