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
  private CachingConnectionFactory objConnection;

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
    this.objContainer = DataContainer.getInstance(this.objResources.getHost().getMessengerName());
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
    // TODO : change static defined name to dynamically declared configuration variable
    String receiver = this.objResources.getHost().getMessengerName();

    RabbitAdmin admin = new RabbitAdmin(this.objConnection);
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
    String receiver = this.objResources.getHost().getMessengerName();

    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(this.objConnection);
    container.setQueueNames(receiver + ".queue");
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
    if (objListener.isRunning())
    {
      objListener.destroy();
    }

    this.objConnection.destroy();
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
            this.objContainer.addOddMessage(message);
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
