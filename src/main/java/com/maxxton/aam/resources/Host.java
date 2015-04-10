package com.maxxton.aam.resources;

/**
 * Host class Holds the information required by the Messenger to be able to connect to the broker. This configuration can be loaded dynamically upon runtime by using a .property or .xml file.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Host
{
  private String strMessengerName;

  /**
   * Constructor for the Host class.
   */
  public Host()
  {

  }

  /**
   * Sets the Messenger name.
   * 
   * @param messengerName
   *          name of the messenger
   */
  public void setMessengerName(String messengerName)
  {
    this.strMessengerName = messengerName;
  }

  /**
   * Gets the Messenger name.
   * 
   * @return the name of the messenger
   */
  public String getMessengerName()
  {
    return this.strMessengerName;
  }
}
