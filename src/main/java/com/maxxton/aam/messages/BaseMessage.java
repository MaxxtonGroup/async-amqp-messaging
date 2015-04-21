package com.maxxton.aam.messages;

import java.io.Serializable;

/**
 * Message interface Skeleton class for the different types of messages. Includes the mandatory fields to be used by it's extender.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */

public abstract class BaseMessage implements Serializable
{
  /**
   * Serial version for UID
   */
  private static final long serialVersionUID = -8763348067503641944L;

  protected Object objPayload;

  protected MessageType objMessageType;

  protected int intPriority;

  /**
   * Sets the payload object of the message.
   * 
   * @param payload
   *          the payload object.
   */
  public void setPayload(Object payload)
  {
    this.objPayload = payload;
  }

  /**
   * Gets the payload object.
   * 
   * @return an instance of the payload object.
   */
  public Object getPayload()
  {
    return this.objPayload;
  }

  /**
   * Gets the type of the message.
   *
   * @return enumeration of the MessageType
   */
  public MessageType getMessageType()
  {
    return this.objMessageType;
  }

  /**
   * Sets the type of the message.
   *
   * @param messageType
   *          enumeration of the MessageType.
   */
  public void setMessageType(MessageType messageType)
  {
    this.objMessageType = messageType;
  }

  /**
   * Gets the message priority.
   *
   * @return the priority of the message.
   */
  public int getPriority()
  {
    return this.intPriority;
  }

  /**
   * Sets the message priority.
   *
   * @param priority
   *          integer ranging from 0 till 9.
   */
  public void setPriority(int priority)
  {
    this.intPriority = priority;
    if (this.intPriority > 9)
    {
      this.intPriority = 9;
    }
    if (this.intPriority < 0)
    {
      this.intPriority = 0;
    }
  }

  // /**
  // * Generates a hashcode from two class related variables.
  // *
  // * @return hashcode result of the two variables as integer
  // */
  // @Override
  // public int hashCode()
  // {
  // final int prime = 31;
  // int result = 1;
  // result = prime * result + ((mtMessageType == null) ? 0 : mtMessageType.hashCode());
  // result = prime * result + ((strMessageId == null) ? 0 : strMessageId.hashCode());
  // return result;
  // }
  //
  // /**
  // * Method used by a set to check for duplicates.
  // *
  // * @param obj
  // * object to be compared against the current one
  // * @return true if the object matches the criteria, false if it doesn't
  // */
  // @Override
  // public boolean equals(Object obj)
  // {
  // if (this == obj)
  // return true;
  // if (obj == null)
  // return false;
  // if (!(obj instanceof BaseMessage))
  // return false;
  // BaseMessage other = (BaseMessage) obj;
  // if (mtMessageType != other.mtMessageType)
  // return false;
  // if (strMessageId == null)
  // {
  // if (other.strMessageId != null)
  // return false;
  // }
  // else if (!strMessageId.equals(other.strMessageId))
  // return false;
  // return true;
  // }

}
