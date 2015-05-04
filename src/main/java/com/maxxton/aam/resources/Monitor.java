package com.maxxton.aam.resources;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitor class Supports monitoring and logging of information within the library.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Monitor
{

  public enum MonitorLevel
  {
    ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF
  };

  private Logger objLogger;

  private MonitorLevel monitorLvl;

  /**
   * Constructor for the monitor class.
   */
  public Monitor()
  {
    this.objLogger = LoggerFactory.getLogger(Monitor.class);

    this.loadConfiguration("/default.properties");
  }

  public void loadConfiguration(String configFile)
  {
    Properties properties = ConfigParser.parseConfig(configFile);

    if (properties != null)
    {
      String strMonitorLvl = properties.getProperty("monitor.level", this.getMonitorLevel() == null ? "" : this.getMonitorLevel().toString());
      this.setMonitorLevel(this.determineLevel(strMonitorLvl));
    }
  }

  private MonitorLevel determineLevel(String lvl)
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
  public void trace(String strTrace)
  {
    this.objLogger.trace(strTrace);
  }

  /**
   * Log and/or monitor a DEBUG.
   * 
   * @param strDebug
   *          description given for the DEBUG
   */
  public void debug(String strDebug)
  {
    this.objLogger.debug(strDebug);
  }

  /**
   * Log and/or monitor a INFO.
   * 
   * @param strInfo
   *          description given for the INFO
   */
  public void info(String strInfo)
  {
    this.objLogger.info(strInfo);
  }

  /**
   * Log and/or monitor a WARN.
   * 
   * @param strWarn
   *          description given for the WARN
   */
  public void warn(String strWarn)
  {
    this.objLogger.warn(strWarn);
  }

  /**
   * Log and/or monitor a ERROR.
   * 
   * @param strError
   *          description given for the ERROR
   */
  public void error(String strError)
  {
    this.objLogger.error(strError);
  }

  /** Getters and Setters **/

  /**
   * Sets the level for the monitor to activate.
   * 
   * @param monitorLvl
   *          Level on which the monitor triggers
   */
  public void setMonitorLevel(MonitorLevel monitorLvl)
  {
    this.monitorLvl = monitorLvl;
  }

  /**
   * Gets the level for the monitor to activate.
   * 
   * @return Level on which the monitor triggers.
   */
  public MonitorLevel getMonitorLevel()
  {
    return this.monitorLvl;
  }
}
