package com.maxxton.aam.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.maxxton.aam.resources.Resources;

import static org.junit.Assert.*;

public class MessengerTests
{

  @Before
  public void setup()
  {
    // TODO : load custom configuration file
  }

  @Test
  public void testConstructor() throws Exception
  {
    System.out.print("Messenger : Testing constructor...");

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    System.out.println("done.");
  }

  @Test
  public void testResources() throws Exception
  {
    System.out.print("Messenger : Testing resources setter and getter...");

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    Resources resources = new Resources();
    msgr.setResources(resources);
    Resources otherResources = msgr.getResources();

    assertEquals("The Resources instances are not the same.", resources, otherResources);

    msgr.closeConnection();

    System.out.println("done.");
  }

  @Test
  public void testCommunicationController() throws Exception
  {
    System.out.print("Messenger : Testing communicationcontroller...");

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    CommunicationController communication = new CommunicationController(msgr.getResources());
    msgr.setCommunication(communication);
    CommunicationController otherCommunication = msgr.getController();

    assertEquals("The CommunicationController instances are not the same.", communication, otherCommunication);

    msgr.closeConnection();

    System.out.println("done.");
  }

  @Test
  public void testSendAndReceive() throws Exception
  {
    System.out.print("Messenger : Testing sending and receiving...");

    Messenger sender = new Messenger("test");

    // TODO : change test to match MessageType option (currently null).
    sender.sendMessage(null, "other", "Hello World");

    sender.closeConnection();

    Messenger receiver = new Messenger("other");

    String msg = (String) receiver.receiveMessage();

    assertNull("No message has been received.", msg);

    receiver.closeConnection();

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {
    // TODO : cleanup queues and close connections
  }

}
