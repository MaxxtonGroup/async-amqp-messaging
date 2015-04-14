package com.maxxton.aam.communication;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerateMessage;
import com.maxxton.aam.resources.Resources;

import static org.junit.Assert.*;

/**
 * Run a list of tests against the CommuncationController class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommunicationControllerTests
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
   * Test the constructor of the CommunicationController class.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testConstructor() throws Exception
  {
    System.out.print("CommunicationController : Testing constructor...");

    Resources objResources = new Resources();
    objResources.getHost().setMessengerName("test");
    CommunicationController comController = new CommunicationController(objResources);

    assertNotNull("The CommunicationController cannot be Null", comController);

    System.out.println("done.");
  }

  /**
   * Test the functionality of the SendController class getter and setter.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSender() throws Exception
  {
    System.out.print("CommunicationController : Testing sendcontroller setter and getter...");

    Resources objResources = new Resources();
    objResources.getHost().setMessengerName("test");
    CommunicationController comController = new CommunicationController(objResources);
    SendController testSender = new SendController(objResources);
    comController.setSender(testSender);
    SendController otherSender = comController.getSender();

    assertEquals("The SendController instances are not the same.", testSender, otherSender);

    System.out.println("done.");
  }

  /**
   * Test the functionality of the ReceiveController class getter and setter.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiver() throws Exception
  {
    System.out.print("CommunicationController : Testing receivecontroller setter and getter...");

    Resources objResources = new Resources();
    objResources.getHost().setMessengerName("test");
    CommunicationController comController = new CommunicationController(objResources);
    ReceiveController testReceiver = new ReceiveController(objResources);
    comController.setReceiver(testReceiver);
    ReceiveController otherReceiver = comController.getReceiver();

    assertEquals("The ReceiveController instance are not the same.", testReceiver, otherReceiver);

    System.out.println("done.");
  }

  /**
   * Test the functionality of sending and receiving a message.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSendAndReceive() throws Exception
  {
    System.out.print("CommunicationController : Testing sending and receiving a message...");

    Resources objResourcesTest = new Resources();
    objResourcesTest.getHost().setMessengerName("test");
    CommunicationController comControllerTest = new CommunicationController(objResourcesTest);

    BaseMessage msgTest = new GenerateMessage();
    msgTest.setPayload("Hello World");
    comControllerTest.packAndSend("other", msgTest);

    Resources objResourcesOther = new Resources();
    objResourcesOther.getHost().setMessengerName("other");
    CommunicationController comControllerOther = new CommunicationController(objResourcesOther);

    Thread.sleep(50);

    BaseMessage msgOther = comControllerOther.unpackAndReceive();

    assertEquals("The GenerateMessage instances are not the same.", msgTest, msgOther);

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
    // TODO : cleanup queues and close connection.
  }

}
