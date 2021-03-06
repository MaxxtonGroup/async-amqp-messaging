package com.maxxton.aam.communication;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.resources.Resources;

/**
 * CommunicationController class Supports the (un)wrapping,(de/en)coding of send and received messages. Also adds the parameter values to the message body.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class CommunicationController
{

  private SendController objSender;
  private ReceiveController objReceiver;

  /**
   * Constructor for the CommunicationController class.
   * 
   * @param resources
   *          an instance of the Resources class.
   */
  public CommunicationController(Resources resources)
  {
    this.objSender = new SendController(resources);
    this.objReceiver = new ReceiveController(resources);
  }

  /**
   * Poll for a message from the ReceiveController. In case there is one, unpack it and return it. Returns null if no message is available.
   * 
   * @param millis
   *          timeout given in milliseconds.
   * @return an unpacked instance which inherits BaseMessage.
   */
  public BaseMessage unpackAndReceive(int millis)
  {
    Message message = this.objReceiver.receiveMessage(millis);
    if (message != null)
    {
      BaseMessage baseMessage = (BaseMessage) MessageSerializer.deserialize(message.getBody());
      return baseMessage;
    }
    return null;
  }

  /**
   * Convert a message to the appropriate Message class and send it.
   * 
   * @param receiver
   *          the receiver of the message.
   * @param baseMessage
   *          the message to be send.
   * @return outcome of the send method. An valid UUID if the message was send correctly, null if it was not.
   */
  public String packAndSend(String receiver, BaseMessage baseMessage)
  {
    return this.packAndSend(receiver, baseMessage, null);
  }

  /**
   * Convert a message to the appropriate Message class and send it using a response correlationId.
   * 
   * @param receiver
   *          the receiver of the message.
   * @param baseMessage
   *          the message to be send.
   * @param responseTo
   *          correlationId where the message is a response to.
   * @return outcome of the send method. An valid UUID if the message was send correctly, null if it was not.
   */
  public String packAndSend(String receiver, BaseMessage baseMessage, String responseTo)
  {
    MessageProperties properties = new MessageProperties();
    String uuid = responseTo == null || responseTo == "" ? UUID.randomUUID().toString() : responseTo;
    properties.setCorrelationId(uuid.getBytes());
    properties.setTimestamp(new Date());

    byte[] messageBytes = MessageSerializer.serialize(baseMessage);
    Message message = new Message(messageBytes, properties);
    if (objSender.sendMessage(receiver, message))
    {
      return uuid;
    }

    return null;
  }

  /**
   * Sets the SendController instance
   * 
   * @param sender
   *          instance of the SendController class
   */
  public void setSender(SendController sender)
  {
    this.objSender = sender;
  }

  /**
   * Gets the SendController instance
   * 
   * @return an instance of the SendController class
   */
  public SendController getSender()
  {
    return this.objSender;
  }

  /**
   * Sets the ReceiveController instance
   * 
   * @param receiver
   *          instance for the ReceiveController class
   */
  public void setReceiver(ReceiveController receiver)
  {
    this.objReceiver = receiver;
  }

  /**
   * Gets the ReceiveController instance.
   * 
   * @return an instance of the ReceiveController class.
   */
  public ReceiveController getReceiver()
  {
    return this.objReceiver;
  }
}
