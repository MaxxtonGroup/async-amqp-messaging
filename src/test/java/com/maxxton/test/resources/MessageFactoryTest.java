package com.maxxton.test.resources;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerationMessage;
import com.maxxton.aam.messages.InformationMessage;
import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.messages.ResponseMessage;
import com.maxxton.aam.messages.StatusMessage;
import com.maxxton.aam.messages.SynchronizionMessage;
import com.maxxton.aam.resources.MessageFactory;

import static org.junit.Assert.*;

/**
 * Run a list of test against the MessageFactory class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageFactoryTest
{

  @Before
  public void setup()
  {

  }

  /**
   * Test the creational pattern of the MessageFactory class.
   * 
   * @throws Exception
   *           reason of failure given by the test
   */
  @Test
  public void testCreationalPattern() throws Exception
  {
    System.out.print("MessageFactory : Testing the creational pattern...");

    BaseMessage objMessage = MessageFactory.createMessage(MessageType.GENERATION_MESSAGE);
    assertTrue("The created message should be an instance of the GenerationMessage class", objMessage instanceof GenerationMessage);
    objMessage = MessageFactory.createMessage(MessageType.INFORMATION_MESSAGE);
    assertTrue("The created message should be an instance of the InformationMessage class", objMessage instanceof InformationMessage);
    objMessage = MessageFactory.createMessage(MessageType.SYNCHRONIZION_MESSAGE);
    assertTrue("The created message should be an instance of the SynchronizionMessage class", objMessage instanceof SynchronizionMessage);
    objMessage = MessageFactory.createMessage(MessageType.RESPONSE_MESSAGE);
    assertTrue("The created message should be an instance of the ResponseMessage class", objMessage instanceof ResponseMessage);
    objMessage = MessageFactory.createMessage(MessageType.STATUS_MESSAGE);
    assertTrue("The created message should be an instance of the StatusMessage class", objMessage instanceof StatusMessage);

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {

  }
}
