package com.maxxton.aam.monitoring;

import java.util.Set;

import com.maxxton.aam.monitoring.Monitor.DataType;
import com.maxxton.aam.monitoring.Monitor.MonitorLevel;
import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

/**
 * Provides the functionality for sending logs, data and status to the Zabbix Server (based on requested keys).
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessengerProvider implements MetricsProvider
{
  /**
   * Gets the logs as a string by a given type.
   * 
   * @param name
   *          the name of the messenger.
   * @param type
   *          the type of the logs to retrieve.
   * @return a string of the requested logs by type.
   */
  private String getLogs(String name, String type)
  {
    Monitor objMonitor = MonitorFactory.getMonitor(name);
    switch (type)
    {
      case "error":
        return objMonitor.drainLogs(MonitorLevel.ERROR);
      case "warn":
        return objMonitor.drainLogs(MonitorLevel.WARN);
      case "info":
        return objMonitor.drainLogs(MonitorLevel.INFO);
      case "debug":
        return objMonitor.drainLogs(MonitorLevel.DEBUG);
      case "trace":
        return objMonitor.drainLogs(MonitorLevel.TRACE);
      default:
        throw new MetricsException("Unknown parameter: " + type);
    }
  }

  /**
   * Gets the data as a string by a given type.
   * 
   * @param name
   *          the name of the messenger.
   * @param type
   *          the type of the data to retrieve.
   * @return a string of the requested data by type.
   */
  private Object getData(String name, String type)
  {
    Monitor objMonitor = MonitorFactory.getMonitor(name);
    switch (type)
    {
      case "sent":
        return objMonitor.drainData(DataType.MESSAGE_SENT);
      case "received":
        return objMonitor.drainData(DataType.MESSAGE_RECEIVED);
      case "discarded":
        return objMonitor.drainData(DataType.MESSAGE_DISCARDED);
      default:
        throw new MetricsException("Unknown parameter: " + type);
    }
  }

  /**
   * Gets the status of certain messenger.
   * 
   * @param name
   *          the name of the messenger.
   * @param type
   *          the type of status to receive.
   * @return the requested status.
   */
  private Object getStatus(String name, String type)
  {
    Set<String> keys = MonitorFactory.getMonitorInstances();
    if (keys.contains(name))
      return "OK";
    return "NOT OK";
  }

  @Override
  public Object getValue(MetricsKey mKey) throws MetricsException
  {
    switch (mKey.getKey())
    {
      case "logs":
        return this.getLogs(mKey.getParameters()[0], mKey.getParameters()[1]);
      case "data":
        return this.getData(mKey.getParameters()[0], mKey.getParameters()[1]);
      case "status":
        return this.getStatus(mKey.getParameters()[0], mKey.getParameters()[1]);

      default:
        throw new MetricsException("Unknown Key: " + mKey.getKey());
    }
  }

}
