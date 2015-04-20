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
    this.objHost = new Host();
    this.objMessageFactory = new MessageFactory();
    this.objMonitor = new Monitor();
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
   * Gets the MessagingFactory class.
   *
   * @return an instance of the MessageFactory class.
   */
  public MessageFactory getMessageFactory()
  {
    return this.objMessageFactory;
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
}
