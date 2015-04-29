package com.maxxton.test.messages;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.messages.MessageType;

import static org.junit.Assert.*;

/**
 * Runs a list of tests against the MessageType class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageTypeTests
{

  /**
   * Setup method run before each test.
   */
  @Before
  public void setup()
  {

  }

  /**
   * Test the MessageType enumeration.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testMessageType() throws Exception
  {
    System.out.print("MessageType : Testing the message type enumeration...");

    MessageType generationType = MessageType.GENERATION_MESSAGE;
    assertEquals("The message should be of type Generation.", generationType, MessageType.GENERATION_MESSAGE);

    MessageType informationType = MessageType.INFORMATION_MESSAGE;
    assertEquals("The message should be of type Information.", informationType, MessageType.INFORMATION_MESSAGE);

    MessageType responseType = MessageType.RESPONSE_MESSAGE;
    assertEquals("The message should be of type Response.", responseType, MessageType.RESPONSE_MESSAGE);

    MessageType statusType = MessageType.STATUS_MESSAGE;
    assertEquals("The message should be of type Status.", statusType, MessageType.STATUS_MESSAGE);

    MessageType synchronizationType = MessageType.SYNCHRONIZION_MESSAGE;
    assertEquals("The message should be of type Synchronization.", synchronizationType, MessageType.SYNCHRONIZION_MESSAGE);

    System.out.println("done.");
  }

  /**
   * Cleanup method run after each test.
   */
  @After
  public void cleanup()
  {

  }
}
