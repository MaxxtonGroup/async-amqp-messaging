package com.maxxton.test.resources;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.resources.Validator;

import static org.junit.Assert.*;

/**
 * Runs a list of tests against the Validator class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidatorTest
{

  @Before
  public void setup()
  {

  }

  /**
   * Tests the validator checks for strings.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testStringValidation() throws Exception
  {
    System.out.print("Validator : Testing the validator for string checks...");

    String strNull = null;
    String strEmpty = "";
    String strPattern = "hello world";

    boolean result;

    result = Validator.checkString(strNull);
    assertFalse("The result returned should be false", result);
    result = Validator.checkString(strEmpty);
    assertFalse("The result returned should be false", result);
    result = Validator.checkString(strPattern);
    assertTrue("The result returned should be false", result);

    result = Validator.checkString(strPattern, "^.*hello.*$");
    assertTrue("The result returned should be true", result);
    result = Validator.checkString(strPattern, "^.*world.*$");
    assertTrue("The result returned should be true", result);
    result = Validator.checkString(strPattern, "^.*bye.*$");
    assertFalse("The result returned should be false", result);

    result = Validator.checkString(strNull, true, false);
    assertTrue("The result returned should be true", result);
    result = Validator.checkString(strNull, false, true);
    assertFalse("The result returned should be false", result);

    result = Validator.checkString(strEmpty, false, true);
    assertTrue("The result returned should be true", result);
    result = Validator.checkString(strEmpty, true, false);
    assertFalse("The result returned should be false", result);

    System.out.println("done.");
  }

  /**
   * Tests the validator checks for integers.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testIntegerValidation() throws Exception
  {
    System.out.print("Validator : Testing the validator for integer checks...");

    Integer intNull = null;
    Integer intZero = 0;
    Integer intPos = 10;
    Integer intNeg = -10;

    boolean result;

    result = Validator.checkInteger(intNull);
    assertFalse("The result returned should be false", result);
    result = Validator.checkInteger(intZero);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intPos);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intNeg);
    assertTrue("The result returned should be true", result);

    result = Validator.checkInteger(intNull, true);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intZero, false);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intPos, false);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intNeg, false);
    assertTrue("The result returned should be true", result);

    result = Validator.checkInteger(intZero, 0, 0);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intPos, 0, Integer.MAX_VALUE);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intPos, Integer.MAX_VALUE, 0);
    assertFalse("The result returned should be false", result);
    result = Validator.checkInteger(intNeg, Integer.MIN_VALUE, 0);
    assertTrue("The result returned should be true", result);
    result = Validator.checkInteger(intNeg, 0, Integer.MIN_VALUE);
    assertFalse("The result returned should be false", result);

    System.out.println("done.");
  }

  /**
   * Tests the validator checks for objects.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testObjectValidation() throws Exception
  {
    System.out.print("Validator : Testing the validator for object checks...");

    Object objNull = null;
    String objString = "";

    boolean result;

    result = Validator.checkObject(objString);
    assertTrue("The result returned should be true", result);
    result = Validator.checkObject(objNull);
    assertFalse("The result returned should be false", result);

    result = Validator.checkObject(objString, String.class);
    assertTrue("The result returned should be true", result);
    result = Validator.checkObject(objString, Integer.class);
    assertFalse("The result returned should be false", result);

    result = Validator.checkObject(objNull, true);
    assertTrue("The result returned should be true", result);
    result = Validator.checkObject(objNull, false);
    assertFalse("The result returned should be false", result);

    result = Validator.checkObject(objString, false);
    assertTrue("The result returned should be true", result);
    result = Validator.checkObject(objString, true);
    assertFalse("The result returned should be false", result);

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {

  }

}
