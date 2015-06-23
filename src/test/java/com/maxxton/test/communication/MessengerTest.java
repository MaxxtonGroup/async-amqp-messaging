package com.maxxton.test.communication;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.communication.CommunicationController;
import com.maxxton.aam.communication.Messenger;
import com.maxxton.aam.messages.MessageType;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.Configuration;
import com.maxxton.aam.resources.MessageDetails;
import com.maxxton.aam.resources.Resources;

/**
 * Run a list of tests against the Messenger class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessengerTest
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
    String strPayload = "Hello World";
    this.objMessenger.sendMessage(MessageType.GENERATION_MESSAGE, "other", strPayload);

    Messenger receiver = new Messenger("other");
    receiver.loadConfiguration("/test.properties");
    receiver.start();

    MessageDetails details = receiver.receiveMessage(1000);

    assertNotNull("No message has been received.", details);
    assertEquals("The payload should equal 'Hello World'", details.getPayload(), strPayload);

    System.out.println("done.");
  }

  /**
   * Test loading a configuration file.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testConfigurationLoad() throws Exception
  {
    System.out.print("Messenger : Testing configuration loading...");

    Messenger other = new Messenger("other");
    other.loadConfiguration("/test.properties");
    other.start();

    Configuration objConfig = other.getResources().getConfiguration();
    assertNotNull("The configuration instance cannot be Null.", objConfig);

    String strHost = objConfig.getHost();
    assertEquals("The host should equal '192.168.252.141'.", strHost, "192.168.252.141");

    ArrayList<Integer> arrPorts = objConfig.getPorts();
    int iPort = arrPorts.get(0);
    assertEquals("The port should equal '5672'.", iPort, 5672);

    String strUsername = objConfig.getUsername();
    assertEquals("The username should equal 'username'.", strUsername, "username");

    String strPassword = objConfig.getPassword();
    assertEquals("The password should equal 'password'.", strPassword, "password");

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

    String strPayload = "Hello World";

    this.objMessenger.sendMessage(MessageType.GENERATION_MESSAGE, "other", strPayload);

    Messenger other = new Messenger("other");
    other.loadConfiguration("/test.properties");
    other.start();

    Callback objCallback = new Callback()
    {
      @Override
      public void handleMessage(MessageDetails message)
      {
        assertEquals("The payload should equal 'Hello World'.", strPayload, "Hello World");
      }
    };

    other.setReceiveCallback(objCallback);

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {

  }

}
