package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.amqp.core.Message;

import com.maxxton.aam.resources.Configuration;
import com.maxxton.aam.resources.Resources;
import com.maxxton.aam.resources.Validator;

/**
 * DataContainer class Contains all data which passes the Send- and/or ReceiveController. This may include id's and messages.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class DataContainer
{
  private static Map<String, DataContainer> mInstances = new HashMap<String, DataContainer>();

  private final ReentrantReadWriteLock objSendLock = new ReentrantReadWriteLock(true);
  private final ReentrantReadWriteLock objReceivedLock = new ReentrantReadWriteLock(true);
  private final ReentrantReadWriteLock objIdentifiersLock = new ReentrantReadWriteLock(true);

  private final ScheduledExecutorService objExecutor = Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> objScheduler;

  private String sName;
  private Resources objResources;
  private Set<String> setIdentifiers;
  private Set<Message> setSendMessages;
  private Set<Message> setReceivedMessages;
  private Set<Message> setOddMessages;

  /**
   * DataContainer constructor Initiates elements defined in this class
   */
  private DataContainer()
  {
    this.setIdentifiers = new HashSet<String>();
    this.setSendMessages = new HashSet<Message>();
    this.setReceivedMessages = new HashSet<Message>();
    this.setOddMessages = new HashSet<Message>();
  }

  /**
   * Sets the resources class for the DataContainer worker.
   * 
   * @param resources
   *          an instance of the resources.
   */
  public void setResources(Resources resources)
  {
    this.objResources = resources;
    Configuration config = this.objResources.getConfiguration();

    if (Validator.checkObject(this.objScheduler, true))
    {
      this.objScheduler = objExecutor.scheduleAtFixedRate(new Runnable()
      {
        @Override
        public void run()
        {
          garbageCleanup();
        }
      }, config.getDataCleanRate(), config.getDataCleanRate(), TimeUnit.SECONDS);
    }
  }

  /**
   * Creates or returns an (existing) instance of this class by key. Support the creation of multiple singletons.
   * 
   * @param key
   *          Identifier which may refer to a existing object
   * @return an instance of this class
   */
  public static DataContainer getInstance(String key)
  {
    DataContainer container = mInstances.get(key);
    if (container == null)
    {
      synchronized (mInstances)
      {
        container = mInstances.get(key);

        if (container == null)
        {
          container = new DataContainer();
          container.setName(key);
          mInstances.put(key, container);
        }
      }
    }
    return container;
  }

  /**
   * Cleans up any HashSet with more than maximum allowed amount of object.
   */
  public void garbageCleanup()
  {
    this.identifierCleanup();
    this.sendCleanup();
    this.receiveCleanup();
  }

  /**
   * Cleans up the HashSet with identifiers.
   */
  private void identifierCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.setIdentifiers.size() > config.getDataMaxElements())
    {
      Iterator<String> it = this.setIdentifiers.iterator();
      int count = 1;
      this.objIdentifiersLock.writeLock().lock();
      try
      {
        while (it.hasNext())
        {
          it.next();
          if (count > config.getDataMaxElements())
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.objIdentifiersLock.writeLock().unlock();
      }
    }
  }

  /**
   * Cleans up the HashSet with send messages.
   */
  private void sendCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.setSendMessages.size() > config.getDataMaxElements())
    {
      this.objSendLock.writeLock().lock();
      try
      {
        Iterator<Message> it = this.setSendMessages.iterator();
        int count = 1;
        while (it.hasNext())
        {
          it.next();
          if (count > config.getDataMaxElements())
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.objSendLock.writeLock().unlock();
      }
    }
  }

  /**
   * Cleans up the HashSet with received messages.
   */
  private void receiveCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.setReceivedMessages.size() > config.getDataMaxElements())
    {
      this.objReceivedLock.writeLock().lock();
      try
      {
        Iterator<Message> it = this.setReceivedMessages.iterator();
        int count = 1;
        while (it.hasNext())
        {
          it.next();
          if (count > config.getDataMaxElements())
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.objReceivedLock.writeLock().unlock();
      }
    }
  }

  /**
   * Destroys the DataContainer object and all it's data.
   */
  public void destroy()
  {
    this.objExecutor.shutdown();

    this.setIdentifiers = new HashSet<String>();
    this.setSendMessages = new HashSet<Message>();
    this.setReceivedMessages = new HashSet<Message>();
    this.setOddMessages = new HashSet<Message>();

    mInstances.remove(this.sName);
  }

  /**
   * Sets the name of this DataContainer instance.
   *
   * @param name
   *          the name of this DataContainer
   */
  private void setName(String name)
  {
    this.sName = name;
  }

  /**
   * Gets the name of this DataContainer instance.
   *
   * @return the name of this DataContainer instance.
   */
  public String getName()
  {
    return this.sName;
  }

  /**
   * Gets an unique identifier
   * 
   * @return an unique id given as string
   */
  public String getUniqueId()
  {
    String id = UUID.randomUUID().toString();
    this.setIdentifiers.add(id);
    return id;
  }

  /**
   * Check whether or not a message was created and send from this client.
   * 
   * @param id
   *          identifier of a message
   * @return true if owned, false if not.
   */
  public boolean isOwnedByMe(String id)
  {
    this.objIdentifiersLock.readLock().lock();
    try
    {
      if (this.setIdentifiers.contains(id))
      {
        return true;
      }
      return false;
    }
    finally
    {
      this.objIdentifiersLock.readLock().unlock();
    }
  }

  /**
   * Adds a given messageid to the set.
   *
   * @param id
   *          identifier of the message
   */
  public void addIdentifier(String id)
  {
    this.objIdentifiersLock.writeLock().lock();
    try
    {
      this.setIdentifiers.add(id);
    }
    finally
    {
      this.objIdentifiersLock.writeLock().unlock();
    }
  }

  /**
   * Removes a given messagid from the list if it exists.
   * 
   * @param id
   *          identifier of a message
   */
  public void removeIdentifier(String id)
  {
    this.objIdentifiersLock.writeLock().lock();
    this.objIdentifiersLock.readLock().lock();
    try
    {
      if (this.setIdentifiers.contains(id))
      {
        this.setIdentifiers.remove(id);
      }
    }
    finally
    {
      this.objIdentifiersLock.readLock().unlock();
      this.objIdentifiersLock.writeLock().unlock();
    }
  }

  /**
   * Getter for HashSet with string bases identifiers.
   * 
   * @return an Set of identifiers.
   */
  public Set<String> getIdentifiers()
  {
    return this.setIdentifiers;
  }

  /**
   * Setter for Set with string bases identifiers.
   * 
   * @param identifiers
   *          an Set of string identifiers.
   */
  public void setIdentifiers(Set<String> identifiers)
  {
    this.setIdentifiers = identifiers;
  }

  /**
   * Adds a given message to the send Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addSendMessage(Message message)
  {
    // TODO : find solution for overflowing send messages set (run thread that cleans periodically).
    this.objSendLock.writeLock().lock();
    try
    {
      this.setSendMessages.add(message);
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        this.addIdentifier(message.getMessageProperties().getCorrelationId().toString());
      }
    }
    finally
    {
      this.objSendLock.writeLock().unlock();
    }
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeSendMessage(Message message)
  {
    this.objSendLock.writeLock().lock();
    try
    {
      this.setSendMessages.remove(message);
    }
    finally
    {
      this.objSendLock.writeLock().unlock();
    }
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param id
   *          identifier of the message
   */
  public void removeSendMessageById(String id)
  {
    this.objSendLock.readLock().lock();
    try
    {
      for (Message message : this.setSendMessages)
      {
        if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
        {
          String messageId = message.getMessageProperties().getCorrelationId().toString();
          if (messageId.equals(id))
          {
            this.removeSendMessage(message);
          }
        }
      }
    }
    finally
    {
      this.objSendLock.readLock().unlock();
    }
  }

  /**
   * Setter for the messages send Set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setSendMessages(Set<Message> messages)
  {
    this.setSendMessages = messages;
  }

  /**
   * Getter for the messages send Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getSendMessages()
  {
    return this.setSendMessages;
  }

  /**
   * Adds a given message to the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(Message message)
  {
    this.objReceivedLock.writeLock().lock();
    try
    {
      // TODO : Make sure that messages which the same correlationId override each other
      this.setReceivedMessages.add(message);
    }
    finally
    {
      this.objReceivedLock.writeLock().unlock();
    }
  }

  /**
   * Pops the oldest received message from the array
   * 
   * @return the oldest message from the array
   */
  public Message popReceivedMessage()
  {
    this.objReceivedLock.readLock().lock();
    Message message = null;
    try
    {
      Iterator<Message> it = this.setReceivedMessages.iterator();
      while (it.hasNext())
      {
        message = (Message) it.next();
      }
    }
    finally
    {
      this.objReceivedLock.readLock().unlock();
    }

    this.removeReceivedMessage(message);
    return message;
  }

  /**
   * Removes a given message from the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeReceivedMessage(Message message)
  {
    this.objReceivedLock.writeLock().lock();
    try
    {
      this.setReceivedMessages.remove(message);
    }
    finally
    {
      this.objReceivedLock.writeLock().unlock();
    }
  }

  /**
   * Removes a given message from the received Set.
   * 
   * @param id
   *          identifier of the message
   */
  public void removeReceivedMessageById(String id)
  {
    this.objReceivedLock.readLock().lock();
    try
    {
      for (Message message : this.setReceivedMessages)
      {
        if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
        {
          String messageId = (String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId());
          if (messageId.equals(id))
          {
            this.removeReceivedMessage(message);
          }
        }
      }
    }
    finally
    {
      this.objReceivedLock.readLock().unlock();
    }
  }

  /**
   * Setter for the messages receive Set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setReceivedMessages(Set<Message> messages)
  {
    this.objReceivedLock.writeLock().lock();
    try
    {
      this.setReceivedMessages = messages;
    }
    finally
    {
      this.objReceivedLock.writeLock().unlock();
    }
  }

  /**
   * Getter for the messages receive Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getReceivedMessages()
  {
    this.objReceivedLock.readLock().lock();
    try
    {
      return this.setReceivedMessages;
    }
    finally
    {
      this.objReceivedLock.readLock().unlock();
    }
  }

  /**
   * Adds a message instance to the odd messages set.
   * 
   * @param message
   *          an instance of a message object.
   */
  public void addOddMessage(Message message)
  {
    this.setOddMessages.add(message);
  }

  /**
   * Removes a given message object from the odd messages set.
   * 
   * @param message
   *          instance of the Message object
   */
  public void removeOddMessage(Message message)
  {
    this.setOddMessages.remove(message);
  }

  /**
   * Removes a message object by id from the odd messages set.
   * 
   * @param id
   *          the message identifier as string.
   */
  public void removeOddMessageById(String id)
  {
    for (Message message : this.setOddMessages)
    {
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        String messageId = (String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId());
        if (messageId.equals(id))
        {
          this.setOddMessages.remove(message);
        }
      }
    }
  }

  /**
   * Getter for the odd messages set.
   * 
   * @return a list of odd received messages.
   */
  public Set<Message> getOddMessages()
  {
    return this.setOddMessages;
  }

  /**
   * Setter for the odd messages set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setOddMessages(Set<Message> messages)
  {
    this.setOddMessages = messages;
  }
}
