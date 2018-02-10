package application;

import engine.*;

import java.util.LinkedList;

/**
 * This is the only part of the application that the engine
 * directly and explicitly knows about. It only guarantees
 * two things: it will call init() at the start and shutdown()
 * at the end. If you need anything else you must set it up
 * with the engine
 */
public class ApplicationEntryPoint {
    UIButton button = new UIButton("hello", 50, 50);
    LinkedList<Integer> list = new LinkedList<>();
    boolean t = false;
    /**
     * Initializes the application
     */
    public void init()
    {
        //button = new UIButton("hello", 50, 50);
        button.setWidthHeight(100,100);
        //button.addToWindow();
        Singleton.engine.getMessagePump().registerMessage(new Message("test"));
        Singleton.engine.getMessagePump().signalInterest("test", new MessageHandler() {
            @Override
            public void handleMessage(Message message) {
                ((LinkedList<Integer>)message.getMessageData()).add(100);
                System.out.println("SIZE NOW = " + list.size());
            }
        });
        System.out.println("SIZE = " + list.size());
        Singleton.engine.getMessagePump().sendMessage(new Message("test", list));
    }

    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {
    }
}
