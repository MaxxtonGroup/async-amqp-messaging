package com.maxxton.aam.communication;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.maxxton.aam.messages.Message;

/**
 * DataContainer class Contains all data which passes the Send- and/or ReceiveController.
 * This may include id's and messages.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class DataContainer
{
  private Set<String> ssIdentifiers;
  private Set<Message> smSendMessages;
  private Set<Message> smReceivedMessages;

  /**
   * DataContainer constructor Initiates elements defined in this class
   */
  public DataContainer()
  {
    this.ssIdentifiers = new HashSet<String>();
    this.smSendMessages = new HashSet<Message>();
    this.smReceivedMessages = new HashSet<Message>();
  }

  /**
   * Gets an unique identifier
   * 
   * @return an unique id given as string
   */
  public String getUniqueId()
  {
    String id = UUID.randomUUID().toString();
    this.ssIdentifiers.add(id);
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
    if (this.ssIdentifiers.contains(id))
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
    if (this.ssIdentifiers.contains(id))
    {
      this.ssIdentifiers.remove(id);
    }
  }

  /**
   * Getter for HashSet with string bases identifiers.
   * 
   * @return an Set of identifiers.
   */
  public Set<String> getIdentifiers()
  {
    return this.ssIdentifiers;
  }

  /**
   * Setter for Set with string bases identifiers.
   * 
   * @param identifiers
   *          an Set of string identifiers.
   */
  public void setIdentifiers(Set<String> identifiers)
  {
    this.ssIdentifiers = identifiers;
  }

  /**
   * Adds a given message to the send Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addSendMessage(Message message)
  {
    this.smSendMessages.add(message);
    this.ssIdentifiers.add(message.getMessageId());
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeSendMessage(Message message)
  {
    this.smSendMessages.remove(message);
  }

  /**
   * Removes a given message from the send Set.
   * 
   * @param id
   *          identifier of the message
   */
  public void removeSendMessageById(String id)
  {
    for (Message message : this.smSendMessages)
    {
      if (message.getMessageId().equals(id))
      {
        this.smSendMessages.remove(message);
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
    this.smReceivedMessages = messages;
  }

  /**
   * Getter for the messages send Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getSendMessages()
  {
    return this.smReceivedMessages;
  }

  /**
   * Adds a given message to the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void addReceivedMessage(Message message)
  {
    this.smReceivedMessages.add(message);
  }

  /**
   * Removes a given message from the receive Set.
   * 
   * @param message
   *          the Message object.
   */
  public void removeReceivedMessage(Message message)
  {
    this.smReceivedMessages.remove(message);
    if (this.isOwnedByMe(message.getMessageId()))
    {
      this.ssIdentifiers.remove(message.getMessageId());
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
    for (Message message : this.smReceivedMessages)
    {
      if (message.getMessageId().equals(id))
      {
        this.smSendMessages.remove(message);
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
    this.smReceivedMessages = messages;
  }

  /**
   * Getter for the messages receive Set.
   * 
   * @return Set with messages.
   */
  public Set<Message> getReceivedMessages()
  {
    return this.smReceivedMessages;
  }
}
