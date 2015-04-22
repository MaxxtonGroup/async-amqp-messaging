package com.maxxton.aam.resources;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Host class Holds the information required by the Messenger to be able to connect to the broker. This configuration can be loaded dynamically upon runtime by using a .property or .xml file.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Configuration
{
  private String strName;
  private String strHost;
  private ArrayList<Integer> arrPorts;
  private String strUsername;
  private String strPassword;

  private String strQueuePrefix;
  private String strQueueSuffix;
  private boolean bQueueDurability;
  private boolean bQueueAutoDelete;
  private boolean bQueueExclusive;

  private String strBindingPrefix;
  private String strBindingSuffix;
  private String strBindingExchange;

  /**
   * Constructor for the Host class.
   */
  public Configuration()
  {
    this.setPorts(new ArrayList<Integer>());

    this.loadConfiguration(getClass().getClassLoader().getResource("default.properties").getPath());
  }

  /**
   * Loads a given configuration file.
   *
   * @param configFile
   */
  private void loadConfiguration(String configFile)
  {
    Properties properties = ConfigParser.parseConfig(configFile);

    if (properties != null)
    {
      if (properties.getProperty("broker.ports") != null)
      {
        String ports = properties.getProperty("broker.ports").replace("\\s", "");
        if (ports.matches("^[0-9,]+$"))
        {
          List<String> strPorts = Arrays.asList(ports.split(","));
          for (String port : strPorts)
          {
            this.addPort(Integer.parseInt(port));
          }
        }
      }

      this.setHost(properties.getProperty("broker.host") == null ? this.getHost() : properties.getProperty("broker.host"));
      this.setUsername(properties.getProperty("broker.username") == null ? this.getUsername() : properties.getProperty("broker.username"));
      this.setPassword(properties.getProperty("broker.password") == null ? this.getPassword() : properties.getProperty("broker.password"));

      this.setQueuePrefix(properties.getProperty("queue.prefix") == null ? this.getQueuePrefix() : properties.getProperty("queue.prefix"));
      this.setQueueSuffix(properties.getProperty("queue.suffix") == null ? this.getQueueSuffix() : properties.getProperty("queue.suffix"));
      this.setQueueDurability(properties.getProperty("queue.durability") == null ? this.getQueueDurability() : Boolean.parseBoolean(properties.getProperty("queue.durability")));
      this.setQueueAutoDelete(properties.getProperty("queue.autodelete") == null ? this.getQueueAutoDelete() : Boolean.parseBoolean(properties.getProperty("queue.autodelete")));
      this.setQueueExclusive(properties.getProperty("queue.exclusive") == null ? this.getQueueExclusive() : Boolean.parseBoolean(properties.getProperty("queue.exclusive")));
      
      this.setBindingPrefix(properties.getProperty("binding.prefix") == null ? this.getBindingPrefix() : properties.getProperty("binding.prefix"));
      this.setBindingSuffix(properties.getProperty("binding.suffix") == null ? this.getBindingSuffix() : properties.getProperty("binding.suffix"));
      this.setBindingExchange(properties.getProperty("binding.exchange") == null ? this.getBindingExchange() : properties.getProperty("binding.exchange"));
    }
  }

  /**
   * Sets the Messenger name.
   * 
   * @param messengerName
   *          name of the messenger
   */
  public void setName(String name)
  {
    this.strName = name;
  }

  /**
   * Gets the Messenger name.
   * 
   * @return the name of the messenger
   */
  public String getName()
  {
    return this.strName;
  }

  /**
   * Sets the host address.
   *
   * @param host
   *          the host address as string.
   */
  public void setHost(String host)
  {
    this.strHost = host;
  }

  /**
   * Gets the host address.
   *
   * @return the host address as String.
   */
  public String getHost()
  {
    return this.strHost;
  }

  /**
   * Adds a port to the ArrayList of ports.
   *
   * @param port
   *          the port to be added to the list.
   */
  public void addPort(int port)
  {
    this.arrPorts.add(port);
  }

  /**
   * Removes a port form the ArrayList of ports.
   *
   * @param port
   *          the port to be added to the list.
   */
  public void removePort(int port)
  {
    this.arrPorts.remove(port);
  }

  /**
   * Sets the port.
   *
   * @param port
   *          the port of the broker.
   */
  public void setPorts(ArrayList<Integer> port)
  {
    this.arrPorts = port;
  }

  /**
   * Gets the port.
   *
   * @return the port of the broker.
   */
  public ArrayList<Integer> getPorts()
  {
    return this.arrPorts;
  }

  /**
   * Sets the username.
   *
   * @param username
   *          the name of the user as string.
   */
  public void setUsername(String username)
  {
    this.strUsername = username;
  }

  /**
   * Gets the username.
   *
   * @return the name of the user as string.
   */
  public String getUsername()
  {
    return this.strUsername;
  }

  /**
   * Sets the password.
   *
   * @param password
   *          the password as string.
   */
  public void setPassword(String password)
  {
    this.strPassword = password;
  }

  /**
   * Gets the password.
   *
   * @return the password as string.
   */
  public String getPassword()
  {
    return this.strPassword;
  }

  /**
   * Sets the queue prefix.
   *
   * @param prefix
   *          the prefix for all queues.
   */
  public void setQueuePrefix(String prefix)
  {
    this.strQueuePrefix = prefix;
  }

  /**
   * Gets the queue prefix.
   *
   * @return the prefix for all queues.
   */
  public String getQueuePrefix()
  {
    return this.strQueuePrefix;
  }

  /**
   * Sets the queue suffix.
   *
   * @param suffix
   *          the suffix for all queues.
   */
  public void setQueueSuffix(String suffix)
  {
    this.strQueueSuffix = suffix;
  }

  /**
   * Gets the queue suffix.
   *
   * @return the suffix for all queues.
   */
  public String getQueueSuffix()
  {
    return this.strQueueSuffix;
  }

  /**
   * Sets the durability option of the queue.
   *
   * @param durability
   *          the durability option of the queue.
   */
  public void setQueueDurability(boolean durability)
  {
    this.bQueueDurability = durability;
  }

  /**
   * Gets the durability option of the queue.
   *
   * @return the durability option of the queue.
   */
  public boolean getQueueDurability()
  {
    return this.bQueueDurability;
  }

  /**
   * Sets the autodelete option of the queue.
   *
   * @param autodelete
   *          the autodelete option of the queue.
   */
  public void setQueueAutoDelete(boolean autodelete)
  {
    this.bQueueAutoDelete = autodelete;
  }

  /**
   * Gets the autodetele option of the queue.
   *
   * @return the autodelete option of the queue.
   */
  public boolean getQueueAutoDelete()
  {
    return this.bQueueAutoDelete;
  }

  /**
   * Set the exclusive option for the queue.
   *
   * @param exclusive
   *          the exclusive option of the queue.
   */
  public void setQueueExclusive(boolean exclusive)
  {
    this.bQueueExclusive = exclusive;
  }

  /**
   * Gets the exclusive option for the queue.
   *
   * @return the exclusive option of the queue.
   */
  public boolean getQueueExclusive()
  {
    return this.bQueueExclusive;
  }

  /**
   * Set the prefix for all bindings.
   *
   * @param prefix
   *          the prefix for all bindings
   */
  public void setBindingPrefix(String prefix)
  {
    this.strBindingPrefix = prefix;
  }

  /**
   * Gets the prefix of all bindings.
   *
   * @return the prefix for all binding.
   */
  public String getBindingPrefix()
  {
    return this.strBindingPrefix;
  }

  /**
   * Sets the suffix of all bindings.
   *
   * @param suffix
   *          the suffix of all bindings.
   */
  public void setBindingSuffix(String suffix)
  {
    this.strBindingSuffix = suffix;
  }

  /**
   * Gets the suffix of all binding.
   * 
   * @return the suffix of all bindings.
   */
  public String getBindingSuffix()
  {
    return this.strBindingSuffix;
  }

  /**
   * Sets the exchange for all bindings.
   *
   * @param exchange
   *          the exchange for all bindings.
   */
  public void setBindingExchange(String exchange)
  {
    this.strBindingExchange = exchange;
  }

  /**
   * Gets the exchange for all bindings.
   *
   * @return the exchange for all bindings.
   */
  public String getBindingExchange()
  {
    return this.strBindingExchange;
  }

}
