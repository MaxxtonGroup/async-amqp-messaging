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

  /**
   * Constructor for the Host class.
   */
  public Configuration()
  {
    this.setPorts(new ArrayList<Integer>());

    this.loadConfiguration(getClass().getClassLoader().getResource("default.properties").getPath());
  }

  private void loadConfiguration(String configFile)
  {
    Properties properties = ConfigParser.parseConfig(configFile);

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

}
