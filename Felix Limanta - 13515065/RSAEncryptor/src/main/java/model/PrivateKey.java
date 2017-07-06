package org.felixlimanta.RSAEncryptor.model;

import java.io.IOException;
import java.security.SecureRandom;
import org.xml.sax.SAXException;

/**
 * Created by ASUS on 13/06/17.
 */
public class PrivateKey {
  private static final int BIT_LENGTH = 1024;
  private static final BigInt DEFAULT_E_VALUE = new BigInt("65537");
  private static SecureRandom random = new SecureRandom();

  private static final String[] tagNames = new String[] {
      "Modulus", "Exponent", "P", "Q", "DP", "DQ", "InverseQ", "D"
  };

  private BigInt n;
  private BigInt e;
  private BigInt d;
  private BigInt lambda;
  private BigInt p;
  private BigInt q;
  private BigInt dP;
  private BigInt dQ;
  private BigInt qInv;

  //region Construction
  //------------------------------------------------------------------------------------------------

  public PrivateKey() {
    generateKey();
  }

  public PrivateKey(BigInt n, BigInt e, BigInt d) {
    this.n = n;
    this.e = e;
    this.d = d;
    getPQFromNED();
    generateCrtKey();
  }

  public PrivateKey(BigInt p, BigInt q) {
    this.p = p;
    this.q = q;
    getNEDFromPQ();
    generateCrtKey();
  }

  public PrivateKey(BigInt p, BigInt q, BigInt dP, BigInt dQ,
      BigInt qInv) {
    this.p = p;
    this.q = q;
    this.dP = dP;
    this.dQ = dQ;
    this.qInv = qInv;
    getNEDFromPQ();
  }

  public PrivateKey(BigInt n, BigInt e, BigInt d, BigInt p,
      BigInt q, BigInt dP, BigInt dQ, BigInt qInv) {
    this.n = n;
    this.e = e;
    this.d = d;
    this.p = p;
    this.q = q;
    this.dP = dP;
    this.dQ = dQ;
    this.qInv = qInv;
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Getters
  //------------------------------------------------------------------------------------------------

  public BigInt getN() {
    return n;
  }

  public BigInt getE() {
    return e;
  }

  public BigInt getD() {
    return d;
  }

  public BigInt getP() {
    return p;
  }

  public BigInt getQ() {
    return q;
  }

  public BigInt getdP() {
    return dP;
  }

  public BigInt getdQ() {
    return dQ;
  }

  public BigInt getqInv() {
    return qInv;
  }

  public BigInt getLambda() {
    return lambda;
  }

  public PublicKey getPublicKey() {
    return new PublicKey(n, e);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region Key Generation
  //------------------------------------------------------------------------------------------------

  /**
   * Generates key values for RSA
   */
  private void generateKey() {
    // 4. Choose an integer e such that 1 < e < λ(n) and gcd(e, λ(n)) = 1;
    //    i.e., e and λ(n) are coprime.
    // e is predetermined
    e = DEFAULT_E_VALUE;

    // 1. Choose two distinct prime numbers p and q, p > q
    do {
      p = BigInt.probablePrime(BIT_LENGTH / 2, random);
    } while (!e.gcd(p.subtract(BigInt.ONE)).equals(BigInt.ONE));
    do {
      q = BigInt.probablePrime(BIT_LENGTH / 2, random);
    } while (!e.gcd(q.subtract(BigInt.ONE)).equals(BigInt.ONE) && p.equals(q));
    if (p.compareTo(q) == -1) {
      BigInt temp = p;
      p = q;
      q = temp;
    }

    // 2. Compute n = pq
    n = p.multiply(q);

    // 3. Compute λ(n) = lcm(λ(p), λ(q)) = lcm(p − 1, q − 1),
    //    where λ is Carmichael’s totient function. This value is kept private. */
    BigInt p2 = p.subtract(BigInt.ONE);
    BigInt q2 = q.subtract(BigInt.ONE);
    lambda = p2.divide(p2.gcd(q2)).multiply(q2);

    // 5. Determine d as d ≡ e^-1 (mod λ(n));
    //    i.e., d is the modular multiplicative inverse of e (modulo λ(n)).
    d = e.modInverse(lambda);

    // Generate CRT Keys
    generateCrtKey();
  }

  /**
   * Recovers P and Q, given N, E, D.
   * Uses algorithm from NIST 800-56B Appendix C.
   */
  private void getPQFromNED() {
    // 1: Let k = de – 1. If k is odd, then go to Step 4
    BigInt k = d.multiply(e).subtract(BigInt.ONE);

    if (!k.isOdd()) {
      // 2: Express k as (2^t)r, where r is the largest odd integer
      // dividing k and t >= 1
      BigInt r = k;
      long t = 0;
      do {
        r = r.shiftRight(1);
        t++;
      } while (!r.isOdd());

      // 3
      boolean success = false;
      BigInt y = null;

      step3: for (int i = 1; i <= 100; ++i) {
        // 3.1: Generate a random integer g in the range [0, n−1]
        BigInt g = BigInt.getRandomBigInt(n, random);

        // 3.2: Let y = g^r mod n
        y = g.modExp(r, n);

        // 3.3: If y = 1 or y = n – 1, then go to 3.1
        // (i.e. repeat this loop).
        if (y.equals(BigInt.ONE) || y.equals(n.subtract(BigInt.ONE)))
          continue step3;

        // 3.4
        for (long j = 1; j <= t - 1; ++j) {
          // 3.4.1: Let x = y^2 mod n
          BigInt x = y.modExp(BigInt.TWO, n);

          // 3.4.2: If x = 1, go to (outer) Step 5.
          if (x.equals(BigInt.ONE)) {
            success = true;
            break step3;
          }

          // 3.4.3: If x = n – 1, go to Step 3.1.
          if (x.equals(n.subtract(BigInt.ONE)))
            continue step3;

          // 3.4.4: Let y = x.
          y = x;
        }

        // 3.5: Let x = y^2 mod n
        BigInt x = y.modExp(BigInt.TWO, n);

        // 3.6: If x = 1, go to (outer) 5.
        if (x.equals(BigInt.ONE)) {
          success = true;
          break step3;
        }
      }

      // 5: Let p = GCD(y – 1, n) and let q = n/p
      // 6: Output (p, q) as the prime factors.
      if (success) {
        p = y.subtract(BigInt.ONE).gcd(n);
        q = n.divide(p);
        if (p.compareTo(q) == -1) {
          BigInt temp = p;
          p = q;
          q = temp;
        }
        return;
      }
    }

    // 4: Output “prime factors not found” and stop
    throw new RuntimeException("Prime factors not found");
  }

  /**
   * Generates N, E, D from P, Q
   * P and Q assumed prime
   */
  private void getNEDFromPQ() {
    n = p.multiply(q);

    BigInt p2 = p.subtract(BigInt.ONE);
    BigInt q2 = q.subtract(BigInt.ONE);
    lambda = p2.divide(p2.gcd(q2)).multiply(q2);

    e = DEFAULT_E_VALUE;
    while (!e.isCoprime(lambda) && (e.compareTo(lambda) == -1))
      e = e.add(BigInt.TWO);
    if (!e.isCoprime(lambda))
      throw new ArithmeticException("e not found");

    d = e.modInverse(lambda);
  }

  /**
   * Generates dP, dQ, and qInv from d, p, and q.
   * Used for decryption with Chinese Remainder Theorem
   */
  private void generateCrtKey() {
    dP = d.modInverse(p.subtract(BigInt.ONE));
    dQ = d.modInverse(q.subtract(BigInt.ONE));
    qInv = q.modInverse(p);
  }

  //------------------------------------------------------------------------------------------------
  //endregion

  //region To and From XML
  //------------------------------------------------------------------------------------------------

  public static PrivateKey fromXmlString(String xml) throws SAXException, IOException {
    BigInt[] values = XmlHelper.fromXmlString(xml, tagNames);
    if (values != null)
      return new PrivateKey(values[0], values[1], values[7], values[2],
          values[3], values[4], values[5], values[6]);
    return null;
  }

  public String toXmlString() {
    BigInt[] values = new BigInt[] { n, e, p, q, dP, dQ, qInv, d };
    return XmlHelper.toXmlString(tagNames, values);
  }

  //------------------------------------------------------------------------------------------------
  //endregion
}
