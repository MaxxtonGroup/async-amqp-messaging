package com.maxxton.aam.resources;

import com.maxxton.aam.messages.BaseMessage;

/**
 * Callback interface Can be set by the user to handle received message asynchronous. Uses Lambda expressions for the implementation of the code.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public interface Callback
{

  /**
   * Method which can be implemented by overriding or using Lambda expressions.
   *
   * @param message
   *          an instance of the Message class.
   */
  public void handleMessage(BaseMessage message);

}
