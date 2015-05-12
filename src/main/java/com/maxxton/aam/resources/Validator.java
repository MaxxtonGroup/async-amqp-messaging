package com.maxxton.aam.resources;

/**
 * Validator class. Contains static methods which implement checks for multiple datatypes.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Validator
{

  /**
   * Checks if the string is not null and not empty.
   * 
   * @param strObject
   *          String object to check.
   * @return boolean to tell if the string passed the test.
   */
  public static boolean checkString(String strObject)
  {
    return Validator.checkString(strObject, "(.*)");
  }

  /**
   * Checks if the string is not null, not empty and matches the given regex.
   * 
   * @param strObject
   *          String object to check.
   * @param regex
   *          Regex to check String object against.
   * @return boolean to tell if the string passed the test.
   */
  public static boolean checkString(String strObject, String regex)
  {
    if (!Validator.checkString(strObject, false, false))
      return false;

    if (!strObject.matches(regex))
      return false;

    return true;
  }

  /**
   * Checks if the string is null or empty.
   * 
   * @param strObject
   *          String object to check.
   * @param isNull
   *          Should the string object be null.
   * @param isEmtpy
   *          Should the string object be empty.
   * @return boolean to tell if the string passed the test.
   */
  public static boolean checkString(String strObject, boolean isNull, boolean isEmtpy)
  {
    if ((strObject == null) != isNull)
      return false;

    if (isNull == false && strObject.isEmpty() != isEmtpy)
      return false;

    return true;
  }

  /**
   * Checks if the integer is not null.
   * 
   * @param intObject
   *          Integer object to check.
   * @return boolean to tell if the integer passed the test.
   */
  public static boolean checkInteger(Integer intObject)
  {
    return checkInteger(intObject, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Checks if the integer is not null and stays between the given minimum and maximum.
   * 
   * @param intObject
   *          Integer object to check.
   * @param minValue
   *          Minimum value the integer can have.
   * @param maxValue
   *          Maximum value the integer can have.
   * @return boolean to tell if the integer passed the test.
   */
  public static boolean checkInteger(Integer intObject, int minValue, int maxValue)
  {
    if (!Validator.checkInteger(intObject, false))
      return false;

    if (intObject < minValue)
      return false;

    if (intObject > maxValue)
      return false;

    return true;
  }

  /**
   * Check if the integer is null.
   * 
   * @param intObject
   *          Integer object to check.
   * @param isNull
   *          Should the integer object be null.
   * @return boolean to tell if the integer passed the test.
   */
  public static boolean checkInteger(Integer intObject, boolean isNull)
  {
    if ((intObject == null) != isNull)
      return false;

    return true;
  }

  /**
   * Check if the object is not null
   * 
   * @param object
   *          Object instance to check.
   * @return boolean to tell if the object passed the test.
   */
  public static boolean checkObject(Object object)
  {
    return Validator.checkObject(object, Object.class);
  }

  /**
   * Check if the object is not null and equals a certain class type.
   * 
   * @param object
   *          Object instance to check.
   * @param objectType
   *          Class type to check to object against
   * @return boolean to tell if the object passed the test.
   */
  public static boolean checkObject(Object object, Class<?> objectType)
  {
    if (!Validator.checkObject(object, false))
      return false;

    if (object == null)
      return false;

    if (!objectType.isInstance(object))
      return false;

    return true;
  }

  /**
   * Check if the object is not null.
   * 
   * @param object
   *          Object to check.
   * @param isNull
   *          Should the object be null.
   * @return boolean to tell if the object passed the test.
   */
  public static boolean checkObject(Object object, boolean isNull)
  {
    if ((object == null) != isNull)
      return false;

    return true;
  }
}
