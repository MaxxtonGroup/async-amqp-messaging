package com.maxxton.aam.resources;

import java.net.InetAddress;

import com.quigley.zabbixj.agent.ZabbixAgent;
import com.quigley.zabbixj.providers.JVMMetricsProvider;

/**
 * Zabbix class Support monitoring of the client through the Zabbix agent.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Zabbix
{
  private ZabbixAgent zbxAgent;

  private String strHostname;
  private String strServerAddress;
  private int intServerPort;

  /**
   * Sets up the agent's settings. You still need to call start to
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
    try
    {
      this.zbxAgent.start();
    }
    catch (Exception e)
    {
      Monitor.error("Failed to start the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
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
}