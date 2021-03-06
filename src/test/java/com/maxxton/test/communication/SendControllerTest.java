package com.maxxton.test.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.maxxton.aam.communication.DataContainer;
import com.maxxton.aam.communication.MessageSerializer;
import com.maxxton.aam.communication.ReceiveController;
import com.maxxton.aam.communication.SendController;
import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerationMessage;
import com.maxxton.aam.resources.Resources;

/**
 * Run a list of tests against the SendController class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SendControllerTest
{

  private Resources objResources;
  private SendController objController;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    if (objResources == null)
    {
      this.objResources = new Resources();
      this.objResources.getConfiguration().loadConfiguration("/test.properties");
      this.objResources.getConfiguration().setName("test");
      this.objController = new SendController(this.objResources);

      assertNotNull("The sendcontroller instance cannot be null.", this.objController);
    }
  }

  /**
   * Test the functionality of the receiver exists method.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceiverExists() throws Exception
  {
    System.out.print("SendController : Testing receiver exists method...");

    Resources resources = new Resources();
    resources.getConfiguration().loadConfiguration("/test.properties");
    resources.getConfiguration().setName("other");

    ReceiveController otherSender = new ReceiveController(resources);
    assertNotNull("The other sendcontroller instance cannot be null.", otherSender);

    boolean exists = objController.doesReceiverExist("other");
    assertTrue("The other receiving instance should exist.", exists);

    exists = objController.doesReceiverExist("none");
    assertFalse("The other receiving instance not should exist.", exists);

    System.out.println("done.");
  }

  /**
   * Test the send method for messages.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSending() throws Exception
  {
    System.out.print("SendController : Testing sending a message...");

    Resources otherResources = new Resources();
    otherResources.getConfiguration().loadConfiguration("/test.properties");
    otherResources.getConfiguration().setName("other");
    new ReceiveController(otherResources);

    MessageProperties props = new MessageProperties();
    props.setCorrelationId(this.objController.generateUniqueId().getBytes());
    BaseMessage baseMsg = new GenerationMessage();
    baseMsg.setPayload("Hello World");
    Message msg = new Message(MessageSerializer.serialize(baseMsg), props);
    boolean exists = this.objController.sendMessage("other", msg);

    assertTrue("The receiver does not exist", exists);

    exists = this.objController.sendMessage("none", msg);
    assertFalse("The receiver does exist", exists);

    System.out.println("done.");
  }

  /**
   * Test the generation of an unique identifier.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testGenerateUniqueId() throws Exception
  {
    System.out.print("SendController : Testing generation of a unique identifier...");

    String uuid = this.objController.generateUniqueId();
    assertNotNull("The unique identifier cannot be null.", uuid);

    String otherUuid = this.objController.generateUniqueId();
    assertNotNull("The unique identifier cannot be null.", otherUuid);

    assertNotEquals("The unique identigiers cannot be the same.", uuid, otherUuid);

    System.out.println("done.");
  }

  /**
   * Test the datacontainer class getter and setter.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testDataContainer() throws Exception
  {
    System.out.print("SendController : Testing getter and setter for the datacontainer instance...");

    DataContainer testContainer = this.objController.getDataContainer();
    assertNotNull("The datacontainer instance cannot be null.", testContainer);

    DataContainer otherContainer = DataContainer.getInstance("other");
    this.objController.setDataContainer(otherContainer);

    DataContainer sameContainer = this.objController.getDataContainer();
    assertNotNull("The datacontainer instance cannot be null.", sameContainer);

    assertNotEquals("The datacontainer instances should not be te same.", testContainer, sameContainer);
    assertEquals("The datacontainer instances should be the same.", otherContainer, sameContainer);

    System.out.println("done.");
  }

  /**
   * Test the resources class getter and setter.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testResources() throws Exception
  {
    System.out.print("SendController : Testing getter and setter for the resources instance...");

    Resources testResources = this.objController.getResources();
    assertNotNull("The resources instance cannot be null.", testResources);

    Resources otherResources = new Resources();
    this.objController.setResources(otherResources);

    Resources sameResources = this.objController.getResources();
    assertNotNull("The resources instance cannot be null.", sameResources);

    assertNotEquals("The resource instances should not be the same", testResources, sameResources);
    assertEquals("The resource instances should be the same.", otherResources, sameResources);

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
