package com.maxxton.aam.messages;

/**
 * GenerateMessage class Defines a message type which supports sending Generation tasks.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class GenerationMessage extends BaseMessage
{
  private static final long serialVersionUID = -1667893027787920636L;

  public GenerationMessage()
  {
    this.objMessageType = MessageType.GENERATION_MESSAGE;
    this.intPriority = 2;
    this.objPayload = null;
  }

}
