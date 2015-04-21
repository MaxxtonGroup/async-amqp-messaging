package com.maxxton.aam.messages;

/**
 * StatusMessage class Defines a message type which supports sending a response after a request/task is done.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class ResponseMessage extends BaseMessage
{
  private static final long serialVersionUID = -7444536989688341484L;

  public ResponseMessage()
  {
    this.objMessageType = MessageType.RESPONSE_MESSAGE;
    this.intPriority = 1;
    this.objPayload = null;
  }
}
