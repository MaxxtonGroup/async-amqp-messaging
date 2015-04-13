package com.maxxton.aam.communication;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.maxxton.aam.resources.Resources;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;

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
  private CachingConnectionFactory objConnection;

  /**
   * SendController constructor Initiates elements defined in this class
   * 
   * @param resources
   *          an instance of the resources class
   */
  public SendController(Resources resources)
  {
    // TODO : Change the key to the appropriate one as mentioned in the configuration class.
    this.objResources = resources;
    this.objContainer = DataContainer.getInstance(this.objResources.getHost().getMessengerName());

    this.connectToBroker();
  }

  /**
   * Creates a new connection to an AMQP broker with the given information.
   */
  private void connectToBroker()
  {
    if (this.objConnection == null)
    {
      // TODO : change static information to dynamically loaded
      this.objConnection = new CachingConnectionFactory("localhost");
      this.objConnection.setUsername("username");
      this.objConnection.setPassword("password");
    }
  }

  /**
   * Stops the message listener and closes the connection.
   */
  public void disconnectFromBroker()
  {
    this.objConnection.destroy();
  }

  /**
   * Sends a converted message to a given receiver.
   * 
   * @param receiver
   *          The receiver of the message.
   * @param message
   *          The converted message to be send out.
   * @return a boolean if the receiver exists.
   */
  public boolean sendMessage(String receiver, Message message)
  {
    RabbitTemplate template = new RabbitTemplate(this.objConnection);
    template.setExchange("amq.direct");
    template.setRoutingKey(receiver + ".route");

    boolean excists = template.execute(new ChannelCallback<DeclareOk>()
    {
      @Override
      public DeclareOk doInRabbit(com.rabbitmq.client.Channel channel) throws Exception
      {
        try
        {
          return channel.queueDeclarePassive(receiver + ".queue");
        }
        catch (Exception e)
        {
          return null;
        }
      }
    }) != null;

    if (excists)
    {
      template.send(message);
      this.objContainer.addSendMessage(message);
    }
    return excists;
  }

  public String generateUniqueId()
  {
    return this.objContainer.getUniqueId();
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
