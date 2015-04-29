package com.maxxton.test.communication;

import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.communication.CommunicationController;
import com.maxxton.aam.communication.Messenger;
import com.maxxton.aam.resources.Resources;

/**
 * Run a list of tests against the Messenger class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessengerTests
{

  private Messenger objMessenger;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    this.objMessenger = new Messenger("test");
    this.objMessenger.loadConfiguration("/test.properties");
    this.objMessenger.start();
    assertNotNull("The messenger cannot be NULL.", this.objMessenger);
  }

  /**
   * Test the functionality of the Resource class getter and setter.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testResources() throws Exception
  {
    System.out.print("Messenger : Testing resources setter and getter...");

    Resources resources = this.objMessenger.getResources();
    assertNotNull("The resources instance cannot be null.", resources);

    System.out.println("done.");
  }

  /**
   * Test the functionality of the CommunicationController class getter.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testCommunicationController() throws Exception
  {
    System.out.print("Messenger : Testing communicationcontroller getter...");

    CommunicationController communication = this.objMessenger.getCommunication();
    assertNotNull("The communicationcontroller instance cannot be null.", communication);

    System.out.println("done.");
  }

  /**
   * Test the send and receive methods as given by the Messenger class.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSendAndReceive() throws Exception
  {
    System.out.print("Messenger : Testing sending and receiving...");

    // TODO : change test to match MessageType option (currently null).
    // this.objMessenger.sendMessage(null, "other", "Hello World");
    //
    // Messenger receiver = new Messenger("other");
    //
    // String msg = (String) receiver.receiveMessage(1000);
    //
    // assertNotNull("No message has been received.", msg);

    System.out.println("done.");
  }

  /**
   * Test the load of a configuration file.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testConfigurationLoad() throws Exception
  {
    System.out.print("Messenger : Testing configuration loading...");

    // TODO : once functionality added, implement this test.

    System.out.println("done.");
  }

  /**
   * Test setting of a callback method.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiveCallback() throws Exception
  {
    System.out.print("Messenger : Testing receive callback...");

    // TODO : once callback is implemented test it by sending a message and
    // receiving it through the callback set.

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
    // TODO : cleanup queues and close connections
  }

}
