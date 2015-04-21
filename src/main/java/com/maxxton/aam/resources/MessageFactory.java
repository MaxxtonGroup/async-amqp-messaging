package com.maxxton.aam.resources;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerationMessage;
import com.maxxton.aam.messages.InformationMessage;
import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.messages.ResponseMessage;
import com.maxxton.aam.messages.StatusMessage;
import com.maxxton.aam.messages.SynchronizionMessage;

/**
 * MessageFactory class Creates new instances of a certain message type. Implements the Factory pattern as this suits the class better.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessageFactory
{

  /**
   * Creates a new Message instance based on the given MessageType.
   *
   * @param messageType
   *          message type enumeration.
   * @return instance of the requested message type.
   */
  public static BaseMessage createMessage(MessageType messageType)
  {
    switch (messageType)
    {
      case GENERATION_MESSAGE:
        return new GenerationMessage();
      case INFORMATION_MESSAGE:
        return new InformationMessage();
      case SYNCHRONIZION_MESSAGE:
        return new SynchronizionMessage();
      case RESPONSE_MESSAGE:
        return new ResponseMessage();
      case STATUS_MESSAGE:
        return new StatusMessage();
      default:
        return null;
    }
  }
}
