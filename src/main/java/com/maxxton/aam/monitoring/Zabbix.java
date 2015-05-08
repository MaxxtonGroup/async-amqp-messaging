package com.maxxton.aam.monitoring;

import java.net.InetAddress;
import java.util.ArrayList;

import com.quigley.zabbixj.agent.ZabbixAgent;
import com.quigley.zabbixj.providers.JVMMetricsProvider;

/**
 * Zabbix class Support monitoring of the client through the Zabbix agent.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
// TODO : Make this monitoring class thread safe.
public class Zabbix
{
  private ZabbixAgent objZabbixAgent;
  private boolean blnIsStarted;

  private String strHostname;
  private String strServerAddress;
  private int intServerPort;

  private ArrayList<String> arlErrorLog;
  private ArrayList<String> arlWarnLog;
  private ArrayList<String> arlInfoLog;
  private ArrayList<String> arlDebugLog;
  private ArrayList<String> arlTraceLog;

  private int intSentMessage;
  private int intReceivedMessage;
  private int intDiscardedMessage;

  /**
   * DataType enumeration. Used to determine the type of data being passed to the monitor.
   * 
   * @author Robin Hermans
   * @copyright Maxxton 2015
   */
  public enum DataType
  {
    MESSAGE_SENT, MESSAGE_RECEIVED, MESSAGE_DISCARDED
  }

  /**
   * Zabbix class constructor.
   */
  public Zabbix()
  {
    this.arlErrorLog = new ArrayList<String>();
    this.arlWarnLog = new ArrayList<String>();
    this.arlInfoLog = new ArrayList<String>();
    this.arlDebugLog = new ArrayList<String>();
    this.arlTraceLog = new ArrayList<String>();

    this.intSentMessage = 0;
    this.intReceivedMessage = 0;
    this.intDiscardedMessage = 0;

    this.blnIsStarted = false;
  }

  /**
   * Sets up the agent's settings. You still need to call start to active the Agent.
   * 
   * @param hostname
   *          The name of the host in the Zabbix configuration
   * @param serverAddress
   *          The name or IP of the Zabbix server
   * @param serverPort
   *          The port used to connect to the Zabbix server (default: 10050, active-checks: 10051)
   */
  public void setupAgent(String hostname, String serverAddress, int serverPort)
  {
    try
    {
      this.objZabbixAgent = new ZabbixAgent();
      this.objZabbixAgent.setEnableActive(true);
      this.objZabbixAgent.setEnablePassive(false);
      this.objZabbixAgent.setHostName(hostname);
      this.objZabbixAgent.setServerAddress(InetAddress.getByName(serverAddress));
      this.objZabbixAgent.setServerPort(serverPort);

      this.objZabbixAgent.addProvider("java", new JVMMetricsProvider());
      this.objZabbixAgent.addProvider("heartbeat", new HeartBeatProvider());
      this.objZabbixAgent.addProvider("logs", new LogProvider(this));
      this.objZabbixAgent.addProvider("messages", new MessageProvider(this));

      this.strHostname = hostname;
      this.strServerAddress = serverAddress;
      this.intServerPort = serverPort;
    }
    catch (Exception e)
    {
      Monitor.error("Failed to initiate the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
    }
  }

  /**
   * Start method. Called after configuring the agent to start sending metrics to Zabbix.
   */
  public void start()
  {
    if (!this.blnIsStarted)
    {
      try
      {
        this.objZabbixAgent.start();
      }
      catch (Exception e)
      {
        Monitor.error("Failed to start the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
      }
      this.blnIsStarted = true;
    }
  }

  /**
   * Gets the Hostname defined in the Zabbix configuration.
   * 
   * @return the hostname as string.
   */
  public String getHostname()
  {
    return this.strHostname;
  }

  /**
   * Gets the server address.
   * 
   * @return the address of the Zabbix server
   */
  public String getServerAddress()
  {
    return this.strServerAddress;
  }

  /**
   * Gets the server port.
   * 
   * @return the port of the Zabbix server.
   */
  public int getServerPort()
  {
    return this.intServerPort;
  }

  /**
   * Drains a given ArrayList into a String using a given prefix.
   * 
   * @param prefix
   *          The prefix to put before each log.
   * @param logs
   *          An ArrayList of a certain log level.
   * @return An ArrayList converted to String.
   */
  private String drainArrayList(String prefix, ArrayList<String> logs)
  {
    String strLogs = "";
    for (String log : logs)
    {
      strLogs += (prefix + log + "\n");
    }
    logs.clear();
    return strLogs;
  }

  /**
   * Gets the logs as String by a given log level.
   * 
   * @param level
   *          The level of which the logs should be returned.
   * @return All logs of a logLevel converted to String.
   */
  public String getLogsByLevel(Monitor.MonitorLevel level)
  {
    switch (level)
    {
      case ERROR:
        return this.drainArrayList("ERROR: ", this.arlErrorLog);
      case WARN:
        return this.drainArrayList("WARN: ", this.arlWarnLog);
      case INFO:
        return this.drainArrayList("INFO: ", this.arlInfoLog);
      case DEBUG:
        return this.drainArrayList("DEBUG: ", this.arlDebugLog);
      case TRACE:
        return this.drainArrayList("TRACE: ", this.arlTraceLog);
      default:
        return "";
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
  public void addLog(Monitor.MonitorLevel level, String log)
  {
    if (blnIsStarted)
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

  /**
   * Gets all data by a given DataType.
   * 
   * @return The object of the data.
   */
  public Object getDataByType(DataType type)
  {
    int intTemp = 0;
    if (blnIsStarted)
    {
      switch (type)
      {
        case MESSAGE_SENT:
          intTemp = intSentMessage;
          intSentMessage = 0;
          break;
        case MESSAGE_RECEIVED:
          intTemp = intReceivedMessage;
          intReceivedMessage = 0;
          break;
        case MESSAGE_DISCARDED:
          intTemp = intDiscardedMessage;
          intDiscardedMessage = 0;
          break;
        default:
          break;
      }
    }
    return intTemp;
  }

  /**
   * Adds a new data entry to a certain type.
   * 
   * @param type
   *          The type to assign the data too.
   * @param data
   *          The data to be assigned.
   */
  public void addData(DataType type, Object data)
  {
    if (blnIsStarted)
    {
      switch (type)
      {
        case MESSAGE_SENT:
          intSentMessage += (Integer) data;
          break;
        case MESSAGE_RECEIVED:
          intReceivedMessage += (Integer) data;
          break;
        case MESSAGE_DISCARDED:
          intDiscardedMessage += (Integer) data;
          break;
        default:
          // Do Nothing
          break;
      }
    }
  }
}