package com.maxxton.aam.resources;

/**
 * Resources class Packs all required classes within the resource package together.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Resources
{
  private Host objHost;
  private MessageFactory objMessageFactory;
  private Monitor objMonitor;

  /**
   * Constructor for the Resources class.
   */
  public Resources()
  {
    Host host = new Host();
    // TODO : Configure Host
    this.setHost(host);

    MessageFactory messageFactory = new MessageFactory();
    // TODO : Configure MessageFactory
    this.setMessageFactory(messageFactory);

    Monitor monitor = new Monitor();
    // TODO : Configure Monitor
    this.setMonitor(monitor);
  }

  /**
   * Gets the Host instance.
   * 
   * @return an instance of the Host class.
   */
  public Host getHost()
  {
    return this.objHost;
  }

  /**
   * Sets the Host instance.
   *
   * @param host
   *          instance of the Host class.
   */
  private void setHost(Host host)
  {
    this.objHost = host;
  }

  /**
   * Gets the MessagingFactory class.
   *
   * @return an instance of the MessageFactory class.
   */
  public MessageFactory getMessageFactory()
  {
    return this.objMessageFactory;
  }

  /**
   * Sets the MessageFactory instance.
   *
   * @param messageFactory
   *          instance of the MessageFactory class.
   */
  private void setMessageFactory(MessageFactory messageFactory)
  {
    this.objMessageFactory = messageFactory;
  }

  /**
   * Gets the Monitor class.
   *
   * @return an instance of the Monitor class.
   */
  public Monitor getMonitor()
  {
    return this.objMonitor;
  }

  /**
   * Sets the Monitor class.
   *
   * @param monitor
   *          instance of the Monitor class.
   */
  private void setMonitor(Monitor monitor)
  {
    this.objMonitor = monitor;
  }
}
