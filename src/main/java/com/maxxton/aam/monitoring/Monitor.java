package com.maxxton.aam.monitoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxxton.aam.resources.Validator;

/**
 * Monitor class. Supports both logging and monitoring of logs and data.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Monitor
{
  /**
   * Static enumeration to tell the monitor level.
   * 
   * @author Robin Hermans
   * @copyright Maxxton 2015
   */
  public static enum MonitorLevel
  {
    ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF
  };

  /**
   * Static enumeration to tell the type of data
   * 
   * @author Robin Hermans
   * @copyright Maxxton 2015
   */
  public static enum DataType
  {
    MESSAGE_SENT, MESSAGE_RECEIVED, MESSAGE_DISCARDED
  };

  private String strName;
  private MonitorLevel enmLevel;

  private ArrayList<String> arlErrorLog;
  private ArrayList<String> arlWarnLog;
  private ArrayList<String> arlInfoLog;
  private ArrayList<String> arlDebugLog;
  private ArrayList<String> arlTraceLog;

  private int intSentMessages;
  private int intReceivedMessages;
  private int intDiscardedMessages;

  /**
   * Constructor of the Monitor class.
   * 
   * @param name
   *          the name of the messenger which uses the monitor class.
   */
  public Monitor(String name)
  {
    this.strName = name;
    this.enmLevel = MonitorLevel.ALL;

    this.arlErrorLog = new ArrayList<String>();
    this.arlWarnLog = new ArrayList<String>();
    this.arlInfoLog = new ArrayList<String>();
    this.arlDebugLog = new ArrayList<String>();
    this.arlTraceLog = new ArrayList<String>();

    this.intSentMessages = 0;
    this.intReceivedMessages = 0;
    this.intDiscardedMessages = 0;
  }

  /**
   * Sets the level of monitoring.
   * 
   * @param level
   *          The level on which the zabbixAgent starts monitoring.
   */
  public void setMonitorLevel(MonitorLevel level)
  {
    this.enmLevel = level;
  }

  /**
   * Gets the level of monitoring.
   * 
   * @return The level on which the zabbixAgent starts monitoring.
   */
  public MonitorLevel getMonitorLevel()
  {
    return this.enmLevel;
  }

  /**
   * Log a trace using a certain class.
   * 
   * @param objClass
   *          The class from which the trace is given.
   * @param trace
   *          The trace message as string.
   */
  public void trace(Class<?> objClass, String trace)
  {
    if (Validator.checkString(trace, false, false))
    {
      Logger logger = LoggerFactory.getLogger(objClass);
      logger.trace(trace);
      if (this.checkLevelPass(MonitorLevel.TRACE))
      {
        this.arlTraceLog.add("[" + this.strName + "]" + trace);
      }
    }
  }

  /**
   * Log a trace using a certain class.
   * 
   * @param objClass
   *          The class from which the trace is given.
   * @param e
   *          The trace exception class.
   */
  public void trace(Class<?> objClass, Exception e)
  {
    if (Validator.checkObject(e, Exception.class))
    {
      StringWriter trace = new StringWriter();
      e.printStackTrace(new PrintWriter(trace));

      Logger logger = LoggerFactory.getLogger(objClass);
      logger.trace(trace.toString());

      if (this.checkLevelPass(MonitorLevel.TRACE))
      {
        this.arlTraceLog.add("[" + this.strName + "]" + trace.toString());
      }
    }
  }

  /**
   * Log a debug using a certain class.
   * 
   * @param objClass
   *          The class from which the debug is given.
   * @param strDebug
   *          The debug message as string.
   */
  public void debug(Class<?> objClass, String strDebug)
  {
    if (Validator.checkString(strDebug, false, false))
    {
      Logger logger = LoggerFactory.getLogger(objClass);
      logger.debug(strDebug);
      if (this.checkLevelPass(MonitorLevel.DEBUG))
      {
        this.arlDebugLog.add("[" + this.strName + "]" + strDebug);
      }
    }
  }

  /**
   * Log an info using a certain class.
   * 
   * @param objClass
   *          The class from which the info is given.
   * @param strInfo
   *          The info message as string.
   */
  public void info(Class<?> objClass, String strInfo)
  {
    if (Validator.checkString(strInfo, false, false))
    {
      Logger logger = LoggerFactory.getLogger(objClass);
      logger.info(strInfo);
      if (this.checkLevelPass(MonitorLevel.INFO))
      {
        this.arlInfoLog.add("[" + this.strName + "]" + strInfo);
      }
    }
  }

  /**
   * Log a warn using a certain class.
   * 
   * @param objClass
   *          The class from which the warn is given.
   * @param strWarn
   *          The debug message as string.
   */
  public void warn(Class<?> objClass, String strWarn)
  {
    if (Validator.checkString(strWarn, false, false))
    {
      Logger logger = LoggerFactory.getLogger(objClass);
      logger.warn(strWarn);
      if (this.checkLevelPass(MonitorLevel.WARN))
      {
        this.arlWarnLog.add("[" + this.strName + "]" + strWarn);
      }
    }
  }

  /**
   * Log an error using a certain class.
   * 
   * @param objClass
   *          The class from which the error is given.
   * @param strError
   *          The error message as string.
   */
  public void error(Class<?> objClass, String strError)
  {
    if (Validator.checkString(strError, false, false))
    {
      Logger logger = LoggerFactory.getLogger(objClass);
      logger.error(strError);
      if (this.checkLevelPass(MonitorLevel.ERROR))
      {
        this.arlErrorLog.add("[" + this.strName + "]" + strError);
      }
    }
  }

  /**
   * Adds a new data entry to a certain type.
   * 
   * @param type
   *          The type to assign the data too.
   * @param data
   *          The data to be assigned.
   */
  public void data(DataType type, Object data)
  {
    switch (type)
    {
      case MESSAGE_SENT:
        this.intSentMessages += (Integer) data;
        break;
      case MESSAGE_RECEIVED:
        this.intReceivedMessages += (Integer) data;
        break;
      case MESSAGE_DISCARDED:
        this.intDiscardedMessages += (Integer) data;
        break;
      default:
        // Do Nothing
        break;
    }
  }

  /**
   * Checks if a certain level passes the MonitorLevel setting.
   * 
   * @param level
   *          Level to be checked against the MonitorLevel.
   * @return True if it passed, False if it didn't.
   */
  private boolean checkLevelPass(MonitorLevel level)
  {
    switch (this.enmLevel)
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

  /**
   * Converts a given ArrayList of logs to string.
   * 
   * @param logs
   *          The ArrayList with logs.
   * @return The logs as an string.
   */
  private String logsToString(ArrayList<String> logs)
  {
    String strLog = "";

    for (String log : logs)
    {
      strLog += log + "\n";
    }
    logs.clear();

    return strLog;
  }

  /**
   * Drains the logs into a string.
   * 
   * @param level
   *          The level of the requested logs.
   * @return a string of the logs.
   */
  public String drainLogs(MonitorLevel level)
  {
    switch (level)
    {
      case ERROR:
        return this.logsToString(this.arlErrorLog);
      case WARN:
        return this.logsToString(this.arlWarnLog);
      case INFO:
        return this.logsToString(this.arlInfoLog);
      case DEBUG:
        return this.logsToString(this.arlDebugLog);
      case TRACE:
        return this.logsToString(this.arlTraceLog);
      default:
        return "";
    }
  }

  /**
   * Drains the data into an object.
   * 
   * @param data
   *          The data type to be drained.
   * @return a object of the requested data type.
   */
  public Object drainData(DataType data)
  {
    Object tmpData = 0;
    switch (data)
    {
      case MESSAGE_SENT:
        tmpData = intSentMessages;
        intSentMessages = 0;
        return tmpData;
      case MESSAGE_RECEIVED:
        tmpData = intReceivedMessages;
        intReceivedMessages = 0;
        return tmpData;
      case MESSAGE_DISCARDED:
        tmpData = intDiscardedMessages;
        intDiscardedMessages = 0;
        return tmpData;
      default:
        return tmpData;
    }
  }

  /**
   * Adds a new log line to a given monitor level.
   * 
   * @param level
   *          The level of the given log.
   * @param log
   *          The log message as string.
   */
  public void addLog(MonitorLevel level, String log)
  {
    switch (level)
    {
      case ERROR:
        this.arlErrorLog.add(log);
        break;
      case WARN:
        this.arlWarnLog.add(log);
        break;
      case INFO:
        this.arlInfoLog.add(log);
        break;
      case DEBUG:
        this.arlDebugLog.add(log);
        break;
      case TRACE:
        this.arlTraceLog.add(log);
        break;
      default:
        // Do Nothing...
        break;
    }
  }
}