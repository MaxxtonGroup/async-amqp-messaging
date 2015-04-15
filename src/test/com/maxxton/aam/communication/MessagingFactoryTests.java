package com.maxxton.aam.communication;

import org.junit.After;
import org.junit.Before;
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

  private MessagingFactory objMessageFactory;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    if (objMessageFactory == null)
    {
      objMessageFactory = MessagingFactory.getInstance();
      assertNotNull("The messagingfactory cannot be NULL.", objMessageFactory);
    }
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

    MessagingFactory otherFactory = MessagingFactory.getInstance();
    assertNotNull("The messagingfactory cannot be NULL.", otherFactory);

    assertEquals("The instances returned by the singleton are not the same.", objMessageFactory, otherFactory);

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

    Messenger msgr = objMessageFactory.createMessenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    Messenger otherMsgr = objMessageFactory.createMessenger("other");
    assertNotNull("The other messenger cannot be NULL.", otherMsgr);

    assertNotEquals("The instances returned by the creational method are the same.", msgr, otherMsgr);

    Messenger sameMsgr = objMessageFactory.createMessenger("test");
    assertNotNull("The same messenger cannot be NULL.", sameMsgr);

    assertEquals("The instances returned by the creational method are not the same.", msgr, sameMsgr);

    System.out.println("done.");
  }

  /**
   * Test the functionality of the destroy method.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testDestroy() throws Exception
  {
    System.out.print("MessagingFactory : Testing destroy method...");

    Messenger msgr = this.objMessageFactory.createMessenger("test");
    this.objMessageFactory.destroyMessenger("test");
    Messenger otherMsgr = this.objMessageFactory.createMessenger("test");

    assertNotEquals("The Messenger instance should not be the same.", msgr, otherMsgr);

    System.out.println("done.");
  }

  /**
   * Cleanup method called after running the test to cleanup.
   */
  @After
  public void cleanup()
  {

  }

}
