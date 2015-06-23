package com.maxxton.test.messages;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerationMessage;
import com.maxxton.aam.messages.MessageType;

import static org.junit.Assert.*;

/**
 * Run a list of tests against the BaseMessage class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseMessageTest
{
  private BaseMessage objMessage;

  /**
   * Setup method to init elements before the test.
   */
  @Before
  public void setup()
  {
    if (this.objMessage == null)
    {
      this.objMessage = new GenerationMessage();
      assertNotNull("The basemessage cannot be Null.", this.objMessage);
    }
  }

  /**
   * Test the getter and setter of the payload variable.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testPayload() throws Exception
  {
    System.out.print("BaseMessage : Testing getter and setter for message payload...");

    Object objPayload = this.objMessage.getPayload();
    assertNull("The payload should not have been set.", objPayload);

    Object otherPayload = "Hello World.";
    this.objMessage.setPayload(otherPayload);
    Object samePayload = this.objMessage.getPayload();

    assertEquals("The payloads should be the same.", otherPayload, samePayload);
    assertNotEquals("The payloads should be different.", objPayload, samePayload);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter of the payload variable.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testMessageType() throws Exception
  {
    System.out.print("BaseMessage : Testing the getter and setter for the message type...");

    MessageType enmType = this.objMessage.getMessageType();
    assertEquals("The message should be of type GENERATION_MESSAGE.", enmType, MessageType.GENERATION_MESSAGE);

    MessageType enmOthertype = MessageType.INFORMATION_MESSAGE;
    this.objMessage.setMessageType(enmOthertype);
    MessageType enmSameType = this.objMessage.getMessageType();

    assertEquals("The messagetypes should be the same.", enmOthertype, enmSameType);
    assertNotEquals("The messagetypes should be different.", enmType, enmSameType);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter of the payload variable.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testPriority() throws Exception
  {
    System.out.print("BaseMessage : Testing getter and setter for the message priority...");

    int iPriority = this.objMessage.getPriority();
    assertEquals("The priority should be the number 2.", iPriority, 2);

    int iOtherPriority = 3;
    this.objMessage.setPriority(iOtherPriority);
    int iSamePriority = this.objMessage.getPriority();

    assertEquals("The priority should be the same.", iOtherPriority, iSamePriority);
    assertNotEquals("The priority should be different.", iSamePriority, iPriority);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter of the message sender.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSender() throws Exception
  {
    System.out.print("BaseMessage : Testing getter and setter for the message sender...");

    String strSender = this.objMessage.getSender();
    assertEquals("The sender should not be set.", strSender, "");

    String strOtherSender = "test";
    this.objMessage.setSender(strOtherSender);
    String strSameSender = this.objMessage.getSender();

    assertEquals("The senders should be the same.", strOtherSender, strSameSender);
    assertNotEquals("The senders should be different.", strSender, strSameSender);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter of the message receiver.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiver() throws Exception
  {
    System.out.print("BaseMessage : Testing getter and setter for the message receiver...");

    String strReceiver = this.objMessage.getReceiver();
    assertEquals("The receiver should not be set.", strReceiver, "");

    String strOtherReceiver = "test";
    this.objMessage.setReceiver(strOtherReceiver);
    String strSameReceiver = this.objMessage.getReceiver();

    assertEquals("The receivers should be the same.", strOtherReceiver, strSameReceiver);
    assertNotEquals("The receivers shoudl be different.", strReceiver, strSameReceiver);

    System.out.println("done.");
  }

  /**
   * Cleanup method called after running the test.
   */
  @After
  public void cleanup()
  {

  }
}
