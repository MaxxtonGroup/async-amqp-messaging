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

  /**
   * ReceiveController constructor Initiates elements defined in this class
   */
  public ReceiveController(Resources resources)
  {
    // TODO : Change the key to the appropriate one as mentioned in the configuration class.
    this.objContainer = DataContainer.getInstance("temporary");
    this.objCallback = null;
    this.objResources = resources;

    this.configureQueue();
    this.objListener = this.configureMessageListener();
  }

  /**
   * Configures the queue and binding based on the messenger specific name.
   */
  private void configureQueue()
  {
    // TODO : change static defined name to dynamically declared configuration variable
    String receiver = "test";
    CachingConnectionFactory connection = this.connectToBroker();

    RabbitAdmin admin = new RabbitAdmin(connection);
    Queue queue = new Queue(receiver + ".queue", true, false, false);
    admin.declareQueue(queue);
    Binding binding = new Binding(queue.getName(), DestinationType.QUEUE, "amq.direct", receiver + ".route", null);
    admin.declareBinding(binding);
  }

  /**
   * Creates a new MessageListener that gets fired once a message has been received.
   * 
   * @return an instance of SimpleMessageListernerContainer
   */
  private SimpleMessageListenerContainer configureMessageListener()
  {
    // TODO : change static defined name to dynamically declared configuration variable
    String receiver = "test";

    CachingConnectionFactory connection = this.connectToBroker();

    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connection);
    container.setQueueNames(receiver + ".queue");
    container.setMessageListener(this);
    container.afterPropertiesSet();
    container.start();
    return container;
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
   * Get the oldest message available from the DataContainer.
   * 
   * @return Message The received message
   */
  public Message receiveMessage()
  {
    Message message = this.objContainer.popReceivedMessage();
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
   *          nstance of the Callback class
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
   * @param messagen
   */
  private void handleMessageCallback(String correlationId, Message message)
  {
    if (this.objCallback == null)
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
        String correlationId = (String) MessageSerializer.deserialize(properties.getCorrelationId());
        Object messageBody = MessageSerializer.deserialize(message.getBody());
        if (messageBody instanceof StatusMessage || messageBody instanceof ResponseMessage)
        {
          if (this.objContainer.isOwnedByMe(correlationId))
          {
            this.handleMessageCallback(correlationId, message);
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
