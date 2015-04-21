package com.maxxton.aam.messages;

/**
 * InformationMessage class Defines a message type which supports fast travel of information only.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class InformationMessage extends BaseMessage
{
  private static final long serialVersionUID = 3610404050645830637L;
  
  public InformationMessage()
  {
    this.objMessageType = MessageType.INFORMATION_MESSAGE;
    this.intPriority = 3;
    this.objPayload = null;
  }

}
