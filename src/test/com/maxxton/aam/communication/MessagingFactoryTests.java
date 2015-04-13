package com.maxxton.aam.communication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessagingFactoryTests
{

  @Before
  public void setup()
  {
    // TODO : Load custom configuration file.
  }

  @Test
  public void testSingleton() throws Exception
  {
    System.out.print("MessagingFactory : Testing singleton pattern...");

    MessagingFactory msgFactory = MessagingFactory.getInstance();
    assertNotNull("The messagingfactory cannot be NULL.", msgFactory);

    MessagingFactory otherFactory = MessagingFactory.getInstance();
    assertNotNull("The messagingfactory cannot be NULL.", otherFactory);

    assertEquals("The instances returned by the singleton are not the same.", msgFactory, otherFactory);

    System.out.println("done.");
  }

  @Test
  public void testCreationalPattern() throws Exception
  {
    System.out.print("MessagingFactory : Testing creational pattern...");

    MessagingFactory msgFactory = MessagingFactory.getInstance();
    assertNotNull("The messagingfactory cannot be NULL.", msgFactory);

    Messenger msgr = msgFactory.createMessenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    Messenger otherMsgr = msgFactory.createMessenger("other");
    assertNotNull("The other messenger cannot be NULL.", otherMsgr);

    assertNotEquals("The instances returned by the creational method are the same.", msgr, otherMsgr);

    Messenger sameMsgr = msgFactory.createMessenger("test");
    assertNotNull("The same messenger cannot be NULL.", sameMsgr);

    assertEquals("The instances returned by the creational method are not the same.", msgr, sameMsgr);

    msgr.closeConnection();
    otherMsgr.closeConnection();
    sameMsgr.closeConnection();

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {
    // TODO : Cleanup queues and close connection.
  }

}
