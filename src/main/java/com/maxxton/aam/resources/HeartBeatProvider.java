package com.maxxton.aam.resources;

import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

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
