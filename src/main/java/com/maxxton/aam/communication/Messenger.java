package com.maxxton.aam.communication;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.MessageDetails;
import com.maxxton.aam.resources.MessageFactory;
import com.maxxton.aam.resources.Resources;

/**
 * Messenger class. This is the main entry point for the library. It is connected to all other classes within the library. Has multiple methods to support the communication with AMQP brokers.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Messenger
{

  private Resources objResources;
  private CommunicationController objCommunication;
  private boolean bIsStarted;

  /**
   * Constructor for the Messenger class.
   * 
   * @param messengerName
   *          name of the messenger client.
   */
  public Messenger(String messengerName)
  {
    Resources resources = new Resources();
    resources.getConfiguration().setName(messengerName);
    // TODO : setup + initialize the resources.
    this.setResources(resources);

    this.bIsStarted = false;
  }

  /**
   * Send a specific message type with payload to a given receiver.
   * 
   * @param type
   *          enumeration type of the to be send message.
   * @param receiver
   *          the receiver of the message as string
   * @param payload
   *          the contents of the message (can be any object)
   * @return outcome of the send method. True for success and false for failed.
   */
  public boolean sendMessage(MessageType type, String receiver, Object payload)
  {
    return this.sendMessage(type, receiver, payload, null);
  }

  /**
   * Send a specific message type with payload to a given receiver referring to another message.
   * 
   * @param messageType
   *          enumeration type of the to be send message.
   * @param receiver
   *          the receiver of the message as string
   * @param payload
   *          the contents of the message (can be any object)
   * @param responseTo
   *          correlationId where the message is a response to.
   * @return outcome of the send method. True for success and false for failed.
   */
  public boolean sendMessage(MessageType messageType, String receiver, Object payload, String responseTo)
  {
    if (this.bIsStarted)
    {
      BaseMessage message = MessageFactory.createMessage(messageType);
      if (message != null)
      {
        message.setPayload(payload);
        message.setSender(this.objResources.getConfiguration().getName());
        message.setReceiver(receiver.toLowerCase());
        return this.objCommunication.packAndSend(receiver.toLowerCase(), message, responseTo);
      }
    }
    return false;
  }

  /**
   * Checks if there are any message stored in the DataContainer. Returns BaseMessage on true and null on false.
   * 
   * @param millis
   *          timeout given in milliseconds.
   * @return an instance of the BaseMessage class.
   */
  // TODO : create generic type to support passing the more relevant data.
  public MessageDetails receiveMessage(long millis)
  {
    if (this.bIsStarted)
    {
      BaseMessage message = this.objCommunication.unpackAndReceive(millis);
      if (message != null)
      {
        MessageDetails details = new MessageDetails("", message.getSender(), message.getReceiver(), message.getMessageType(), message.getPayload());
        return details;
      }
    }
    return null;
  }

  /**
   * Sets the callback to support asynchronous messaging.
   * 
   * @param callback
   *          the implemented callback object.
   */
  public void setReceiveCallback(Callback callback)
  {
    this.objCommunication.getReceiver().setCallback(callback);
  }

  /**
   * Load a custom configuration to override the default one.
   * 
   * @param configFile
   *          path to the configuration file.
   */
  public void loadConfiguration(String configFile)
  {
    this.objResources.getConfiguration().loadConfiguration(configFile);
  }

  /**
   * Start method to notify the Messenger to start communicating.
   */
  public void start()
  {
    CommunicationController controller = new CommunicationController(this.getResources());
    this.setCommunication(controller);
    this.bIsStarted = true;
  }

  /**
   * Sets the Resources class instance.
   * 
   * @param resources
   *          an instance of the Resources class.
   */
  private void setResources(Resources resources)
  {
    this.objResources = resources;
  }

  /**
   * Gets the Resources class instance.
   * 
   * @return an instance of the Resources class.
   */
  public Resources getResources()
  {
    return this.objResources;
  }

  /**
   * Closes the current connection. Sets the CommuncanicationController instance.
   * 
   * @param communication
   *          an instance of the CommunicationController.
   */
  private void setCommunication(CommunicationController communication)
  {
    this.objCommunication = communication;
  }

  /**
   * Gets the CommunicationController instance.
   * 
   * @return an instance of the CommunicationController class.
   */
  public CommunicationController getCommunication()
  {
    return this.objCommunication;
  }

}