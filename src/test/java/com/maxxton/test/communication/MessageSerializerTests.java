package com.maxxton.test.communication;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.communication.MessageSerializer;

/**
 * Run a list of test against the MessageSerializer class.
 *
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageSerializerTests
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
   * Test serialization and deserialization of objects
   *
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testSerializeAndDeserialize() throws Exception
  {
    System.out.print("MessageSerializer : Testing serialization and deserialization of object...");

    String sTest = "Hello World";
    byte[] baSerialized = MessageSerializer.serialize(sTest);

    String sOther = (String) MessageSerializer.deserialize(baSerialized);

    assertEquals("The String instances are not the same.", sTest, sOther);

    System.out.println("done.");
  }

  /**
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
    // TODO : Cleanup queues and connections.
  }
}
