package engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The MessagePump is responsible for collecting messages
 * passed around the engine during a given frame. At some point
 * it can be asked to dispatch the messages (events) to any system
 * or other object which has expressed interest.
 *
 * @author Justin Hall
 */
public class MessagePump {
    private final HashMap<String, Message> _registeredMessages = new HashMap<>();
    private final HashMap<Message, LinkedList<MessageHandler>> _registeredHandlers = new HashMap<>();
    private final LinkedList<Message> _messageDispatchBuffer = new LinkedList<>();

    /**
     * Tells the message pump that you are interested in receiving event
     * notifications for the given message
     * @param message message to receive event notifications for
     * @param handler callback
     */
    public void signalInterest(String message, MessageHandler handler)
    {
        if (!contains(message))
        {
            throw new IllegalArgumentException("Non-registered message passed into MessagePump.signalInterest");
        }
        _registeredHandlers.get(getRegisteredMessage(message)).add(handler);
    }

    /**
     * Tells the message pump that the given message should be cached and it
     * should expect messages of its type fo be written in the future.
     *
     * @param message message to register
     */
    public void registerMessage(Message message)
    {
        // Only add it if it has not been added yet
        if (!_registeredMessages.containsKey(message.getMessageName()))
        {
            System.out.println("Registering message type (" + message.getMessageName() + ")");
            _registeredMessages.put(message.getMessageName(), message);
            _registeredHandlers.put(message, new LinkedList<>());
        }
    }

    /**
     * Removes a message from the message pump
     */
    public void unregisterMessage(Message message)
    {
        _registeredMessages.remove(message.getMessageName());
        _registeredHandlers.remove(message);
    }

    /**
     * Converts a String to a registered Message object
     */
    public Message getRegisteredMessage(String message)
    {
        return _registeredMessages.get(message);
    }

    /**
     * Retrieves a list of all messages currently registered by the MessagePump.
     * @return list of messages
     */
    public LinkedList<Message> getAllRegisteredMessages()
    {
        LinkedList<Message> result = new LinkedList<>();
        for (Map.Entry<String, Message> entry : _registeredMessages.entrySet())
        {
            result.add(entry.getValue());
        }
        return result;
    }

    /**
     * Checks to see if the given message (as string) has been registered
     * @return true if registered and false if not
     */
    public boolean contains(String message)
    {
        return _registeredMessages.containsKey(message);
    }

    /**
     * Equivalent to contains(String), but it allows you to pass in
     * the actual Message object.
     * @return true if registered and false if not
     */
    public boolean contains(Message message)
    {
        return contains(message.getMessageName());
    }

    /**
     * @return integer corresponding to the total number of registered messages
     */
    public int size()
    {
        return _registeredMessages.size();
    }

    /**
     * Notifies the system that you want to send a message to anyone interested
     * in receiving and processing it.
     * @param message message to send
     */
    public void sendMessage(Message message)
    {
        System.out.println("Trying to send message: " + message.getMessageName());
        if (!_registeredMessages.containsKey(message.getMessageName()))
        {
            throw new IllegalArgumentException("Non-registered message passed into MessagePump");
        }
        _messageDispatchBuffer.add(message);
    }

    /**
     * Allows you to send a message without having a hard reference to the Message object
     * you want to send. Instead the message pump will look it up for you.
     * @param message message to send
     */
    public void sendMessage(String message)
    {
        sendMessage(getRegisteredMessage(message));
    }

    /**
     * If you are not the engine then it is best not to call this
     */
    public void dispatchMessages()
    {
        for (Message msg : _messageDispatchBuffer)
        {
            LinkedList<MessageHandler> interested = _registeredHandlers.get(msg);
            for (MessageHandler handler : interested)
            {
                handler.handleMessage(msg);
            }
        }
        // Make sure we get rid of all recently-dispatched messages
        _messageDispatchBuffer.clear();
    }
}
