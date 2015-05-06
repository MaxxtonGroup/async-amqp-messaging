package com.maxxton.aam.resources;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitor class. Supports monitoring and logging of information within the library.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Monitor
{

  /**
   * MonitorLevel class. Inner enumeration to determine level of monitoring.
   * 
   * @author Robin Hermans
   * @copyright Maxxton 2015
   */
  public static enum MonitorLevel
  {
    ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF
  };

  private static Logger objLogger;
  private static MonitorLevel monitorLvl;

  /**
   * Constructor for the monitor class.
   */
  public Monitor()
  {
    Monitor.objLogger = LoggerFactory.getLogger(Monitor.class);

    Monitor.loadConfiguration("/default.properties");
  }

  /**
   * Loads the properties from a given configuration file if they exist.
   * 
   * @param configFile
   *          properties or XML configuration file.
   */
  public static void loadConfiguration(String configFile)
  {
    Properties properties = ConfigParser.parseConfig(configFile);

    if (Validator.checkObject(properties))
    {
      String strMonitorLvl = properties.getProperty("monitor.level", Monitor.getMonitorLevel() == null ? "" : Monitor.getMonitorLevel().toString());
      Monitor.setMonitorLevel(Monitor.determineLevel(strMonitorLvl));
    }
  }

  /**
   * Determines the MonitorLevel enum for a given string.
   * 
   * @param lvl
   *          the monitorlevel given as string.
   * @return a MonitorLevel enumeration.
   */
  private static MonitorLevel determineLevel(String lvl)
  {
    switch (lvl.toUpperCase())
    {
      case "TRACE":
        return MonitorLevel.TRACE;
      case "DEBUG":
        return MonitorLevel.DEBUG;
      case "INFO":
        return MonitorLevel.INFO;
      case "WARN":
        return MonitorLevel.WARN;
      case "ERROR":
        return MonitorLevel.ERROR;
      case "OFF":
        return MonitorLevel.OFF;
      default:
        return MonitorLevel.ALL;
    }
  }

  /** Different Log Levels **/

  /**
   * Log and/or monitor a TRACE.
   * 
   * @param strTrace
   *          description given for the trace.
   */
  public static void trace(String strTrace)
  {
    if (Validator.checkString(strTrace))
      Monitor.objLogger.trace(strTrace);
  }

  /**
   * Log and/or monitor a TRACE using an Exception object.
   * 
   * @param e
   *          Exception object to get trace from.
   */
  public static void trace(Exception e)
  {
    if (Validator.checkObject(e, Exception.class))
    {
      StringWriter trace = new StringWriter();
      e.printStackTrace(new PrintWriter(trace));

      Monitor.objLogger.trace(trace.toString());
    }
  }

  /**
   * Log and/or monitor a DEBUG.
   * 
   * @param strDebug
   *          description given for the DEBUG
   */
  public static void debug(String strDebug)
  {
    if (Validator.checkString(strDebug))
      Monitor.objLogger.debug(strDebug);
  }

  /**
   * Log and/or monitor a INFO.
   * 
   * @param strInfo
   *          description given for the INFO
   */
  public static void info(String strInfo)
  {
    if (Validator.checkString(strInfo))
      Monitor.objLogger.info(strInfo);
  }

  /**
   * Log and/or monitor a WARN.
   * 
   * @param strWarn
   *          description given for the WARN
   */
  public static void warn(String strWarn)
  {
    if (Validator.checkString(strWarn))
      Monitor.objLogger.warn(strWarn);
  }

  /**
   * Log and/or monitor a ERROR.
   * 
   * @param strError
   *          description given for the ERROR
   */
  public static void error(String strError)
  {
    if (Validator.checkString(strError))
      Monitor.objLogger.error(strError);
  }

  /** Getters and Setters **/

  /**
   * Sets the level for the monitor to activate.
   * 
   * @param monitorLvl
   *          Level on which the monitor triggers
   */
  public static void setMonitorLevel(MonitorLevel monitorLvl)
  {
    if (Validator.checkObject(monitorLvl, MonitorLevel.class))
      Monitor.monitorLvl = monitorLvl;
  }

  /**
   * Gets the level for the monitor to activate.
   * 
   * @return Level on which the monitor triggers.
   */
  public static MonitorLevel getMonitorLevel()
  {
    return Monitor.monitorLvl;
  }
}
