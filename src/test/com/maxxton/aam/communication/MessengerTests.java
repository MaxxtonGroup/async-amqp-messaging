package com.maxxton.aam.communication;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.resources.Resources;

import static org.junit.Assert.*;

/**
 * Run a list of tests against the Messenger class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessengerTests
{

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @BeforeClass
  public static void setup()
  {
    // TODO : load custom configuration file
  }

  /**
   * Test the functionality of the constructor method.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testConstructor() throws Exception
  {
    System.out.print("Messenger : Testing constructor...");

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    System.out.println("done.");
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

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    Resources resources = new Resources();
    msgr.setResources(resources);
    Resources otherResources = msgr.getResources();

    assertEquals("The Resources instances are not the same.", resources, otherResources);

    msgr.destroy(false);

    System.out.println("done.");
  }

  /**
   * Test the functionality of the CommunicationController class getter and setter.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testCommunicationController() throws Exception
  {
    System.out.print("Messenger : Testing communicationcontroller setter and getter...");

    Messenger msgr = new Messenger("test");
    assertNotNull("The messenger cannot be NULL.", msgr);

    CommunicationController communication = new CommunicationController(msgr.getResources());
    msgr.setCommunication(communication);
    CommunicationController otherCommunication = msgr.getCommunication();

    assertEquals("The CommunicationController instances are not the same.", communication, otherCommunication);

    msgr.destroy(false);

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

    Messenger sender = new Messenger("test");

    // TODO : change test to match MessageType option (currently null).
    sender.sendMessage(null, "other", "Hello World");

    sender.destroy(false);

    Messenger receiver = new Messenger("other");

    Thread.sleep(50);

    String msg = (String) receiver.receiveMessage();

    assertNotNull("No message has been received.", msg);

    receiver.destroy(false);

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

    // TODO : once callback is implemented test it by sending a message and receiving it through the callback set.

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
