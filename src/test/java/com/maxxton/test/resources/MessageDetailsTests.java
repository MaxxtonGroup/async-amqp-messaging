package com.maxxton.test.resources;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.resources.MessageDetails;

/**
 * Runs a list of tests against the MessageDetails class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageDetailsTests
{

  @Before
  public void setup()
  {

  }

  /**
   * Test the getter and setter for the ResponseId.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testResponseId() throws Exception
  {
    System.out.print("MessageDetails : Testing the Response Id getter and setter...");

    String strResponseId = "123456789";
    MessageDetails objDetails = new MessageDetails(strResponseId, null, null, null, null);
    assertNotNull("The MessageDetails instance cannot be null", objDetails);

    String strSameResponseId = objDetails.getResponseId();
    assertEquals("The response id's should be the same.", strResponseId, strSameResponseId);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the Sender.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSender() throws Exception
  {
    System.out.print("MessageDetails : Testing the Sender getter and setter...");

    String strSender = "testSender";
    MessageDetails objDetails = new MessageDetails(null, strSender, null, null, null);
    assertNotNull("The MessageDetails instance cannot be null", objDetails);

    String strSameSender = objDetails.getSender();
    assertEquals("The senders should be the same.", strSender, strSameSender);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the Receiver.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiver() throws Exception
  {
    System.out.print("MessageDetails : Testing the Receiver getter and setter...");

    String strReceiver = "testReceiver";
    MessageDetails objDetails = new MessageDetails(null, null, strReceiver, null, null);
    assertNotNull("The MessageDetails instance cannot be null", objDetails);

    String strSameReceiver = objDetails.getReceiver();
    assertEquals("The receivers should be the same.", strReceiver, strSameReceiver);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the MessageType.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testMessageType() throws Exception
  {
    System.out.print("MessageDetails : Testing the MessageType getter and setter...");

    MessageType objType = MessageType.GENERATION_MESSAGE;
    MessageDetails objDetails = new MessageDetails(null, null, null, objType, null);
    assertNotNull("The MessageDetails instance cannot be null", objDetails);

    MessageType strSameType = objDetails.getMessageType();
    assertEquals("The message types should be the same.", objType, strSameType);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the Payload.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testPayload() throws Exception
  {
    System.out.print("MessageDetails : Testing the Payload getter and setter...");

    Object objMessage = "Hello World";
    MessageDetails objDetails = new MessageDetails(null, null, null, null, objMessage);
    assertNotNull("The MessageDetails instance cannot be null", objDetails);

    Object strSameMessage = objDetails.getPayload();
    assertEquals("The payload should be the same.", objMessage, strSameMessage);

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {

  }

}
