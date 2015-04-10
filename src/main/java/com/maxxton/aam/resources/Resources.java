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
  
  /**
   * Constructor for the Resources class.
   */
  public Resources()
  {
    this.objHost = new Host();
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
}
