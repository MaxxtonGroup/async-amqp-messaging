package com.maxxton.aam.messages;

/**
 * StatusMessage class Defines a message type which supports sending small status updates between clients.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class StatusMessage extends BaseMessage
{
  private static final long serialVersionUID = 9125113113678252464L;

  public StatusMessage()
  {
    this.objMessageType = MessageType.STATUS_MESSAGE;
    this.intPriority = 1;
    this.objPayload = null;
  }
}
