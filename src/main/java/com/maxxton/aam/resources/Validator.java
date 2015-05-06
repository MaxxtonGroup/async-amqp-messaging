package com.maxxton.aam.resources;

public class Validator
{

  /** Checks for String objects **/

  public static boolean checkString(String strObject)
  {
    return Validator.checkString(strObject, "(.*)");
  }

  public static boolean checkString(String strObject, String regex)
  {
    if (!Validator.checkString(strObject, false, false))
      return false;

    if (!strObject.matches(regex))
      return false;

    return true;
  }

  public static boolean checkString(String strObject, boolean allowNull, boolean allowEmtpy)
  {
    if ((strObject == null) != allowNull)
      return false;

    if (strObject.isEmpty() != allowEmtpy)
      return false;

    return true;
  }

  /** Checks for Integer objects **/

  public static boolean checkInteger(Integer intObject)
  {
    return checkInteger(intObject, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

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

  public static boolean checkInteger(Integer intObject, boolean isNull)
  {
    if ((intObject == null) != isNull)
      return false;

    return true;
  }

  /** Checks for Object objects **/

  public static boolean checkObject(Object object)
  {
    return Validator.checkObject(object, Object.class);
  }

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

  public static boolean checkObject(Object object, boolean isNull)
  {
    if ((object == null) != isNull)
      return false;

    return true;
  }
}
