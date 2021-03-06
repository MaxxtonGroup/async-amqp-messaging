package com.maxxton.aam.resources;

/**
 * Callback interface Can be set by the user to handle received message asynchronous. Uses Lambda expressions for the implementation of the code.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public interface Callback
{

  /**
   * Method which can be implemented by overriding (using implements on custom callback class) or using Lambda expressions (see
   * http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html).
   *
   * @param message
   *          an instance of the Message class.
   */
  public void handleMessage(MessageDetails message);

}
