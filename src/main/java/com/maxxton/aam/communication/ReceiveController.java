package com.maxxton.aam.communication;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.Resources;

//TODO : set up callback(s) for asynchronous message receiving

/**
 * ReceiveController class
 * Handles all message communication which involves receiving.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class ReceiveController
{ 
  private Resources objResources;
  private DataContainer objContainer;
  private Callback objCallback;

  /**
   * ReceiveController constructor
   * Initiates elements defined in this class
   */
  public ReceiveController(Resources resources)
  {
    // TODO : Change the key to the appropriate one as mentioned in the configuration class.
    this.objContainer = DataContainer.getInstance("temporary");
    this.objCallback = null;
    this.objResources = resources;
    
    this.configureQueue();
  }
  
  /**
   * 
   */
  private void configureQueue()
  {
    // TODO : change static defined name to dynamically declared configuration variable
    String receiver = "test";
    CachingConnectionFactory connection = this.connectToBroker();
    
    RabbitAdmin admin = new RabbitAdmin(connection);
    Queue queue = new Queue(receiver+".queue", true, false, false);
    admin.declareQueue(queue);
    Binding binding = new Binding(queue.getName(), DestinationType.QUEUE, "amq.direct", receiver+".route", null);
    admin.declareBinding(binding);
    
    connection.destroy();
  }
  
  /**
   * Creates a new connection to an AMQP broker with the given information.
   * 
   * @return an object to be able to manage connections.
   */
  private CachingConnectionFactory connectToBroker()
  {
    // TODO : change static information to dynamically loaded
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
    connectionFactory.setUsername("username");
    connectionFactory.setPassword("password");
    return connectionFactory;
  }
  
  /**
   * 
   * 
   * @return Message The received message
   */
  public Message receiveMessage()
  { 
    // TODO : change static defined name to dynamically declared configuration variable
    String receiver = "test";
    
    CachingConnectionFactory connection = this.connectToBroker();
    RabbitTemplate template = new RabbitTemplate(connection);
    
    Message message = template.receive(receiver+".queue");
    
    connection.destroy();
    
    return message;
  }
  
  /**
   * Gets the DataContainer instance.
   * 
   * @param container
   *          Instance of DataContainer class
   */
  public void setDataContainer(DataContainer container)
  {
    this.objContainer = container;
  }

  /**
   * Sets the DataContainer instance.
   * 
   * @return an instance of the DataContainer class
   */
  public DataContainer getDataContainer()
  {
    return this.objContainer;
  }
  
  /**
   * Sets the Callback instance.
   * 
   * @param callback Instance of the Callback class
   */
  public void setCallback(Callback callback)
  {
    this.objCallback = callback;
  }
  
  /**
   * Gets the Callback instance.
   * 
   * @return an instance of the Callback class
   */
  public Callback getCallback()
  {
    return this.objCallback;
  }
  
  /**
   * Sets the Resources class
   * 
   * @param resources 
   */
  public void setResources(Resources resources)
  {
    this.objResources = resources;
  }
  
  
  /**
   * Gets the Resources class
   * 
   * @return an instance of the Resources class
   */
  public Resources getResources()
  {
    return this.objResources;
  }
}
