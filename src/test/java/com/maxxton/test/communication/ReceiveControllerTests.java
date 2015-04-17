package com.maxxton.test.communication;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.maxxton.aam.communication.DataContainer;
import com.maxxton.aam.communication.MessageSerializer;
import com.maxxton.aam.communication.ReceiveController;
import com.maxxton.aam.communication.SendController;
import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerateMessage;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.Resources;

/**
 * Run a list of tests against the ReceiveController class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReceiveControllerTests
{

  private Resources objResources;
  private ReceiveController objReceiver;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    // if (this.objReceiver == null)
    // {
    this.objResources = new Resources();
    this.objResources.getHost().setMessengerName("test");
    this.objReceiver = new ReceiveController(objResources);
    // }
  }

  /**
   * Test the receive method for messages.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiving() throws Exception
  {
    System.out.print("ReceiveController : Testing receiving a message...");

    Resources otherResources = new Resources();
    otherResources.getHost().setMessengerName("other");
    SendController sender = new SendController(otherResources);

    MessageProperties props = new MessageProperties();
    props.setCorrelationId(sender.generateUniqueId().getBytes());
    BaseMessage baseMsg = new GenerateMessage();
    baseMsg.setPayload("Hello World");
    Message msg = new Message(MessageSerializer.serialize(baseMsg), props);
    boolean exists = sender.sendMessage("test", msg);

    assertTrue("The receiver does not exist", exists);

    Message receiverdMsg = this.objReceiver.receiveMessage(1000);
    assertNotNull("The received message cannot be null.", receiverdMsg);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the datacontainer class.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testDataContainer() throws Exception
  {
    System.out.print("ReceiveController : Testing getter and setter for the datacontainer instance...");

    DataContainer testContainer = this.objReceiver.getDataContainer();
    assertNotNull("The datacontainer instance cannot be null.", testContainer);

    DataContainer otherContainer = DataContainer.getInstance("other");
    this.objReceiver.setDataContainer(otherContainer);

    DataContainer sameContainer = this.objReceiver.getDataContainer();
    assertNotNull("The datacontainer instance cannot be null.", sameContainer);

    assertNotEquals("The datacontainer instances should not be te same.", testContainer, sameContainer);
    assertEquals("The datacontainer instances should be the same.", otherContainer, sameContainer);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the resources class.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testResources() throws Exception
  {
    System.out.print("ReceiveController : Testing getter and setter for the resources instance...");

    Resources testResources = this.objReceiver.getResources();
    assertNotNull("The resources instance cannot be null.", testResources);

    Resources otherResources = new Resources();
    this.objReceiver.setResources(otherResources);

    Resources sameResources = this.objReceiver.getResources();
    assertNotNull("The resources instance cannot be null.", sameResources);

    assertNotEquals("The resource instances should not be the same", testResources, sameResources);
    assertEquals("The resource instances should be the same.", otherResources, sameResources);

    System.out.println("done.");
  }

  /**
   * Test the getter and setter for the callback class.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testCallback() throws Exception
  {
    System.out.print("ReceiveController : Testing getter and setter for the callback instance...");

    // TODO : Add test for receiving a message though the callback.
    Callback testCallback = this.objReceiver.getCallback();
    assertNull("The callback instance should be null.", testCallback);

    Callback otherCallback = new Callback();
    this.objReceiver.setCallback(otherCallback);

    Callback sameCallback = this.objReceiver.getCallback();
    assertNotNull("The callback instance cannot be null.", sameCallback);

    assertNotEquals("The callback instances cannot be the same.", testCallback, sameCallback);
    assertEquals("The callback instances should be the same.", otherCallback, sameCallback);

    this.objReceiver.setCallback(testCallback);

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
