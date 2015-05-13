package com.maxxton.aam.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.maxxton.aam.monitoring.Monitor;
import com.maxxton.aam.monitoring.MonitorFactory;

/**
 * MessageSerializer class. Contains static methods for serialization or deserialization of objects.
 * 
 * @author Robin Hermans
 * @copyright Maxxton 2015
 */
public class MessageSerializer
{
  private static Monitor objMonitor = MonitorFactory.getMonitor("gloabal");

  /**
   * Serializes a given object into byte array.
   * 
   * @param object
   *          Object to serialized.
   * @return serialized object as array of bytes.
   */
  public static byte[] serialize(Object object)
  {
    byte[] bytes = null;
    try
    {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
      objectStream.writeObject(object);
      bytes = byteStream.toByteArray();
    }
    catch (IOException e)
    {
      MessageSerializer.objMonitor.warn(MessageSerializer.class, "Failed to serialize certain object to bytes. See trace in the logs for more information.");
      MessageSerializer.objMonitor.trace(MessageSerializer.class, e);
    }
    return bytes;
  }

  /**
   * Deserializes an array of bytes to an object.
   * 
   * @param bytes
   *          array of bytes to be deserialized.
   * @return deserialized byte array as object.
   */
  public static Object deserialize(byte[] bytes)
  {
    Object object = null;
    try
    {
      ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
      ObjectInputStream objectStream;
      objectStream = new ObjectInputStream(byteStream);
      object = objectStream.readObject();
    }
    catch (IOException | ClassNotFoundException e)
    {
      MessageSerializer.objMonitor.warn(MessageSerializer.class, "Failed to deserialize certain bytes to object. See trace in the logs for more information.");
      MessageSerializer.objMonitor.trace(MessageSerializer.class, e);
    }
    return object;
  }
}
