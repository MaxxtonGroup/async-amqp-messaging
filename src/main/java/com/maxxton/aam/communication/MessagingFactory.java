package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.Map;

import com.maxxton.aam.monitoring.Monitor;
import com.maxxton.aam.resources.Validator;

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
    if (Validator.checkObject(objInstance, true))
    {
      synchronized (MessagingFactory.class)
      {
        if (Validator.checkObject(objInstance, true))
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
    if (Validator.checkString(name, "[a-zA-Z]+"))
    {
      if (this.mMessengerMap.containsKey(name.toLowerCase()))
      {
        messenger = this.mMessengerMap.get(name.toLowerCase());
      }
      else
      {
        messenger = new Messenger(name.toLowerCase());
        this.mMessengerMap.put(name.toLowerCase(), messenger);
      }
    }
    else
    {
      Monitor.warn("No messenger instance created or returned due incorrect name.");
    }
    return messenger;
  }

  /**
   * Destroys the instance of a given Messenger name.
   *
   * @param name
   *          the messenger name
   * @return true if the messenger was deleted, false if it didn't.
   */
  public boolean destroyMessenger(String name)
  {
    if (Validator.checkString(name, "[a-zA-Z]+"))
    {
      if (this.mMessengerMap.containsKey(name))
      {
        this.mMessengerMap.remove(name);
        return true;
      }
      else
      {
        Monitor.info("No Messenger found with name '" + name + "'");
      }
    }
    else
    {
      Monitor.warn("No messenger instance deleted due incorrect name");
    }
    return false;
  }

}
