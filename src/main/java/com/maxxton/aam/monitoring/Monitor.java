package com.maxxton.aam.monitoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
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
  private boolean blnEnabled;
  private MonitorLevel enmLevel;

  private CopyOnWriteArrayList<String> arlErrorLog;
  private CopyOnWriteArrayList<String> arlWarnLog;
  private CopyOnWriteArrayList<String> arlInfoLog;
  private CopyOnWriteArrayList<String> arlDebugLog;
  private CopyOnWriteArrayList<String> arlTraceLog;

  private AtomicInteger intSentMessages;
  private AtomicInteger intReceivedMessages;
  private AtomicInteger intDiscardedMessages;

  /**
   * Constructor of the Monitor class.
   * 
   * @param name
   *          the name of the messenger which uses the monitor class.
   * @param enabled
   *          the enabled state of this monitor.
   */
  public Monitor(String name, boolean enabled)
  {
    this.strName = name;
    this.blnEnabled = enabled;
    this.enmLevel = MonitorLevel.ALL;

    this.arlErrorLog = new CopyOnWriteArrayList<String>();
    this.arlWarnLog = new CopyOnWriteArrayList<String>();
    this.arlInfoLog = new CopyOnWriteArrayList<String>();
    this.arlDebugLog = new CopyOnWriteArrayList<String>();
    this.arlTraceLog = new CopyOnWriteArrayList<String>();

    this.intSentMessages = new AtomicInteger(0);
    this.intReceivedMessages = new AtomicInteger(0);
    this.intDiscardedMessages = new AtomicInteger(0);
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
   * Sets the enabled state of the Monitor.
   * 
   * @param enabled
   *          New enabled state of the Monitor.
   */
  public void setEnabled(boolean enabled)
  {
    this.blnEnabled = enabled;
  }

  /**
   * Gets the enabled state of the Monitor.
   * 
   * @return Current enabled state of the Monitor.
   */
  public boolean getEnabled()
  {
    return this.blnEnabled;
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
      logger.trace("[" + this.strName + "] " + trace);
      if (this.checkLevelPass(MonitorLevel.TRACE))
      {
        this.arlTraceLog.add("(" + objClass.getName() + ") " + trace);
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
      logger.trace("[" + this.strName + "] " + trace.toString());

      if (this.checkLevelPass(MonitorLevel.TRACE))
      {
        this.arlTraceLog.add("(" + objClass.getName() + ") " + trace.toString());
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
      logger.debug("[" + this.strName + "] " + strDebug);
      if (this.checkLevelPass(MonitorLevel.DEBUG))
      {
        this.arlDebugLog.add("(" + objClass.getName() + ") " + strDebug);
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
      logger.info("[" + this.strName + "] " + strInfo);
      if (this.checkLevelPass(MonitorLevel.INFO))
      {
        this.arlInfoLog.add("(" + objClass.getName() + ") " + strInfo);
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
      logger.warn("[" + this.strName + "] " + strWarn);
      if (this.checkLevelPass(MonitorLevel.WARN))
      {
        this.arlWarnLog.add("(" + objClass.getName() + ") " + strWarn);
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
      logger.error("[" + this.strName + "] " + strError);
      if (this.checkLevelPass(MonitorLevel.ERROR))
      {
        this.arlErrorLog.add("(" + objClass.getName() + ") " + strError);
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
    if (this.blnEnabled)
    {
      switch (type)
      {
        case MESSAGE_SENT:
          this.intSentMessages.addAndGet((Integer) data);
          break;
        case MESSAGE_RECEIVED:
          this.intReceivedMessages.addAndGet((Integer) data);
          break;
        case MESSAGE_DISCARDED:
          this.intDiscardedMessages.addAndGet((Integer) data);
          break;
        default:
          // Do Nothing
          break;
      }
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
    if (this.blnEnabled)
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
    else
    {
      return false;
    }
  }

  /**
   * Converts a given ArrayList of logs to string.
   * 
   * @param logs
   *          The ArrayList with logs.
   * @return The logs as an string.
   */
  private String logsToString(CopyOnWriteArrayList<String> logs)
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
        tmpData = intSentMessages.get();
        intSentMessages.set(0);
        return tmpData;
      case MESSAGE_RECEIVED:
        tmpData = intReceivedMessages.get();
        intReceivedMessages.set(0);
        return tmpData;
      case MESSAGE_DISCARDED:
        tmpData = intDiscardedMessages.get();
        intDiscardedMessages.set(0);
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
    if (this.blnEnabled)
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
}