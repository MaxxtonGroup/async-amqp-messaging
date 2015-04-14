package com.maxxton.aam.communication;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
    System.out.print("MessagingFactory : Testing (multi) singleton method...");
    
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
  
  @Test
  public void testRandomId() throws Exception
  {
    System.out.print("MessagingFactory : Testing generation of random id...");
    
    DataContainer dataContainer = DataContainer.getInstance("test");
    assertNotNull("The datacontainer cannot be Null", dataContainer);
    
    String id = dataContainer.getUniqueId();
    assertNotNull("The unique id given cannot be Null", id);
    assertNotEquals("The unique id given cannot be empty", id, "");
    
    System.out.println("done.");
  }
  
  @Test
  public void testIsOwnedByMe() throws Exception
  {
    System.out.print("MessagingFactory : Testing if an id is owned by the datacontainer...");
    
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
   * Static method called after running the test to cleanup.
   */
  @AfterClass
  public static void cleanup()
  {
  }

}
