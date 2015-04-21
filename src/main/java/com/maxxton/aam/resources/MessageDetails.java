package com.maxxton.aam.resources;

import com.maxxton.aam.messages.MessageType;

/**
 * Data holding class based on the Tuple class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessageDetails
{
  private final String strResponseId;
  private final MessageType objMessageType;
  private final Object objPayload;

  /**
   * Constructor for the MessageDetials class.
   * 
   * @param responseId
   *          identifier created by the sender of the message.
   * @param payload
   *          object which was in the payload of the message.
   */
  public MessageDetails(String responseId, MessageType messageType, Object payload)
  {
    this.strResponseId = responseId;
    this.objMessageType = messageType;
    this.objPayload = payload;
  }

  /**
   * Gets the response identifier.
   *
   * @return the identifier of the message as string.
   */
  public String getResponseId()
  {
    return this.strResponseId;
  }

  /**
   * Gets the message type.
   *
   * @return the MessageType of this message.
   */
  public MessageType getMessageType()
  {
    return this.objMessageType;
  }

  /**
   * Gets the payload object.
   *
   * @return the object located in the payload of the message.
   */
  public Object getPayload()
  {
    return this.objPayload;
  }
}
