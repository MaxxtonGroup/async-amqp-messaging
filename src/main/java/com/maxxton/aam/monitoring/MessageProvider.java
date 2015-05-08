package com.maxxton.aam.monitoring;

import com.maxxton.aam.monitoring.Zabbix.DataType;
import com.quigley.zabbixj.metrics.MetricsException;
import com.quigley.zabbixj.metrics.MetricsKey;
import com.quigley.zabbixj.metrics.MetricsProvider;

/**
 * MessageProdiver. Takes care of monitoring information to be send to Zabbix.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessageProvider implements MetricsProvider
{
  private Zabbix objZabbix;

  /**
   * Constructor for the MessageProvider.
   * 
   * @param zabbix
   *          An instance of the Zabbix class.
   */
  public MessageProvider(Zabbix zabbix)
  {
    this.objZabbix = zabbix;
  }

  @Override
  public Object getValue(MetricsKey mKey) throws MetricsException
  {
    switch (mKey.getKey())
    {
      case "sent":
        return new Long((Integer) this.objZabbix.getDataByType(DataType.MESSAGE_SENT));
      case "received":
        return new Long((Integer) this.objZabbix.getDataByType(DataType.MESSAGE_RECEIVED));
      case "discarded":
        return new Long((Integer) this.objZabbix.getDataByType(DataType.MESSAGE_DISCARDED));
      default:
        throw new MetricsException("Unknown Key: " + mKey.getKey());
    }
  }
}
