package com.maxxton.aam.resources;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitor class. Supports monitoring and logging of information within the library.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Monitor extends AppenderSkeleton
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
  private static Zabbix objZabbix;
  private static MonitorLevel monitorLvl;
  private static boolean bIsStarted;

  /**
   * Constructor for the monitor class.
   */
  public Monitor()
  {
    Monitor.objLogger = LoggerFactory.getLogger(Monitor.class);
    Monitor.objZabbix = new Zabbix();
    Monitor.monitorLvl = MonitorLevel.ALL;
    Monitor.bIsStarted = false;

    Monitor.loadConfiguration("/default.properties");
  }

  /**
   * Start method. Called after the correct configuration has been loaded.
   */
  public static void start()
  {
    if (!bIsStarted)
    {
      Monitor.objZabbix.start();
      Monitor.bIsStarted = true;
    }
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
      String strMonitorLvl = properties.getProperty("monitor.level", Monitor.getMonitorLevel().toString());
      Monitor.setMonitorLevel(Monitor.determineLevel(strMonitorLvl));

      String strHostName = properties.getProperty("monitor.hostname", Monitor.objZabbix.getHostname());
      String strServerAddress = properties.getProperty("monitor.server.address", Monitor.objZabbix.getServerAddress());
      int intServerPort = properties.getProperty("monitor.server.port") == null ? Monitor.objZabbix.getServerPort() : Integer.parseInt(properties.getProperty("monitor.server.port"));

      Monitor.objZabbix.setupAgent(strHostName, strServerAddress, intServerPort);
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

  /**
   * Checks if a certain level passes the MonitorLevel setting.
   * 
   * @param level
   *          Level to be checked against the MonitorLevel.
   * @return True if it passed, False if it didn't.
   */
  private static boolean checkLevelPass(MonitorLevel level)
  {
    switch (Monitor.monitorLvl)
    {
      case ALL:
        return true;
      case TRACE:
        return level == MonitorLevel.TRACE || level == MonitorLevel.DEBUG || level == MonitorLevel.INFO || level == MonitorLevel.WARN || level == MonitorLevel.ERROR ? true : false;
      case DEBUG:
        return level == MonitorLevel.DEBUG || level == MonitorLevel.INFO || level == MonitorLevel.WARN || level == MonitorLevel.ERROR ? true : false;
      case INFO:
        return level == MonitorLevel.INFO || level == MonitorLevel.WARN || level == MonitorLevel.ERROR ? true : false;
      case WARN:
        return level == MonitorLevel.WARN || level == MonitorLevel.ERROR ? true : false;
      case ERROR:
        return level == MonitorLevel.ERROR ? true : false;
      case OFF:
        return false;
      default:
        return true;
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
    {
      Monitor.objLogger.trace(strTrace);
      if (Monitor.checkLevelPass(MonitorLevel.TRACE))
      {
        Monitor.objZabbix.addLog(MonitorLevel.TRACE, strTrace);
      }
    }
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
      if (Monitor.checkLevelPass(MonitorLevel.TRACE))
      {
        Monitor.objZabbix.addLog(MonitorLevel.TRACE, trace.toString());
      }
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
    {
      Monitor.objLogger.debug(strDebug);
      if (Monitor.checkLevelPass(MonitorLevel.DEBUG))
      {
        Monitor.objZabbix.addLog(MonitorLevel.DEBUG, strDebug);
      }
    }
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
    {
      Monitor.objLogger.info(strInfo);
      if (Monitor.checkLevelPass(MonitorLevel.INFO))
      {
        Monitor.objZabbix.addLog(MonitorLevel.INFO, strInfo);
      }
    }
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
    {
      Monitor.objLogger.warn(strWarn);
      if (Monitor.checkLevelPass(MonitorLevel.WARN))
      {
        Monitor.objZabbix.addLog(MonitorLevel.WARN, strWarn);
      }
    }
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
    {
      Monitor.objLogger.error(strError);
      if (Monitor.checkLevelPass(MonitorLevel.ERROR))
      {
        Monitor.objZabbix.addLog(MonitorLevel.ERROR, strError);
      }
    }
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

  @Override
  public void close()
  {

  }

  @Override
  public boolean requiresLayout()
  {
    return false;
  }

  @Override
  protected void append(LoggingEvent event)
  {
    MonitorLevel level = Monitor.determineLevel(event.getLevel().toString());
    if (Monitor.checkLevelPass(level))
    {
      Monitor.objZabbix.addLog(level, event.getMessage().toString());
    }
  }
}
