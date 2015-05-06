package com.maxxton.aam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigParser class Used by multiple classes to load configuration files. Contains mostly static classes as it is a utility class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class ConfigParser
{
  /**
   * Parses all supported configuration files.
   *
   * @param sFile
   *          path to the configuration file
   * @return the configuration parsed into properties object.
   */
  public static Properties parseConfig(String sFile)
  {
    InputStream stream = ConfigParser.class.getResourceAsStream(sFile);
    if (Validator.checkObject(stream, InputStream.class))
    {
      switch (ConfigParser.getExtension(sFile))
      {
        case "properties":
          return ConfigParser.parseProperties(stream);
        case "xml":
          return ConfigParser.parseXml(stream);
        default:
          // TODO : Notify through logger that the configuration file given is unsupported.
          break;
      }
    }
    else
    {
      // TODO : Notify through logger that the configuration file given is invalid.
    }
    return null;
  }

  /**
   * Gets the extension of the file.
   *
   * @param sFile
   *          path to the configuration file.
   * @return extension of the file as a string.
   */
  private static String getExtension(String sFile)
  {
    if (Validator.checkString(sFile))
    {
      int position = sFile.lastIndexOf('.');
      if (Validator.checkInteger(position, 1, Integer.MAX_VALUE))
      {
        return sFile.substring(position + 1).toLowerCase();
      }
    }
    return "";
  }

  /**
   * Parses a configuration file with the properties extension.
   *
   * @param objFile
   *          configuration file to be parsed.
   * @return
   */
  private static Properties parseProperties(InputStream objFile)
  {
    if (Validator.checkObject(objFile))
    {
      try
      {
        Properties properties = new Properties();
        properties.load(objFile);
        return properties;
      }
      catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Parses a configuration file with the xml extension.
   *
   * @param objFile
   *          configuration file to be parsed.
   * @return
   */
  private static Properties parseXml(InputStream objFile)
  {
    if (Validator.checkObject(objFile))
    {
      try
      {
        Properties properties = new Properties();
        properties.loadFromXML(objFile);
        return properties;
      }
      catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return null;
  }
}
