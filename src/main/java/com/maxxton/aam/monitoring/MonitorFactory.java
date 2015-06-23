package com.maxxton.aam.monitoring;

import java.net.InetAddress;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.maxxton.aam.monitoring.Monitor.MonitorLevel;
import com.maxxton.aam.resources.ConfigParser;
import com.maxxton.aam.resources.Validator;
import com.quigley.zabbixj.agent.ZabbixAgent;

/**
 * The MonitorFactory used to use and track Monitor instances by name
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MonitorFactory extends AppenderSkeleton
{
  private static ConcurrentHashMap<String, Monitor> mapMonitors = new ConcurrentHashMap<String, Monitor>();
  private static Monitor objMonitor = MonitorFactory.getMonitor("global");

  private static MonitorLevel enmMonitorLevel;

  private static String strHostname;
  private static String strServerAddress;
  private static int intServerPort;
  private static boolean blnEnabled;
  private static boolean blnIsStarted;
  private static ZabbixAgent objAgent;

  /**
   * Static constructor of the MonitorFactory.
   */
  static
  {
    MonitorFactory.blnIsStarted = false;
    MonitorFactory.blnEnabled = false;
    MonitorFactory.enmMonitorLevel = MonitorLevel.ALL;
    MonitorFactory.loadConfiguration("/default.properties");
  }

  /**
   * Gets a monitor instance by messenger name.
   * 
   * @param key
   *          The name of the messenger.
   * @return an instance of the Monitor class.
   */
  public static Monitor getMonitor(String key)
  {
    Monitor instance = mapMonitors.get(key);
    if (Validator.checkObject(instance, true))
    {
      instance = mapMonitors.get(key);
      if (Validator.checkObject(instance, true))
      {
        instance = new Monitor(key, MonitorFactory.getEnabled());
        mapMonitors.put(key, instance);
      }
    }
    return instance;
  }

  /**
   * Loads the configuration from a given file.
   * 
   * @param configFile
   *          the configuration file to be loaded.
   */
  public static void loadConfiguration(String configFile)
  {
    if (!MonitorFactory.blnIsStarted)
    {
      Properties properties = ConfigParser.parseConfig(configFile);
      if (Validator.checkObject(properties))
      {
        Boolean bEnabled = properties.getProperty("monitor.enabled") == null ? MonitorFactory.getEnabled() : Boolean.parseBoolean(properties.getProperty("monitor.enabled"));
        MonitorFactory.setEnabled(bEnabled);

        MonitorLevel level = MonitorFactory.getMonitorLevel();
        String strMonitorLvl = properties.getProperty("monitor.level", level.toString());
        MonitorFactory.setMonitorLevel(MonitorFactory.determineLevel(strMonitorLvl));

        String hostname = properties.getProperty("monitor.hostname", MonitorFactory.getHostname());
        MonitorFactory.setHostname(hostname);

        String serverAddress = properties.getProperty("monitor.server.address", MonitorFactory.getServerAddress());
        MonitorFactory.setServerAdderss(serverAddress);

        int serverPort = properties.getProperty("monitor.server.port") == null ? MonitorFactory.getServerPort() : Integer.parseInt(properties.getProperty("monitor.server.port"));
        MonitorFactory.setServerPort(serverPort);

        MonitorFactory.setupAgent();
        MonitorFactory.updateLogger();
      }
    }
  }

  /**
   * Sets up the Zabbix Agent with the current configuration.
   */
  private static void setupAgent()
  {
    if (!MonitorFactory.blnIsStarted)
    {
      try
      {
        MonitorFactory.objAgent = new ZabbixAgent();
        MonitorFactory.objAgent.setEnableActive(true);
        MonitorFactory.objAgent.setEnablePassive(false);
        MonitorFactory.objAgent.setHostName(MonitorFactory.getHostname());
        MonitorFactory.objAgent.setServerAddress(InetAddress.getByName(MonitorFactory.getServerAddress()));
        MonitorFactory.objAgent.setServerPort(MonitorFactory.getServerPort());

        MonitorFactory.objAgent.addProvider("monitor", new DiscoverProvider());
        MonitorFactory.objAgent.addProvider("messenger", new MessengerProvider());
      }
      catch (Exception e)
      {
        MonitorFactory.objMonitor.error(MonitorFactory.class, 
            "Failed to initiate the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
      }
    }
  }

  /**
   * Updates the currently created Monitor instances to match new settings.
   */
  private static void updateLogger()
  {
    if (!MonitorFactory.blnIsStarted)
    {
      for (String key : MonitorFactory.mapMonitors.keySet())
      {
        Monitor monitor = MonitorFactory.mapMonitors.get(key);
        monitor.setEnabled(MonitorFactory.getEnabled());
        monitor.setMonitorLevel(MonitorFactory.getMonitorLevel());
      }
    }
  }

  /**
   * Gets a list with the current Monitor instances as string.
   * 
   * @return a set with names as strings.
   */
  public static Set<String> getMonitorInstances()
  {
    return mapMonitors.keySet();
  }

  /**
   * Starts the MonitorFactory instance and Zabbix agent.
   */
  public static void start()
  {
    if (!MonitorFactory.blnIsStarted)
    {
      if (MonitorFactory.blnEnabled)
      {
        try
        {
          MonitorFactory.objAgent.start();
          MonitorFactory.blnIsStarted = true;
        }
        catch (Exception e)
        {
          MonitorFactory.objMonitor.error(MonitorFactory.class, "Failed to start the Zabbix Agent. No active/passive communication with the server available, ALL monitoring dropped.");
        }
      }
      else
      {
        MonitorFactory.objMonitor.warn(MonitorFactory.class, "Zabbix monitoring is set as disabled in the configuration file. Please change this if you'd want to use this functionality.");
      }
    }
  }

  /**
   * Determines the MonitorLevel enum for a given string.
   * 
   * @param lvl
   *          the monitorlevel given as string.
   * @return a MonitorLevel enumeration.
   */
  public static MonitorLevel determineLevel(String lvl)
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
   * Gets the enabled state of the Zabbix Agent.
   * 
   * @return true if enabled, false if disabled.
   */
  private static boolean getEnabled()
  {
    return MonitorFactory.blnEnabled;
  }

  /**
   * Sets the enabled state of the Zabbix Agent.
   * 
   * @param enabled
   *          true if enabled, false if disabled.
   */
  private static void setEnabled(boolean enabled)
  {
    MonitorFactory.blnEnabled = enabled;
  }

  /**
   * Gets the level on which the Zabbix Agent starts monitoring.
   * 
   * @return enumeration of monitor level.
   */
  private static MonitorLevel getMonitorLevel()
  {
    return MonitorFactory.enmMonitorLevel;
  }

  /**
   * Sets the level on which the Zabbix Agent starts monitoring.
   * 
   * @param level
   *          enumeration of the monitro level.
   */
  private static void setMonitorLevel(MonitorLevel level)
  {
    MonitorFactory.enmMonitorLevel = level;
  }

  /**
   * Gets the hostname setting for the Zabbix Agent.
   * 
   * @return the hostname as string.
   */
  private static String getHostname()
  {
    return MonitorFactory.strHostname;
  }

  /**
   * Set the hostname setting for the Zabbix Agent.
   * 
   * @param hostname
   *          the hostname as string.
   */
  private static void setHostname(String hostname)
  {
    MonitorFactory.strHostname = hostname;
  }

  /**
   * Gets the server address setting for the Zabbix server.
   * 
   * @return the server address as string.
   */
  private static String getServerAddress()
  {
    return MonitorFactory.strServerAddress;
  }

  /**
   * Sets the server address setting for the zabbix server.
   * 
   * @param address
   *          the server address as string.
   */
  private static void setServerAdderss(String address)
  {
    MonitorFactory.strServerAddress = address;
  }

  /**
   * Gets the port setting for the zabbix server.
   * 
   * @return the port as integer.
   */
  private static int getServerPort()
  {
    return MonitorFactory.intServerPort;
  }

  /**
   * Sets the port setting for the zabbix server.
   * 
   * @param port
   *          the port as integer.
   */
  private static void setServerPort(int port)
  {
    MonitorFactory.intServerPort = port;
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
    if (event.getLocationInformation().getClassName() != Monitor.class.getName())
    {
      MonitorLevel level = MonitorFactory.determineLevel(event.getLevel().toString());
      MonitorFactory.objMonitor.addLog(level, "(" + event.getLocationInformation().getClassName() + ") " + event.getMessage());
    }
  }
}