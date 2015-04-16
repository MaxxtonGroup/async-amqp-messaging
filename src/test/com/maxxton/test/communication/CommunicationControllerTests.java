package com.maxxton.test.communication;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.communication.CommunicationController;
import com.maxxton.aam.communication.ReceiveController;
import com.maxxton.aam.communication.SendController;
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

  private Resources objResources;
  private CommunicationController objCommunication;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    if (this.objCommunication == null)
    {
      this.objResources = new Resources();
      this.objResources.getHost().setMessengerName("test");
      this.objCommunication = new CommunicationController(this.objResources);

      assertNotNull("The CommunicationController cannot be Null", this.objCommunication);
    }
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

    SendController testSender = new SendController(objResources);
    this.objCommunication.setSender(testSender);
    SendController otherSender = this.objCommunication.getSender();

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

    ReceiveController testReceiver = new ReceiveController(objResources);
    this.objCommunication.setReceiver(testReceiver);
    ReceiveController otherReceiver = this.objCommunication.getReceiver();

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
    
    Resources objResourcesOther = new Resources();
    objResourcesOther.getHost().setMessengerName("other");
    CommunicationController comControllerOther = new CommunicationController(objResourcesOther);

    BaseMessage msgTest = new GenerateMessage();
    msgTest.setPayload("Hello World");
    this.objCommunication.packAndSend("other", msgTest);

    BaseMessage msgOther = comControllerOther.unpackAndReceive(1500);

    assertEquals("The GenerateMessage instances are not the same.", msgTest, msgOther);

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @After
  public void cleanup()
  {
    // TODO : cleanup queues and close connection.
  }

}
