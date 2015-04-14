package com.maxxton.aam.communication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.maxxton.aam.messages.BaseMessage;
import com.maxxton.aam.messages.GenerateMessage;

import static org.junit.Assert.*;

/**
 * Run a list of tests against the DataContainer class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataContainerTests
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
   * Test the (multi)singleton method of the DataContainer class.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testMultiton() throws Exception
  {
    System.out.print("DataContainer : Testing (multi) singleton method...");

    DataContainer dataContainerTest = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainerTest);

    DataContainer dataContainerOther = DataContainer.getInstance("other");
    assertNotNull("The datacontainer cannot be Null", dataContainerOther);

    DataContainer dataContainerSame = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainerSame);

    assertEquals("The datacontainer instances are not the same.", dataContainerTest, dataContainerSame);
    assertNotEquals("The datacontainer instances are not different.", dataContainerTest, dataContainerOther);

    System.out.println("done.");
  }

  /**
   * Test the generation of a random identifier.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testRandomId() throws Exception
  {
    System.out.print("DataContainer : Testing generation of random id...");

    DataContainer dataContainer = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainer);

    String id = dataContainer.getUniqueId();
    assertNotNull("The unique id given cannot be Null", id);
    assertNotEquals("The unique id given cannot be empty", id, "");

    System.out.println("done.");
  }

  /**
   * Test if a certain identifier is owned by DataContainer.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testIsOwnedByMe() throws Exception
  {
    System.out.print("DataContainer : Testing if an id is owned by the datacontainer...");

    DataContainer dataContainer = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainer);

    Set<String> ids = new HashSet<String>();
    String idOne = "0123456789";
    String idTwo = "9876543210";
    ids.add(idOne);
    ids.add(idTwo);

    dataContainer.setIdentifiers(ids);

    boolean owned = dataContainer.isOwnedByMe(idOne);
    assertTrue("The id should be owned by the datacontainer class.", owned);

    owned = dataContainer.isOwnedByMe(idTwo);
    assertTrue("The id should be owned by the datacontainer class.", owned);

    String idThree = "1029384756";
    owned = dataContainer.isOwnedByMe(idThree);
    assertFalse("The id should not be owned by the datacontainer class.", owned);

    System.out.println("done.");
  }

  /**
   * Test the getting, setting and removal of identifiers.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testIdentifiers() throws Exception
  {
    System.out.print("DataContainer : Testing getting, setting and removal of identifiers..");

    DataContainer dataContainer = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainer);

    Set<String> ids = new HashSet<String>();
    Set<String> alterIds = new HashSet<String>();
    for (int i = 0; i < 10; i++)
    {
      String uuid = dataContainer.getUniqueId();
      ids.add(uuid);
      alterIds.add(uuid);
    }

    dataContainer.setIdentifiers(ids);
    Set<String> idsOther = dataContainer.getIdentifiers();

    assertNotNull("The set with identifiers cannot be Null", idsOther);
    assertEquals("The sets with ids are not the same.", ids, idsOther);

    // Testing removal
    Iterator<String> iterator = ids.iterator();

    String removedId = iterator.next();
    dataContainer.removeId(removedId);

    idsOther = dataContainer.getIdentifiers();

    assertNotNull("The set with identifiers cannot be Null", idsOther);
    assertNotEquals("The sets with identifiers are not the same.", alterIds, idsOther);

    alterIds.remove(removedId);
    assertEquals("The sets with identifiers are not the same.", alterIds, idsOther);

    System.out.println("done.");
  }

  /**
   * Test the getting, setting and removal of send messages.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSendMessages() throws Exception
  {
    System.out.print("DataContainer : Testing getting, setting and removal of send messages...");

    DataContainer dataContainer = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null.", dataContainer);

    Message msgOne = new Message("Hello World".getBytes(), new MessageProperties());
    Message msgTwo = new Message("Hello World".getBytes(), new MessageProperties());
    Message msgThree = new Message("Hello World".getBytes(), new MessageProperties());

    dataContainer.addSendMessage(msgOne);
    dataContainer.addSendMessage(msgTwo);

    Set<Message> sendMessages = new HashSet<Message>();
    sendMessages.add(msgOne);
    sendMessages.add(msgTwo);

    Set<Message> otherMessages = dataContainer.getSendMessages();

    assertNotNull("The set with message cannot be Null.", otherMessages);
    assertEquals("The sets with messages where not the same.", otherMessages, sendMessages);

    dataContainer.removeSendMessage(msgOne);
    assertNotEquals("The sets with messages cannot be the same.", otherMessages, sendMessages);

    sendMessages.remove(msgOne);
    assertEquals("The sets with messages where not the same.", otherMessages, sendMessages);

    System.out.println("done.");
  }

  /**
   * Test the getting, setting and removal of received messages.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceivedMessages() throws Exception
  {
    System.out.print("DataContainer : Testing getting, setting and removal of received messages...");

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
    // TODO : Cleanup queue and connections.
  }

}
