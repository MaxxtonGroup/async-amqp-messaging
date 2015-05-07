package com.maxxton.aam.resources;

import com.maxxton.aam.resources.Monitor.MonitorLevel;
import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

/**
 * Provider for the logs related keys of the (active) Zabbix agent.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class LogProvider implements MetricsProvider
{
  private Zabbix objZabbix;

  /**
   * Constructor for the LogProvider.
   * 
   * @param zabbix
   *          An instance of the Zabbix class.
   */
  public LogProvider(Zabbix zabbix)
  {
    this.objZabbix = zabbix;
  }

  @Override
  public Object getValue(MetricsKey mKey) throws MetricsException
  {
    switch (mKey.getKey())
    {
      case "error":
        return this.objZabbix.getLogsByLevel(MonitorLevel.ERROR);
      case "warn":
        return this.objZabbix.getLogsByLevel(MonitorLevel.WARN);
      case "info":
        return this.objZabbix.getLogsByLevel(MonitorLevel.INFO);
      case "debug":
        return this.objZabbix.getLogsByLevel(MonitorLevel.DEBUG);
      case "trace":
        return this.objZabbix.getLogsByLevel(MonitorLevel.TRACE);
      default:
        throw new MetricsException("Unknown Key: " + mKey.getKey());
    }
  }
}
