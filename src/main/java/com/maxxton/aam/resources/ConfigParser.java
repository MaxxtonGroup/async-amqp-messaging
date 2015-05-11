package com.maxxton.aam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.maxxton.aam.monitoring.Monitor;
import com.maxxton.aam.monitoring.MonitorFactory;

/**
 * ConfigParser class Used by multiple classes to load configuration files. Contains mostly static classes as it is a utility class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class ConfigParser
{
  private final static Monitor objMonitor = MonitorFactory.getMonitor("global");

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
          objMonitor.warn(ConfigParser.class, "Failed to load configuration file '" + sFile + "'. Unsupported file type (Allowed: Properties, XML).");
          break;
      }
    }
    else
    {
      objMonitor.warn(ConfigParser.class, "Unable to find configuration file '" + sFile + "' in classpath. Make sure the file exists and is in the correct location.");
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
        return sFile.substring(position + 1).toLowerCase();
      else
        objMonitor.warn(ConfigParser.class, "Unable to find extension for file '" + sFile + "'. Make sure you are loading a file with a XML or Properties extension.");
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
    try
    {
      Properties properties = new Properties();
      properties.load(objFile);
      return properties;
    }
    catch (IOException e)
    {
      objMonitor.warn(ConfigParser.class, "Unable to load properties configuration file from inputstream.");
      objMonitor.trace(ConfigParser.class, e);
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
    try
    {
      Properties properties = new Properties();
      properties.loadFromXML(objFile);
      return properties;
    }
    catch (IOException e)
    {
      objMonitor.warn(ConfigParser.class, "Unable to load XML configuration file from inputstream.");
      objMonitor.trace(ConfigParser.class, e);
    }
    return null;
  }
}
