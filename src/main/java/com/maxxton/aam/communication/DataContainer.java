package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

  private Set<String> seIdentifiers;
  private Set<Message> seSendMessages;
  private Set<Message> seReceivedMessages;

  /**
   * DataContainer constructor Initiates elements defined in this class
   */
  private DataContainer()
  {
    this.seIdentifiers = new HashSet<String>();
    this.seSendMessages = new HashSet<Message>();
    this.seReceivedMessages = new HashSet<Message>();
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
          mInstances.put(key, container);
        }
      }
    }
    return container;
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
    this.seSendMessages.add(message);
    if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
    {
      this.seIdentifiers.add((String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId()));
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
        String messageId = (String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId());
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
    this.seReceivedMessages = messages;
  }

  /**
   * Getter for the messages send Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getSendMessages()
  {
    return this.seReceivedMessages;
  }

  /**
   * Adds a given message to the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(Message message)
  {
    // TODO : Make sure that messages which the same correlationId override each other
    this.seReceivedMessages.add(message);
  }

  /**
   * Pops the oldest received message from the array
   * 
   * @return the oldest message from the array
   */
  public Message popReceivedMessage()
  {
    Iterator<Message> it = this.seReceivedMessages.iterator();
    Message message = null;
    while (it.hasNext())
    {
      message = (Message) it.next();
    }
    this.seReceivedMessages.remove(message);
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
    this.seReceivedMessages.remove(message);
  }

  /**
   * Removes a given message from the received Set.
   * 
   * @param id
   *          identifier of the message
   */
  public void removeReceivedMessageById(String id)
  {
    for (Message message : this.seReceivedMessages)
    {
      if (message.getMessageProperties().getCorrelationId() != null && message.getMessageProperties().getCorrelationId().length > 0)
      {
        String messageId = (String) MessageSerializer.deserialize(message.getMessageProperties().getCorrelationId());
        if (messageId.equals(id))
        {
          this.seSendMessages.remove(message);
        }
      }
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
    this.seReceivedMessages = messages;
  }

  /**
   * Getter for the messages receive Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getReceivedMessages()
  {
    return this.seReceivedMessages;
  }
}
