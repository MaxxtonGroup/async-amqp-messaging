package com.maxxton.aam.communication;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.maxxton.aam.resources.Resources;

/**
 * SendController class Handles all message communication which involves sending.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class SendController
{
  private Resources objResources;
  private DataContainer objContainer;

  /**
   * SendController constructor Initiates elements defined in this class
   */
  public SendController(Resources resources)
  {
    // TODO : Change the key to the appropriate one as mentioned in the configuration class.
    this.objContainer = DataContainer.getInstance("temporary");
    this.objResources = resources;
  }

  /**
   * Creates a new connection to an AMQP broker with the given information.
   * 
   * @return an object to be able to maintain connections.
   */
  public CachingConnectionFactory connectToBroker()
  {
    // TODO : change static information to dynamically loadeds
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
    connectionFactory.setUsername("username");
    connectionFactory.setPassword("password");
    return connectionFactory;
  }

  /**
   * Sends a converted message to a given receiver.
   * 
   * @param receiver
   *          The receiver of the message.
   * @param message
   *          The converted message to be send out.
   */
  public void sendMessage(String receiver, Message message)
  {
    CachingConnectionFactory connection = connectToBroker();
    RabbitTemplate template = new RabbitTemplate(connection);
    template.setExchange("amq.direct");
    template.setRoutingKey(receiver + ".queue");

    template.send(message);

    connection.destroy();
  }

  /**
   * Sets the DataContainer
   * 
   * @param container
   *          Instance of DataContainer class
   */
  public void setDataContainer(DataContainer container)
  {
    this.objContainer = container;
  }

  /**
   * Gets the DataContainer
   * 
   * @return an instance of DataContainer class
   */
  public DataContainer getDataContainer()
  {
    return this.objContainer;
  }

  /**
   * Sets the Resource class
   * 
   * @param resources
   *          Instance of Resources class
   */
  public void setResources(Resources resources)
  {
    this.objResources = resources;
  }

  /**
   * Gets the Resource class
   * 
   * @return an instance of Resource class
   */
  public Resources getResources()
  {
    return this.objResources;
  }

}
