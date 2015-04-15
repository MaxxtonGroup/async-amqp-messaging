package com.maxxton.aam.communication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

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

  private DataContainer objContainer;

  /**
   * Setup method called before running the tests to construct the testing environment.
   */
  @Before
  public void setup()
  {
    if (this.objContainer == null)
    {
      this.objContainer = DataContainer.getInstance("test");
      assertNotNull("The datacontainer cannot be Null", this.objContainer);
    }
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

    DataContainer dataContainerOther = DataContainer.getInstance("other");
    assertNotNull("The datacontainer cannot be Null", dataContainerOther);

    DataContainer dataContainerSame = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainerSame);

    assertEquals("The datacontainer instances are not the same.", this.objContainer, dataContainerSame);
    assertNotEquals("The datacontainer instances are not different.", this.objContainer, dataContainerOther);

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

    String id = this.objContainer.getUniqueId();
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

    Set<String> ids = new HashSet<String>();
    String idOne = "0123456789";
    String idTwo = "9876543210";
    ids.add(idOne);
    ids.add(idTwo);

    this.objContainer.setIdentifiers(ids);

    boolean owned = this.objContainer.isOwnedByMe(idOne);
    assertTrue("The id should be owned by the datacontainer class.", owned);

    owned = this.objContainer.isOwnedByMe(idTwo);
    assertTrue("The id should be owned by the datacontainer class.", owned);

    String idThree = "1029384756";
    owned = this.objContainer.isOwnedByMe(idThree);
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

    Set<String> ids = new HashSet<String>();
    Set<String> alterIds = new HashSet<String>();
    for (int i = 0; i < 10; i++)
    {
      String uuid = this.objContainer.getUniqueId();
      ids.add(uuid);
      alterIds.add(uuid);
    }

    this.objContainer.setIdentifiers(ids);
    Set<String> idsOther = this.objContainer.getIdentifiers();

    assertNotNull("The set with identifiers cannot be Null", idsOther);
    assertEquals("The sets with ids are not the same.", ids, idsOther);

    // Testing removal
    Iterator<String> iterator = ids.iterator();

    String removedId = iterator.next();
    this.objContainer.removeId(removedId);

    idsOther = this.objContainer.getIdentifiers();

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

    Message msgOne = new Message("Hello First World".getBytes(), new MessageProperties());
    Message msgTwo = new Message("Hello Second World".getBytes(), new MessageProperties());

    this.objContainer.addSendMessage(msgOne);
    this.objContainer.addSendMessage(msgTwo);

    Set<Message> sendMessages = new HashSet<Message>();
    sendMessages.add(msgOne);
    sendMessages.add(msgTwo);

    Set<Message> otherMessages = this.objContainer.getSendMessages();

    assertNotNull("The set with message cannot be Null.", otherMessages);
    assertEquals("The sets with messages where not the same.", otherMessages, sendMessages);

    this.objContainer.removeSendMessage(msgOne);
    assertNotEquals("The sets with messages cannot be the same.", otherMessages, sendMessages);

    sendMessages.remove(msgOne);
    assertEquals("The sets with messages where not the same.", otherMessages, sendMessages);

    System.out.println("done.");
  }

  /**
   * Test the getting, setting and removal of received message.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testReceivedMessages() throws Exception
  {
    System.out.print("DataContainer : Testing getting, setting and removal of messages...");

    Message msgOne = new Message("Hello First World".getBytes(), new MessageProperties());
    Message msgTwo = new Message("Hello Second World".getBytes(), new MessageProperties());

    this.objContainer.addReceivedMessage(msgOne);
    this.objContainer.addReceivedMessage(msgTwo);

    Set<Message> receivedMessages = new HashSet<Message>();
    receivedMessages.add(msgOne);
    receivedMessages.add(msgTwo);

    Set<Message> otherMessages = this.objContainer.getReceivedMessages();

    assertNotNull("The set with message cannot be Null.", otherMessages);
    assertEquals("The sets with messages where not the same.", otherMessages, receivedMessages);

    this.objContainer.removeReceivedMessage(msgOne);
    assertNotEquals("The sets with messages cannot be the same.", otherMessages, receivedMessages);

    receivedMessages.remove(msgOne);
    assertEquals("The sets with messages where not the same.", otherMessages, receivedMessages);

    System.out.println("done.");
  }

  /**
   * Test the getting, setting and removal of odd messages.
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testOddMessages() throws Exception
  {
    System.out.print("DataContainer : Testing getting, setting and removal of received messages...");

    Message msgOne = new Message("Hello First World".getBytes(), new MessageProperties());
    Message msgTwo = new Message("Hello Second World".getBytes(), new MessageProperties());

    this.objContainer.addOddMessage(msgOne);
    this.objContainer.addOddMessage(msgTwo);

    Set<Message> oddMessages = new HashSet<Message>();
    oddMessages.add(msgOne);
    oddMessages.add(msgTwo);

    Set<Message> otherMessages = this.objContainer.getOddMessages();

    assertNotNull("The set with message cannot be Null.", otherMessages);
    assertEquals("The sets with messages where not the same.", otherMessages, oddMessages);

    this.objContainer.removeOddMessage(msgOne);
    assertNotEquals("The sets with messages cannot be the same.", otherMessages, oddMessages);

    oddMessages.remove(msgOne);
    assertEquals("The sets with messages where not the same.", otherMessages, oddMessages);

    System.out.println("done.");
  }

  @Test
  public void testName() throws Exception
  {
    System.out.print("DataContainer : Testing getter for name...");

    String name = this.objContainer.getName();
    assertEquals("The datacontainer's name was not the same as set.", name, "test");

    assertNotEquals("The datacontainer's name should not match given name", name, "other");

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @After
  public void cleanup()
  {
    this.objContainer.destroy();
  }

}
