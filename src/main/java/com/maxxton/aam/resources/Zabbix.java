package com.maxxton.aam.resources;

import java.net.InetAddress;
import java.util.ArrayList;

import com.maxxton.aam.resources.Monitor.MonitorLevel;
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
  private ZabbixAgent zbxAgent;
  private boolean bIsStarted;

  private String strHostname;
  private String strServerAddress;
  private int intServerPort;

  private ArrayList<String> arrErrorLog;
  private ArrayList<String> arrWarnLog;
  private ArrayList<String> arrInfoLog;
  private ArrayList<String> arrDebugLog;
  private ArrayList<String> arrTraceLog;

  /**
   * Zabbix class constructor.
   */
  public Zabbix()
  {
    this.arrErrorLog = new ArrayList<String>();
    this.arrWarnLog = new ArrayList<String>();
    this.arrInfoLog = new ArrayList<String>();
    this.arrDebugLog = new ArrayList<String>();
    this.arrTraceLog = new ArrayList<String>();

    this.bIsStarted = false;
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
      this.zbxAgent = new ZabbixAgent();
      this.zbxAgent.setEnableActive(true);
      this.zbxAgent.setEnablePassive(false);
      this.zbxAgent.setHostName(hostname);
      this.zbxAgent.setServerAddress(InetAddress.getByName(serverAddress));
      this.zbxAgent.setServerPort(serverPort);

      this.zbxAgent.addProvider("java", new JVMMetricsProvider());
      this.zbxAgent.addProvider("heartbeat", new HeartBeatProvider());
      this.zbxAgent.addProvider("logs", new LogProvider(this));

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
    if (!this.bIsStarted)
    {
      try
      {
        this.zbxAgent.start();
      }
      catch (Exception e)
      {
        Monitor.error("Failed to start the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
      }
      this.bIsStarted = true;
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
        return this.drainArrayList("ERROR: ", this.arrErrorLog);
      case WARN:
        return this.drainArrayList("WARN: ", this.arrWarnLog);
      case INFO:
        return this.drainArrayList("INFO: ", this.arrInfoLog);
      case DEBUG:
        return this.drainArrayList("DEBUG: ", this.arrDebugLog);
      case TRACE:
        return this.drainArrayList("TRACE: ", this.arrTraceLog);
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
  public void addLog(MonitorLevel level, String log)
  {
    if (bIsStarted)
    {
      switch (level)
      {
        case ERROR:
          this.arrErrorLog.add(log);
          break;
        case WARN:
          this.arrWarnLog.add(log);
          break;
        case INFO:
          this.arrInfoLog.add(log);
          break;
        case DEBUG:
          this.arrDebugLog.add(log);
          break;
        case TRACE:
          this.arrTraceLog.add(log);
          break;
        default:
          // Do Nothing...
          break;
      }
    }
  }
}