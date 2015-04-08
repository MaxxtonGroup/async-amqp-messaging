package com.maxxton.aam.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.maxxton.aam.messages.BaseMessage;

/**
 * DataContainer class Contains all data which passes the Send- and/or ReceiveController. 
 * This may include id's and messages.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class DataContainer
{
  private static Map<String, DataContainer> _mInstances = new HashMap<String, DataContainer>();

  private Set<String> seIdentifiers;
  private Set<BaseMessage> seSendMessages;
  private Set<BaseMessage> seReceivedMessages;

  /**
   * DataContainer constructor 
   * Initiates elements defined in this class
   */
  private DataContainer()
  {
    this.seIdentifiers = new HashSet<String>();
    this.seSendMessages = new HashSet<BaseMessage>();
    this.seReceivedMessages = new HashSet<BaseMessage>();
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
    DataContainer container = _mInstances.get(key);
    if (container == null)
    {
      synchronized (_mInstances)
      {
        container = _mInstances.get(key);

        if (container == null)
        {
          container = new DataContainer();
          _mInstances.put(key, container);
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
  public void addSendMessage(BaseMessage message)
  {
    this.seSendMessages.add(message);
    this.seIdentifiers.add(message.getMessageId());
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeSendMessage(BaseMessage message)
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
    for (BaseMessage message : this.seSendMessages)
    {
      if (message.getMessageId().equals(id))
      {
        this.seSendMessages.remove(message);
      }
    }
  }

  /**
   * Setter for the messages send Set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setSendMessages(Set<BaseMessage> messages)
  {
    this.seReceivedMessages = messages;
  }

  /**
   * Getter for the messages send Set.
   * 
   * @return Set with messages.
   */
  public Set<BaseMessage> getSendMessages()
  {
    return this.seReceivedMessages;
  }

  /**
   * Adds a given message to the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(BaseMessage message)
  {
    this.seReceivedMessages.add(message);
  }

  /**
   * Removes a given message from the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeReceivedMessage(BaseMessage message)
  {
    this.seReceivedMessages.remove(message);
    if (this.isOwnedByMe(message.getMessageId()))
    {
      this.seIdentifiers.remove(message.getMessageId());
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
    for (BaseMessage message : this.seReceivedMessages)
    {
      if (message.getMessageId().equals(id))
      {
        this.seSendMessages.remove(message);
      }
    }
  }

  /**
   * Setter for the messages receive Set.
   * 
   * @param messages
   *          the Set with Message objects.
   */
  public void setReceivedMessages(Set<BaseMessage> messages)
  {
    this.seReceivedMessages = messages;
  }

  /**
   * Getter for the messages receive Set.
   * 
   * @return Set with messages.
   */
  public Set<BaseMessage> getReceivedMessages()
  {
    return this.seReceivedMessages;
  }
}
