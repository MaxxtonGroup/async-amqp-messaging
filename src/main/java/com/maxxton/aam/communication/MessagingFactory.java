package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.Map;

/**
 * MessagingFactory class. Supports the creation and (prevents) duplication of Messenger objects. It implements the Creational pattern (with Singleton pattern as parent), as this is the best approach
 * for such a class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessagingFactory
{
  private static MessagingFactory objInstance = null;

  private Map<String, Messenger> mMessengerMap;

  /**
   * Private constructor for the MessagingFactory class.
   */
  private MessagingFactory()
  {
    this.mMessengerMap = new HashMap<String, Messenger>();
  }

  /**
   * Threadsafe getInstance method for the Singleton pattern.
   * 
   * @return a newly created or existing instance of this class.
   */
  public static MessagingFactory getInstance()
  {
    if (objInstance == null)
    {
      synchronized (MessagingFactory.class)
      {
        if (objInstance == null)
        {
          objInstance = new MessagingFactory();
        }
      }
    }
    return objInstance;
  }

  /**
   * Creates and/or returns an instance on the Messenger class based on a given name.
   * 
   * @param name
   *          the messenger name
   * @return a newly created or existing instance of the Messenger class.
   */
  public Messenger createMessenger(String name)
  {
    Messenger messenger = null;
    if(name.matches("[a-zA-Z]+"))
    {
       if (this.mMessengerMap.containsKey(name))
      {
        messenger = this.mMessengerMap.get(name.toLowerCase());
      }
      else
      {
        messenger = new Messenger(name.toLowerCase());
        this.mMessengerMap.put(name.toLowerCase(), messenger);
      }
    }
    return messenger;
  }

  /**
   * Destroys the instance of a given Messenger name.
   *
   * @param name
   *          the messenger name
   */
  public boolean destroyMessenger(String name)
  {
    if (this.mMessengerMap.containsKey(name))
    {
      this.mMessengerMap.remove(name);
      return true;
    }
    return false;
  }

}
