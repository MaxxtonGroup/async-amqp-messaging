package com.maxxton.test.resources;

import java.util.ArrayList;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.maxxton.aam.resources.Configuration;

/**
 * Runs a list of tests against the Configuration class.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest
{
  private Configuration objConfiguration;

  @Before
  public void setup()
  {
    if (this.objConfiguration == null)
    {
      this.objConfiguration = new Configuration();
      assertNotNull("The configuration object cannot be null.", this.objConfiguration);
    }
  }

  /**
   * Test loading the default properties through the constructor.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testDefaultLoading() throws Exception
  {
    System.out.print("Configuration : Testing default configuration loading...");

    String strHost = this.objConfiguration.getHost();
    assertEquals("The host should equal 'localhost'.", strHost, "localhost");

    ArrayList<Integer> arrPorts = this.objConfiguration.getPorts();
    int iPort = arrPorts.get(0);
    assertEquals("The ports should equal '5672'.", iPort, 5672);

    String strUsername = this.objConfiguration.getUsername();
    assertEquals("The username should equal 'username'.", strUsername, "guest");

    String strPassword = this.objConfiguration.getPassword();
    assertEquals("The password should equal 'username'.", strPassword, "guest");

    System.out.println("done.");
  }

  /**
   * Tests loading a custom properties file.
   * 
   * @throws Exception
   *           reason of failure given by the test.
   */
  @Test
  public void testConfigurationLoading() throws Exception
  {
    System.out.print("Configuration : Testing custom configuration loading...");

    this.objConfiguration.loadConfiguration("/test.properties");

    String strHost = this.objConfiguration.getHost();
    assertEquals("The host should equal '192.168.252.141'.", strHost, "192.168.252.141");

    ArrayList<Integer> arrPorts = this.objConfiguration.getPorts();
    int iPort = arrPorts.get(0);
    assertEquals("The ports should equal '5672'.", iPort, 5672);

    String strUsername = this.objConfiguration.getUsername();
    assertEquals("The username should equal 'username'.", strUsername, "username");

    String strPassword = this.objConfiguration.getPassword();
    assertEquals("The password should equal 'username'.", strPassword, "password");

    System.out.println("done.");
  }

  @After
  public void cleanup()
  {

  }

}
