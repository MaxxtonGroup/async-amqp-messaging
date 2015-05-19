package com.maxxton.aam.communication;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
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
  private static ConcurrentMap<String, DataContainer> mInstances = new ConcurrentHashMap<String, DataContainer>();

  private final ScheduledExecutorService objExecutor = Executors.newScheduledThreadPool(1);
  private ScheduledFuture<?> objScheduler;

  private String sName;
  private Resources objResources;
  private ConcurrentSkipListMap<Integer, String> mapIdentifiers;
  private ConcurrentSkipListMap<Integer, Message> mapSendMessages;
  private ConcurrentLinkedQueue<Message> clqReceivedMessages;
  private ConcurrentLinkedQueue<Message> clqOddMessages;

  /**
   * DataContainer constructor Initiates elements defined in this class
   */
  private DataContainer()
  {
    this.mapIdentifiers = new ConcurrentSkipListMap<Integer, String>();
    this.mapSendMessages = new ConcurrentSkipListMap<Integer, Message>();
    this.clqReceivedMessages = new ConcurrentLinkedQueue<Message>();
    this.clqOddMessages = new ConcurrentLinkedQueue<Message>();
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
    this.oddCleanup();
  }

  /**
   * Cleans up the SkipListMap with identifiers.
   */
  private void identifierCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.mapIdentifiers.size() > config.getDataMaxElements())
    {
      int diff = this.mapIdentifiers.size() - config.getDataMaxElements();
      for (int key = diff; key < this.mapIdentifiers.size(); key++)
      {
        this.mapIdentifiers.put(key - diff, this.mapIdentifiers.get(key));
        if (key >= config.getDataMaxElements())
        {
          this.mapIdentifiers.remove(key);
        }
      }
    }
  }

  /**
   * Cleans up the SkipListMap with send messages.
   */
  private void sendCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.mapSendMessages.size() > config.getDataMaxElements())
    {
      int diff = this.mapSendMessages.size() - config.getDataMaxElements();
      for (int key = diff; key < this.mapSendMessages.size(); key++)
      {
        this.mapSendMessages.put(key - diff, this.mapSendMessages.get(key));
        if (key >= config.getDataMaxElements())
        {
          this.mapSendMessages.remove(key);
        }
      }
    }
  }

  /**
   * Cleans up the LinkedQueue with received messages.
   */
  private void receiveCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.clqReceivedMessages.size() > config.getDataMaxElements())
    {
      int diff = this.clqReceivedMessages.size() - config.getDataMaxElements();
      for (int key = 0; key < diff; key++)
      {
        this.clqReceivedMessages.remove();
      }
    }
  }

  /**
   * Cleans up the LinkedQueue with odd messages.
   */
  private void oddCleanup()
  {
    Configuration config = this.objResources.getConfiguration();
    if (this.clqOddMessages.size() > config.getDataMaxElements())
    {
      int diff = this.clqOddMessages.size() - config.getDataMaxElements();
      for (int key = 0; key < diff; key++)
      {
        this.clqOddMessages.remove();
      }
    }
  }

  /**
   * Destroys the DataContainer object and all it's data.
   */
  public void destroy()
  {
    this.objExecutor.shutdown();

    this.mapIdentifiers = new ConcurrentSkipListMap<Integer, String>();
    this.mapSendMessages = new ConcurrentSkipListMap<Integer, Message>();
    this.clqReceivedMessages = new ConcurrentLinkedQueue<Message>();
    this.clqOddMessages = new ConcurrentLinkedQueue<Message>();

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
    this.addIdentifier(id);
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
    if (Validator.checkString(id))
    {
      if (this.mapIdentifiers.containsValue(id))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a given messageid to the Map.
   *
   * @param id
   *          identifier of the message
   */
  public void addIdentifier(String id)
  {
    if (Validator.checkString(id))
    {
      this.mapIdentifiers.put(this.mapIdentifiers.size(), id);
    }
  }

  /**
   * Removes a given messageId from the maps if it exists.
   * 
   * @param id
   *          identifier to be removed.
   */
  public void removeIdentifier(String id)
  {
    if (Validator.checkString(id))
    {
      if (this.mapIdentifiers.containsValue(id))
      {
        for (int key = 0; key > this.mapIdentifiers.size(); key++)
        {
          if (id == this.mapIdentifiers.get(key))
          {
            this.mapIdentifiers.remove(key);
          }
        }
      }
    }
  }

  /**
   * Removes a given key and value from the Map if it exists.
   * 
   * @param id
   *          identifier for the value in the map.
   */
  public void removeIdentifierById(Integer id)
  {
    if (Validator.checkInteger(id, 0, Integer.MAX_VALUE))
    {
      if (this.mapIdentifiers.containsKey(id))
      {
        this.mapIdentifiers.remove(id);
      }
    }
  }

  /**
   * Getter for SkipListMap with string bases identifiers.
   * 
   * @return an Map of identifiers.
   */
  public ConcurrentSkipListMap<Integer, String> getIdentifiers()
  {
    return this.mapIdentifiers;
  }

  /**
   * Setter for SkipListMap with string bases identifiers.
   * 
   * @param identifiers
   *          an Map of string identifiers.
   */
  public void setIdentifiers(ConcurrentSkipListMap<Integer, String> identifiers)
  {
    if (Validator.checkObject(identifiers, ConcurrentSkipListMap.class))
    {
      this.mapIdentifiers = identifiers;
    }
  }

  /**
   * Adds a given message to the send Map.
   * 
   * @param message
   *          the Message object.
   */
  public void addSendMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        this.mapSendMessages.put(this.mapSendMessages.size(), message);
        this.addIdentifier(new String(message.getMessageProperties().getCorrelationId()));
      }
    }
  }

  /**
   * Removes a given message from the send Map.
   * 
   * @param message
   *          the Message object.
   */
  public void removeSendMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      if (this.mapSendMessages.containsValue(message))
      {
        for (int key = 0; key < this.mapSendMessages.size(); key++)
        {
          if (message == this.mapSendMessages.get(key))
          {
            this.mapSendMessages.remove(key);
          }
        }
      }
    }
  }

  /**
   * Removes a given message from the send Map.
   *
   * @param id
   *          identifier of the message
   */
  public void removeSendMessageById(String id)
  {
    if (Validator.checkString(id))
    {
      for (int key = 0; key < this.mapSendMessages.size(); key++)
      {
        Message message = this.mapSendMessages.get(key);
        String messageId = new String(message.getMessageProperties().getCorrelationId());
        if (Validator.checkObject(messageId))
        {
          if (messageId == id)
          {
            this.mapSendMessages.remove(id);
          }
        }
      }
    }
  }

  /**
   * Setter for the messages send Map.
   * 
   * @param messages
   *          the Map with Message objects.
   */
  public void setSendMessages(ConcurrentSkipListMap<Integer, Message> messages)
  {
    if (Validator.checkObject(messages, ConcurrentSkipListMap.class))
    {
      this.mapSendMessages = messages;
    }
  }

  /**
   * Getter for the messages send Map.
   * 
   * @return Map with messages.
   */
  public ConcurrentSkipListMap<Integer, Message> getSendMessages()
  {
    return this.mapSendMessages;
  }

  /**
   * Adds a given message to the receive LinkedQueue.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      this.clqReceivedMessages.add(message);
    }
  }

  /**
   * Pops the oldest received message from the LinkedQueue
   * 
   * @return the oldest message from the LinkedQueue
   */
  public Message popReceivedMessage()
  {
    return this.clqReceivedMessages.poll();
  }

  /**
   * Removes a given message from the Received queue.
   * 
   * @param message
   *          the message to be removed.
   */
  public void removeReceivedMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      if (this.clqReceivedMessages.contains(message))
      {
        this.clqReceivedMessages.remove(message);
      }
    }
  }

  /**
   * Setter for the messages receive LinkedQueue.
   * 
   * @param messages
   *          the LinkedQueue with Message objects.
   */
  public void setReceivedMessages(ConcurrentLinkedQueue<Message> messages)
  {
    if (Validator.checkObject(messages, ConcurrentLinkedQueue.class))
    {
      this.clqReceivedMessages = messages;
    }
  }

  /**
   * Getter for the messages receive LinkedQueue.
   * 
   * @return the LinkedQueue with received messages.
   */
  public ConcurrentLinkedQueue<Message> getReceivedMessages()
  {
    return this.clqReceivedMessages;
  }

  // ODD and DISCARDED MESSAGES.

  /**
   * Adds a message instance to the odd messages LinkedQueue.
   * 
   * @param message
   *          an instance of a message object.
   */
  public void addOddMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      this.clqOddMessages.add(message);
    }
  }

  /**
   * Pops and returns a message at the head of the OddMessages queue.
   * 
   * @return An instance of the Message class or null if the queue was empty.
   */
  public Message popOddMessage()
  {
    return this.clqOddMessages.poll();
  }

  /**
   * Removes a certain message from the received queue.
   * 
   * @param message
   *          the message to be removed.
   */
  public void removeOddMessage(Message message)
  {
    if (Validator.checkObject(message, Message.class))
    {
      if (this.clqOddMessages.contains(message))
      {
        this.clqOddMessages.remove(message);
      }
    }
  }

  /**
   * Getter for the odd messages LinkedQueue.
   * 
   * @return a LinkedQueue of odd received messages.
   */
  public ConcurrentLinkedQueue<Message> getOddMessages()
  {
    return this.clqOddMessages;
  }

  /**
   * Setter for the odd messages LinkedQueue.
   * 
   * @param messages
   *          the LinkedQueue with Message objects.
   */
  public void setOddMessages(ConcurrentLinkedQueue<Message> messages)
  {
    if (Validator.checkObject(messages, ConcurrentLinkedQueue.class))
    {
      this.clqOddMessages = messages;
    }
  }
}
