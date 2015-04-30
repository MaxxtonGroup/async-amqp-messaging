package com.maxxton.test.resources;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import com.maxxton.aam.resources.ConfigParser;

/**
 * Runs a list of tests against the ConfigParser class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigParserTests
{

  @Before
  public void setup()
  {

  }

  /**
   * Test the file sorting mechanism.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testFileSorting() throws Exception
  {
    System.out.print("ConfigParser : Testing parsing of allowed and disallowed files...");

    Properties objXml = ConfigParser.parseConfig("/parser.xml");
    assertNotNull("The properties instance should not be null.", objXml);

    Properties objProperties = ConfigParser.parseConfig("/parser.properties");
    assertNotNull("The properties instance should not be null.", objProperties);

    Properties objTxt = ConfigParser.parseConfig("/parser.txt");
    assertNull("The properties instance should be null.", objTxt);

    System.out.println("done.");
  }

  /**
   * Test loading and parsing a Xml file.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testXmlParsing() throws Exception
  {
    System.out.print("ConfigParser : Testing parsing of a XML configuration file...");

    Properties objProperties = ConfigParser.parseConfig("/parser.xml");
    assertNotNull("The properties instance should not be null.", objProperties);

    String strHost = objProperties.getProperty("broker.host");
    assertNotNull("The property 'broker.host' should exist", strHost);
    assertEquals("The property 'broker.host' should equal 'localhost'", strHost, "localhost");

    String strPorts = objProperties.getProperty("broker.ports");
    assertNotNull("The property 'broker.ports' should exist", strPorts);
    assertEquals("The property 'broker.ports' should equal '5672'", strPorts, "5672");

    String strUsername = objProperties.getProperty("broker.username");
    assertNotNull("The property 'broker.username' should exist", strUsername);
    assertEquals("The property 'broker.username' should equal 'username'", strUsername, "username");

    String strPassword = objProperties.getProperty("broker.password");
    assertNotNull("The property 'broker.password' should exist", strPassword);
    assertEquals("The property 'broker.password' should equal 'password'", strPassword, "password");

    System.out.println("done.");
  }

  /**
   * Test loading and parsing a Properties file.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testPropertiesParsing() throws Exception
  {
    System.out.print("ConfigParser : Testing parsing of a Properties configuration file...");

    Properties objProperties = ConfigParser.parseConfig("/parser.properties");
    assertNotNull("The properties instance should not be null.", objProperties);

    String strHost = objProperties.getProperty("broker.host");
    assertNotNull("The property 'broker.host' should exist", strHost);
    assertEquals("The property 'broker.host' should equal 'localhost'", strHost, "localhost");

    String strPorts = objProperties.getProperty("broker.ports");
    assertNotNull("The property 'broker.ports' should exist", strPorts);
    assertEquals("The property 'broker.ports' should equal '5672'", strPorts, "5672");

    String strUsername = objProperties.getProperty("broker.username");
    assertNotNull("The property 'broker.username' should exist", strUsername);
    assertEquals("The property 'broker.username' should equal 'username'", strUsername, "username");

    String strPassword = objProperties.getProperty("broker.password");
    assertNotNull("The property 'broker.password' should exist", strPassword);
    assertEquals("The property 'broker.password' should equal 'password'", strPassword, "password");

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {

  }
}
