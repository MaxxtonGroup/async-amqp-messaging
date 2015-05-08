package com.maxxton.aam.monitoring;

import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

/**
 * Provider for the heartbeat related keys of the (active) Zabbix agent.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class HeartBeatProvider implements MetricsProvider
{

  @Override
  public Object getValue(MetricsKey mKey) throws MetricsException
  {
    switch (mKey.getKey())
    {
      case "status":
        return "OK";
      default:
        throw new MetricsException("Unknown Key: " + mKey.getKey());
    }
  }

}
