package com.maxxton.aam.monitoring;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

/**
 * Provides the functionality for the discovery key received from the Zabbix Server.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class DiscoverProvider implements MetricsProvider
{
  @Override
  public Object getValue(MetricsKey mKey) throws MetricsException
  {
    switch (mKey.getKey())
    {
      case "discovery":
        JSONObject object = new JSONObject();
        try
        {
          JSONArray array = new JSONArray();
          Set<String> names = MonitorFactory.getMonitorInstances();
          for (String name : names)
          {
            JSONObject innerObject = new JSONObject();
            innerObject.put("{#MSGNAME}", name);
            array.put(innerObject);
          }
          object.put("data", array);
        }
        catch (JSONException e)
        {
          e.printStackTrace();
        }

        return object;
      default:
        throw new MetricsException("Unknown Key: " + mKey.getKey());
    }
  }
}
