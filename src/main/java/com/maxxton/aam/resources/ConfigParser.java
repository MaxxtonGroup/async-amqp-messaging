package com.maxxton.aam.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    File objFile = new File(sFile);
    if (!objFile.isDirectory() && objFile.exists())
    {
      switch (ConfigParser.getExtension(sFile))
      {
        case "properties":
          return ConfigParser.parseProperties(objFile);
        case "xml":
          return ConfigParser.parseXml(objFile);
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
    int position = sFile.lastIndexOf('.');
    if (position > 0)
    {
      return sFile.substring(position + 1).toLowerCase();
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
  private static Properties parseProperties(File objFile)
  {
    try
    {
      Properties properties = new Properties();
      properties.load(new FileInputStream(objFile));
      return properties;
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
  private static Properties parseXml(File objFile)
  {
    try
    {
      Properties properties = new Properties();
      properties.loadFromXML(new FileInputStream(objFile));
      return properties;
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
