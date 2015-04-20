package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
  
  private final ReentrantReadWriteLock rwReceivedLock = new ReentrantReadWriteLock(true);

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
   * Destroys the DataContainer object and all it's data.
   */
  public void destroy()
  {
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
    if (this.seIdentifiers.contains(id))
    {
      return true;
    }
    return false;
  }

  /**
   * Removes a given messagid from the list if it exists.
   * 
   * @param id
   *          identifier of a message
   */
  public void removeId(String id)
  {
    if (this.seIdentifiers.contains(id))
    {
      this.seIdentifiers.remove(id);
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
    // TODO : Fix overflowing send messages Set!
    this.seSendMessages.add(message);
    if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
    {
      this.seIdentifiers.add(message.getMessageProperties().getCorrelationId().toString());
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
    this.seSendMessages.remove(message);
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param id
   *          identifier of the message
   */
  public void removeSendMessageById(String id)
  {
    for (Message message : this.seSendMessages)
    {
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        String messageId = message.getMessageProperties().getCorrelationId().toString();
        if (messageId.equals(id))
        {
          this.seSendMessages.remove(message);
        }
      }
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
