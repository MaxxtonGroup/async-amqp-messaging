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

/**
 * DataContainer class Contains all data which passes the Send- and/or ReceiveController. This may include id's and messages.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class DataContainer
{
  private static Map<String, DataContainer> mInstances = new HashMap<String, DataContainer>();

  private final ReentrantReadWriteLock rwSendLock = new ReentrantReadWriteLock(true);
  private final ReentrantReadWriteLock rwReceivedLock = new ReentrantReadWriteLock(true);
  private final ReentrantReadWriteLock rwIdentifiersLock = new ReentrantReadWriteLock(true);

  private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
  private ScheduledFuture scheduledFeature;

  private String sName;
  private Set<String> seIdentifiers;
  private Set<Message> seSendMessages;
  private Set<Message> seReceivedMessages;
  private Set<Message> seOddMessages;

  /**
   * DataContainer constructor Initiates elements defined in this class
   */
  private DataContainer()
  {
    this.seIdentifiers = new HashSet<String>();
    this.seSendMessages = new HashSet<Message>();
    this.seReceivedMessages = new HashSet<Message>();
    this.seOddMessages = new HashSet<Message>();

    this.scheduledFeature = scheduledExecutorService.scheduleAtFixedRate(new Runnable()
    {
      @Override
      public void run()
      {
        garbageCleanup();
      }
    }, 30, 30, TimeUnit.SECONDS);
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
    if (this.seIdentifiers.size() > 500)
    {
      Iterator<String> it = this.seIdentifiers.iterator();
      int count = 1;
      this.rwIdentifiersLock.writeLock().lock();
      try
      {
        while (it.hasNext())
        {
          it.next();
          if (count > 500)
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.rwIdentifiersLock.writeLock().unlock();
      }
    }
  }

  /**
   * Cleans up the HashSet with send messages.
   */
  private void sendCleanup()
  {
    if (this.seSendMessages.size() > 500)
    {
      this.rwSendLock.writeLock().lock();
      try
      {
        Iterator<Message> it = this.seSendMessages.iterator();
        int count = 1;
        while (it.hasNext())
        {
          it.next();
          if (count > 500)
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.rwSendLock.writeLock().unlock();
      }
    }
  }

  /**
   * Cleans up the HashSet with received messages.
   */
  private void receiveCleanup()
  {
    if (this.seReceivedMessages.size() > 500)
    {
      this.rwReceivedLock.writeLock().lock();
      try
      {
        Iterator<Message> it = this.seReceivedMessages.iterator();
        int count = 1;
        while (it.hasNext())
        {
          it.next();
          if (count > 500)
          {
            it.remove();
          }
          count++;
        }
      }
      finally
      {
        this.rwReceivedLock.writeLock().unlock();
      }
    }
  }

  /**
   * Destroys the DataContainer object and all it's data.
   */
  public void destroy()
  {
    this.scheduledExecutorService.shutdown();

    this.seIdentifiers = new HashSet<String>();
    this.seSendMessages = new HashSet<Message>();
    this.seReceivedMessages = new HashSet<Message>();
    this.seOddMessages = new HashSet<Message>();

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
    this.seIdentifiers.add(id);
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
    this.rwIdentifiersLock.readLock().lock();
    try
    {
      if (this.seIdentifiers.contains(id))
      {
        return true;
      }
      return false;
    }
    finally
    {
      this.rwIdentifiersLock.readLock().unlock();
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
    this.rwIdentifiersLock.writeLock().lock();
    try
    {
      this.seIdentifiers.add(id);
    }
    finally
    {
      this.rwIdentifiersLock.writeLock().unlock();
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
    this.rwIdentifiersLock.writeLock().lock();
    this.rwIdentifiersLock.readLock().lock();
    try
    {
      if (this.seIdentifiers.contains(id))
      {
        this.seIdentifiers.remove(id);
      }
    }
    finally
    {
      this.rwIdentifiersLock.readLock().unlock();
      this.rwIdentifiersLock.writeLock().unlock();
    }
  }

  /**
   * Getter for HashSet with string bases identifiers.
   * 
   * @return an Set of identifiers.
   */
  public Set<String> getIdentifiers()
  {
    return this.seIdentifiers;
  }

  /**
   * Setter for Set with string bases identifiers.
   * 
   * @param identifiers
   *          an Set of string identifiers.
   */
  public void setIdentifiers(Set<String> identifiers)
  {
    this.seIdentifiers = identifiers;
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
    this.rwSendLock.writeLock().lock();
    try
    {
      this.seSendMessages.add(message);
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        this.addIdentifier(message.getMessageProperties().getCorrelationId().toString());
      }
    }
    finally
    {
      this.rwSendLock.writeLock().unlock();
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
    this.rwSendLock.writeLock().lock();
    try
    {
      this.seSendMessages.remove(message);
    }
    finally
    {
      this.rwSendLock.writeLock().unlock();
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
    this.rwSendLock.readLock().lock();
    try
    {
      for (Message message : this.seSendMessages)
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
      this.rwSendLock.readLock().unlock();
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
    this.seSendMessages = messages;
  }

  /**
   * Getter for the messages send Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getSendMessages()
  {
    return this.seSendMessages;
  }

  /**
   * Adds a given message to the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(Message message)
  {
    this.rwReceivedLock.writeLock().lock();
    try
    {
      // TODO : Make sure that messages which the same correlationId override each other
      this.seReceivedMessages.add(message);
    }
    finally
    {
      this.rwReceivedLock.writeLock().unlock();
    }
  }

  /**
   * Pops the oldest received message from the array
   * 
   * @return the oldest message from the array
   */
  public Message popReceivedMessage()
  {
    this.rwReceivedLock.readLock().lock();
    Message message = null;
    try
    {
      Iterator<Message> it = this.seReceivedMessages.iterator();
      while (it.hasNext())
      {
        message = (Message) it.next();
      }
    }
    finally
    {
      this.rwReceivedLock.readLock().unlock();
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
    this.rwReceivedLock.writeLock().lock();
    try
    {
      this.seReceivedMessages.remove(message);
    }
    finally
    {
      this.rwReceivedLock.writeLock().unlock();
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
    this.rwReceivedLock.readLock().lock();
    try
    {
      for (Message message : this.seReceivedMessages)
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
      this.rwReceivedLock.readLock().unlock();
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
    this.rwReceivedLock.writeLock().lock();
    try
    {
      this.seReceivedMessages = messages;
    }
    finally
    {
      this.rwReceivedLock.writeLock().unlock();
    }
  }

  /**
   * Getter for the messages receive Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getReceivedMessages()
  {
    this.rwReceivedLock.readLock().lock();
    try
    {
      return this.seReceivedMessages;
    }
    finally
    {
      this.rwReceivedLock.readLock().unlock();
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
    this.seOddMessages.add(message);
  }

  /**
   * Removes a given message object from the odd messages set.
   * 
   * @param message
   *          instance of the Message object
   */
  public void removeOddMessage(Message message)
  {
    this.seOddMessages.remove(message);
  }

  /**
   * Removes a message object by id from the odd messages set.
   * 
   * @param id
   *          the message identifier as string.
   */
  public void removeOddMessageById(String id)
  {
    for (Message message : this.seOddMessages)
    {
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        String messageId = (String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId());
        if (messageId.equals(id))
        {
          this.seOddMessages.remove(message);
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
    return this.seOddMessages;
  }

  /**
   * Setter for the odd messages set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setOddMessages(Set<Message> messages)
  {
    this.seOddMessages = messages;
  }
}
