package com.maxxton.aam.communication;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Run a list of tests against the MessagingFactory class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessagingFactoryTests
{

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @BeforeClass
  public static void setup()
  {
    // TODO : Load custom configuration file.
  }

  /**
   * Test the functionality of the Singleton method.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
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

  /**
   * Test the functionality of the creational method.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
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

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
    MessagingFactory msgFactory = MessagingFactory.getInstance();
    msgFactory.createMessenger("test").destroy(true);
    msgFactory.createMessenger("other").destroy(true);
  }

}
