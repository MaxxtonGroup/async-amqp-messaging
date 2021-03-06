package com.maxxton.aam.communication;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.monitoring.Monitor;
import com.maxxton.aam.monitoring.MonitorFactory;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.MessageDetails;
import com.maxxton.aam.resources.MessageFactory;
import com.maxxton.aam.resources.Resources;
import com.maxxton.aam.resources.Validator;

/**
 * Messenger class. This is the main entry point for the library. It is connected to all other classes within the library. Has multiple methods to support the communication with AMQP brokers.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class Messenger
{
  private Monitor objMonitor = MonitorFactory.getMonitor("global");

  private Resources objResources;
  private CommunicationController objCommunication;

  /**
   * Constructor for the Messenger class.
   * 
   * @param messengerName
   *          name of the messenger client.
   */
  public Messenger(String messengerName)
  {
    this(messengerName, "/default.properties");
  }

  /**
   * Constructor for the Messenger class.
   * 
   * @param messengerName
   *          name of the messenger client.
   * @param configFile
   *          file to be loaded into configuration
   */
  public Messenger(String messengerName, String configFile)
  {
    Resources resources = new Resources();
    resources.getConfiguration().setName(messengerName);
    this.setResources(resources);
    this.loadConfiguration(configFile);

    MonitorFactory.start();
    CommunicationController controller = new CommunicationController(this.getResources());
    this.setCommunication(controller);

    this.objMonitor = MonitorFactory.getMonitor(this.objResources.getConfiguration().getName());
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
   * @return outcome of the send method. An valid UUID if the message was send correctly, null if it was not.
   */
  public String sendMessage(MessageType type, String receiver, Object payload)
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
   * @return outcome of the send method. An valid UUID if the message was send correctly, null if it was not.
   */
  public String sendMessage(MessageType messageType, String receiver, Object payload, String responseTo)
  {
    BaseMessage message = MessageFactory.createMessage(messageType);
    if (Validator.checkObject(message, BaseMessage.class))
    {
      message.setPayload(payload);
      message.setSender(this.objResources.getConfiguration().getName());
      message.setReceiver(receiver.toLowerCase());
      return this.objCommunication.packAndSend(receiver.toLowerCase(), message, responseTo);
    }
    else
    {
      objMonitor.warn(Messenger.class, "The messagetype you are sending is null. Be sure to fill all parameters correctly.");
    }
    return null;
  }

  /**
   * Checks if there are any message stored in the DataContainer. Returns BaseMessage on true and null on false.
   * 
   * @param millis
   *          timeout given in milliseconds.
   * @return an instance of the MessageDetails class.
   */
  public MessageDetails receiveMessage(int millis)
  {
    BaseMessage message = this.objCommunication.unpackAndReceive(millis);
    if (Validator.checkObject(message, BaseMessage.class))
    {
      MessageDetails details = new MessageDetails("", message.getSender(), message.getReceiver(), message.getMessageType(), message.getPayload());
      return details;
    }
    objMonitor.info(Messenger.class, "There is currently no message available.");
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
    if (Validator.checkObject(callback, Callback.class))
      this.objCommunication.getReceiver().setCallback(callback);
    else
      objMonitor.warn(Messenger.class, "Callback given in the setter for the receiver callback is invallid.");
  }

  /**
   * Load a custom configuration to override the default one.
   * 
   * @param configFile
   *          path to the configuration file.
   */
  private void loadConfiguration(String configFile)
  {
    MonitorFactory.loadConfiguration(configFile);
    this.objResources.getConfiguration().loadConfiguration(configFile);
  }

  /**
   * Sets the Resources class instance.
   * 
   * @param resources
   *          an instance of the Resources class.
   */
  private void setResources(Resources resources)
  {
    if (Validator.checkObject(resources, Resources.class))
      this.objResources = resources;
    else
      objMonitor.warn(Messenger.class, "Resource instance given in setter method is invallid.");
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
    if (Validator.checkObject(communication, CommunicationController.class))
      this.objCommunication = communication;
    else
      objMonitor.warn(Messenger.class, "CommunicationController instance given in setter method is invallid.");
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