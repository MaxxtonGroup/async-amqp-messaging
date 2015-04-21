package com.maxxton.aam.resources;

import java.io.File;

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
   */
  public static void parseConfig(String sFile)
  {
    File objFile = new File(sFile);
    if (objFile.exists() && !objFile.isDirectory())
    {
      switch (ConfigParser.getExtension(sFile))
      {
        case "properties":
          ConfigParser.parseProperties(objFile);
          break;
        case "xml":
          ConfigParser.parseXml(objFile);
          break;
        default:
          // TODO : Notify through logger that the configuration file given is unsupported.
          break;
      }
    }
    else
    {
      // TODO : Notify through logger that the configuration file given is unsupported.
    }
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
   */
  private static void parseProperties(File objFile)
  {

  }

  /**
   * Parses a configuration file with the xml extension.
   *
   * @param objFile
   *          configuration file to be parsed.
   */
  private static void parseXml(File objFile)
  {

  }
}
