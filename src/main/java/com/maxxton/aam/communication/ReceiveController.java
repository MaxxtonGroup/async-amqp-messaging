package com.maxxton.aam.communication;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import com.maxxton.aam.messages.ResponseMessage;
import com.maxxton.aam.messages.StatusMessage;
import com.maxxton.aam.resources.Callback;
import com.maxxton.aam.resources.Configuration;
import com.maxxton.aam.resources.Resources;

/**
 * ReceiveController class Handles all message communication which involves receiving.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class ReceiveController implements MessageListener
{
  private Resources objResources;
  private DataContainer objContainer;
  private Callback objCallback;
  private SimpleMessageListenerContainer objListener;
  private CachingConnectionFactory objConnection;
  private RabbitAdmin objAdmin;
  
  private Binding objBinding;
  private Queue objQueue;

  /**
   * ReceiveController constructor Initiates elements defined in this class
   * 
   * @param resources
   *          an instance of the resources class
   */
  public ReceiveController(Resources resources)
  {
    // TODO : Change the key to the appropriate one as mentioned in the configuration class.
    this.objResources = resources;
    this.objContainer = DataContainer.getInstance(this.objResources.getConfiguration().getName());
    this.objCallback = null;

    this.connectToBroker();
    this.configureQueue();
    this.objListener = this.configureMessageListener();
  }

  /**
   * Configures the queue and binding based on the messenger specific name.
   */
  private void configureQueue()
  {
    objAdmin = new RabbitAdmin(this.objConnection);

    Configuration config = this.objResources.getConfiguration();
    String queueName = config.getQueuePrefix() + config.getName() + config.getQueueSuffix();
    boolean queueDurability = config.getQueueDurability();
    boolean queueAutoDelete = config.getQueueAutoDelete();
    boolean queueExclusive = config.getQueueExclusive();

    this.objQueue = new Queue(queueName, queueDurability, queueAutoDelete, queueExclusive);
    this.objAdmin.declareQueue(this.objQueue);
    
    String bindingName = config.getBindingPrefix() + config.getName() + config.getBindingSuffix();
    String bindingExchange = config.getBindingExchange();
    
    this.objBinding = new Binding(this.objQueue.getName(), DestinationType.QUEUE, bindingExchange, bindingName, null);
    this.objAdmin.declareBinding(this.objBinding);
  }

  /**
   * Creates a new MessageListener that gets fired once a message has been received.
   * 
   * @return an instance of SimpleMessageListernerContainer
   */
  private SimpleMessageListenerContainer configureMessageListener()
  {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(this.objConnection);
    container.setQueueNames(this.objQueue.getName());
    container.setMessageListener(this);
    container.afterPropertiesSet();
    container.start();
    return container;
  }

  /**
   * Creates a new connection to an AMQP broker with the given information.
   */
  private void connectToBroker()
  {
    if (this.objConnection == null)
    {
      this.objConnection = new CachingConnectionFactory();
      String host = this.objResources.getConfiguration().getHost();
      String addresses = "";
      for (int port : this.objResources.getConfiguration().getPorts())
      {
        addresses = addresses + host + ":" + port + ",";
      }
      this.objConnection.setAddresses(addresses);
      this.objConnection.setUsername(this.objResources.getConfiguration().getUsername());
      this.objConnection.setPassword(this.objResources.getConfiguration().getPassword());
      this.objConnection.setChannelCacheSize(25);
    }
  }

  /**
   * Get the oldest message available from the DataContainer.
   * 
   * @param millis
   *          timeout given in milliseconds.
   * @return The received message
   */
  public Message receiveMessage(long millis)
  {
    Message message = this.objContainer.popReceivedMessage();
    while (message == null && millis > 0)
    {
      long time = millis % 100;
      if (time == 0)
      {
        time = 10;
      }

      millis = millis - time;
      try
      {
        Thread.sleep(time);
      }
      catch (InterruptedException e)
      {
        // TODO : Change to methods from Monitor class.
        e.printStackTrace();
      }
      message = this.objContainer.popReceivedMessage();
    }
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
   * @param callback
   *          instance of the Callback class
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
   *          instance of the Resources class
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

  /**
   * Sets the SimpleMessageListenerContainer class
   * 
   * @param listener
   *          instance of the SimpleMessageListenerContainer class
   */
  public void setListener(SimpleMessageListenerContainer listener)
  {
    this.objListener = listener;
  }

  /**
   * Gets the SimpleMessageListenerContainer class
   * 
   * @return an instance of the SimpleMessageListenerContainer class
   */
  public SimpleMessageListenerContainer getListener()
  {
    return this.objListener;
  }

  /**
   * Handles incoming messages based on whether or not the callback has been set.
   * 
   * @param correlationId
   *          the id of the message.
   * @param message
   *          the message to be handled.
   */
  private void handleMessageCallback(String correlationId, Message message)
  {
    if (this.objCallback != null)
    {
      // TODO : add custom callback support
    }
    else
    {
      this.objContainer.removeSendMessageById(correlationId);
      this.objContainer.addReceivedMessage(message);
    }
  }

  /**
   * Asynchronously handles incoming messages
   * 
   * @param message
   *          the received message instance
   */
  @Override
  public void onMessage(Message message)
  {
    MessageProperties properties = message.getMessageProperties();
    byte[] ciBytes = properties.getCorrelationId();

    if (ciBytes != null)
    {
      if (ciBytes.length > 0)
      {
        String correlationId = properties.getCorrelationId().toString();
        Object messageBody = MessageSerializer.deserialize(message.getBody());
        if (messageBody instanceof StatusMessage || messageBody instanceof ResponseMessage)
        {
          if (this.objContainer.isOwnedByMe(correlationId))
          {
            this.handleMessageCallback(correlationId, message);
          }
          else
          {
            if (properties.getRedelivered())
            {
              // TODO : handle id which where not recognized by the messenger (maybe due client/broker failure, messages where resent).
              this.objContainer.addOddMessage(message);
            }
          }
        }
        else
        {
          this.handleMessageCallback(correlationId, message);
        }
      }
      else
      {
        // TODO : Generate error based on 'CorrelationId is empty.'
      }
    }
    else
    {
      // TODO : Generate error based on 'CorrelationId is not set.'
    }
  }
}
