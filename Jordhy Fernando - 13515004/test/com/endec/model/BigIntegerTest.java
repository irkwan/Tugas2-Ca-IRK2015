package com.endec.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test class for BigInteger class
 *
 * @author Jordhy Fernando
 * @version 1.0
 */
public class BigIntegerTest {

  private static BigInteger zero;
  private static BigInteger pos1;
  private static BigInteger pos2;
  private static BigInteger neg1;
  private static BigInteger neg2;

  @BeforeClass
  public static void setUp() {
    zero = new BigInteger();
    pos1 = new BigInteger("87654321");
    pos2 = new BigInteger("12345678");
    neg1 = new BigInteger("-12345678");
    neg2 = new BigInteger("-87654321");
  }

  /**
   * Tests negate method.
   */
  @Test
  public void testNegate() {
    assertEquals("Test failed at negate zero", "0", zero.negate().toString());
    assertEquals("Test failed at negate positive value",
        "-87654321", pos1.negate().toString());
    assertEquals("Test failed at negate negative value",
        "12345678", neg1.negate().toString());
  }

  /**
   * Tests compareTo method.
   */
  @Test
  public void testCompareTo() {
    assertEquals("Test failed when comparing smaller positive value to bigger one",
        -1, pos2.compareTo(pos1));
    assertEquals("Test failed when comparing bigger positive value to smaller one",
        1, pos1.compareTo(pos2));
    assertEquals("Test failed when comparing positive value to negative value",
        1, pos2.compareTo(neg1));
    assertEquals("Test failed when comparing positive value to zero",
        1, pos2.compareTo(zero));

    assertEquals("Test failed when comparing smaller negative value to bigger one",
        -1, neg2.compareTo(neg1));
    assertEquals("Test failed when comparing bigger negative value to smaller one",
        1, neg1.compareTo(neg2));
    assertEquals("Test failed when comparing negative value to positive value",
        -1, neg1.compareTo(pos1));
    assertEquals("Test failed when comparing negative value to zero",
        -1, neg1.compareTo(zero));

    assertEquals("Test failed when comparing zero to positive value",
        -1, zero.compareTo(pos1));
    assertEquals("Test failed when comparing zero to negative value",
        1, zero.compareTo(neg2));

    assertEquals("Test failed when comparing zero to zero",
        0, zero.compareTo(zero));
    assertEquals("Test failed when comparing the same positive values",
        0, pos2.compareTo(pos2));
    assertEquals("Test failed when comparing the same negative values",
        0, neg1.compareTo(neg1));
  }

  /**
   * Tests isLessThan method.
   */
  @Test
  public void testIsLessThan() {
    assertTrue("Test failed when comparing smaller positive value to bigger one",
        pos2.isLessThan(pos1));
    assertFalse("Test failed when comparing positive value to negative value",
        pos2.isLessThan(neg1));
    assertFalse("Test failed when comparing positive value to zero",
        pos2.isLessThan(zero));

    assertTrue("Test failed when comparing smaller negative value to bigger one",
        neg2.isLessThan(neg1));
    assertTrue("Test failed when comparing negative value to zero",
        neg1.isLessThan(zero));

    assertFalse("Test failed when comparing zero to zero",
        zero.isLessThan(zero));
    assertFalse("Test failed when comparing the same positive values",
        pos2.isLessThan(pos2));
    assertFalse("Test failed when comparing the same negative values",
        neg1.isLessThan(neg1));
  }

  /**
   * Tests isGreaterThan method.
   */
  @Test
  public void isGreaterThan() {
    assertFalse("Test failed when comparing smaller positive value to bigger one",
        pos2.isGreaterThan(pos1));
    assertTrue("Test failed when comparing positive value to negative value",
        pos2.isGreaterThan(neg1));
    assertTrue("Test failed when comparing positive value to zero",
        pos2.isGreaterThan(zero));

    assertFalse("Test failed when comparing smaller negative value to bigger one",
        neg2.isGreaterThan(neg1));
    assertFalse("Test failed when comparing negative value to zero",
        neg1.isGreaterThan(zero));

    assertFalse("Test failed when comparing zero to zero",
        zero.isGreaterThan(zero));
    assertFalse("Test failed when comparing the same positive values",
        pos2.isGreaterThan(pos2));
    assertFalse("Test failed when comparing the same negative values",
        neg1.isGreaterThan(neg1));
  }

  /**
   * Tests equals method.
   */
  @Test
  public void equals() {
    assertFalse("Test failed when comparing smaller positive value to bigger one",
        pos2.equals(pos1));
    assertFalse("Test failed when comparing positive value to negative value",
        pos2.equals(neg1));
    assertFalse("Test failed when comparing positive value to zero",
        pos2.equals(zero));

    assertFalse("Test failed when comparing smaller negative value to bigger one",
        neg2.equals(neg1));
    assertFalse("Test failed when comparing negative value to zero",
        neg1.equals(zero));

    assertTrue("Test failed when comparing zero to zero",
        zero.equals(zero));
    assertTrue("Test failed when comparing the same positive values",
        pos2.equals(pos2));
    assertTrue("Test failed when comparing the same negative values",
        neg1.equals(neg1));
  }

  @Test
  public void isLessThanOrEquals() {
    assertTrue("Test failed when comparing smaller positive value to bigger one",
        pos2.isLessThanOrEquals(pos1));
    assertFalse("Test failed when comparing positive value to negative value",
        pos2.isLessThanOrEquals(neg1));
    assertFalse("Test failed when comparing positive value to zero",
        pos2.isLessThanOrEquals(zero));

    assertTrue("Test failed when comparing smaller negative value to bigger one",
        neg2.isLessThanOrEquals(neg1));
    assertTrue("Test failed when comparing negative value to zero",
        neg1.isLessThanOrEquals(zero));

    assertTrue("Test failed when comparing zero to zero",
        zero.isLessThanOrEquals(zero));
    assertTrue("Test failed when comparing the same positive values",
        pos2.isLessThanOrEquals(pos2));
    assertTrue("Test failed when comparing the same negative values",
        neg1.isLessThanOrEquals(neg1));
  }

  @Test
  public void isGreaterThanOrEquals() {
    assertFalse("Test failed when comparing smaller positive value to bigger one",
        pos2.isGreaterThanOrEquals(pos1));
    assertTrue("Test failed when comparing positive value to negative value",
        pos2.isGreaterThanOrEquals(neg1));
    assertTrue("Test failed when comparing positive value to zero",
        pos2.isGreaterThanOrEquals(zero));

    assertFalse("Test failed when comparing smaller negative value to bigger one",
        neg2.isGreaterThanOrEquals(neg1));
    assertFalse("Test failed when comparing negative value to zero",
        neg1.isGreaterThanOrEquals(zero));

    assertTrue("Test failed when comparing zero to zero",
        zero.isGreaterThanOrEquals(zero));
    assertTrue("Test failed when comparing the same positive values",
        pos2.isGreaterThanOrEquals(pos2));
    assertTrue("Test failed when comparing the same negative values",
        neg1.isGreaterThanOrEquals(neg1));
  }

  /**
   * Tests add method.
   */
  @Test
  public void testAdd() {
    assertEquals("Test failed at addition of zero with zero",
        "0", zero.add(zero).toString());
    assertEquals("Test failed at addition of zero with positive value",
        "87654321", zero.add(pos1).toString());
    assertEquals("Test failed at addition of zero with negative value",
        "-12345678", zero.add(neg1).toString());
    assertEquals("Test failed at addition of positive value with zero",
        "87654321", pos1.add(zero).toString());
    assertEquals("Test failed at addition of negative value with zero",
        "-12345678", neg1.add(zero).toString());
    assertEquals("Test failed at addition of 2 positive values (1)",
        "99999999", pos1.add(pos2).toString());
    assertEquals("Test failed at addition of 2 positive values (2)",
        "175308642", pos1.add(pos1).toString());
    assertEquals("Test failed at addition of 2 negative values",
        "-99999999", neg1.add(neg2).toString());
    assertEquals("Test failed at addition of positive value with negative value",
        "0", pos1.add(neg2).toString());
    assertEquals("Test failed at addition of negative value with positive value",
        "0", neg2.add(pos1).toString());
  }

  /**
   * Tests subtract method.
   */
  @Test
  public void testSubtract() {
    assertEquals("Test failed at subtraction of zero with zero",
        "0", zero.subtract(zero).toString());
    assertEquals("Test failed at subtraction of zero with positive value",
        "-87654321", zero.subtract(pos1).toString());
    assertEquals("Test failed at subtraction of zero with negative value",
        "12345678", zero.subtract(neg1).toString());
    assertEquals("Test failed at subtraction of positive value with zero",
        "87654321", pos1.subtract(zero).toString());
    assertEquals("Test failed at subtraction of negative value with zero",
        "-12345678", neg1.subtract(zero).toString());
    assertEquals("Test failed at subtraction of 2 positive values (positive result)",
        "75308643", pos1.subtract(pos2).toString());
    assertEquals("Test failed at subtraction of 2 positive values (negative result)",
        "-75308643", pos2.subtract(pos1).toString());
    assertEquals("Test failed at subtraction of 2 negative values (positive result)",
        "75308643", neg1.subtract(neg2).toString());
    assertEquals("Test failed at subtraction of 2 negative values (negative result)",
        "-75308643", neg2.subtract(neg1).toString());
    assertEquals("Test failed at subtraction of 2 same values",
        "0", neg2.subtract(neg2).toString());
    assertEquals("Test failed at subtraction of positive value with negative value",
        "99999999", pos1.subtract(neg1).toString());
    assertEquals("Test failed at subtraction of negative value with positive value",
        "-99999999", neg2.subtract(pos2).toString());
  }

  /**
   * Tests multiply method.
   */
  @Test
  public void testMultiply() {
    assertEquals("Test failed at multiplication of zero with zero",
        "0", zero.multiply(zero).toString());
    assertEquals("Test failed at multiplication of zero with positive value",
        "0", zero.multiply(pos1).toString());
    assertEquals("Test failed at multiplication of zero with negative value",
        "0", zero.multiply(neg1).toString());
    assertEquals("Test failed at multiplication of positive value with zero",
        "0", pos1.multiply(zero).toString());
    assertEquals("Test failed at multiplication of negative value with zero",
        "0", neg1.multiply(zero).toString());
    assertEquals("Test failed at multiplication of 2 positive values",
        "1082152022374638", pos1.multiply(pos2).toString());
    assertEquals("Test failed at multiplication of 2 negative values",
        "1082152022374638", neg1.multiply(neg2).toString());
    assertEquals("Test failed at multiplication of positive value with negative value",
        "-1082152022374638", pos1.multiply(neg1).toString());
    assertEquals("Test failed at multiplication of negative value with positive value",
        "-1082152022374638", neg2.multiply(pos2).toString());
  }

}