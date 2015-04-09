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

  protected String strMessageId;

  protected MessageType mtMessageType;

  /**
   * Sets the identifier of this message
   * 
   * @param messageId
   */
  public void setMessageId(String messageId)
  {
    this.strMessageId = messageId;
  }

  /**
   * Gets the identifier of this message
   * 
   * @return message identifier as string
   */
  public String getMessageId()
  {
    return this.strMessageId;
  }

  /**
   * Generates a hashcode from two class related variables.
   * 
   * @return hashcode result of the two variables as integer
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mtMessageType == null) ? 0 : mtMessageType.hashCode());
    result = prime * result + ((strMessageId == null) ? 0 : strMessageId.hashCode());
    return result;
  }

  /**
   * Method used by a set to check for duplicates.
   * 
   * @param obj
   *          object to be compared against the current one
   * @return true if the object matches the criteria, false if it doesn't
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof BaseMessage))
      return false;
    BaseMessage other = (BaseMessage) obj;
    if (mtMessageType != other.mtMessageType)
      return false;
    if (strMessageId == null)
    {
      if (other.strMessageId != null)
        return false;
    }
    else if (!strMessageId.equals(other.strMessageId))
      return false;
    return true;
  }

}
