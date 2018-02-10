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
    /**
     * Initializes the application
     */
    public void init()
    {
        // example code
        UIButton button = new UIButton("enter", 135, 100);
        button.setWidthHeight(100, 25);
        button.addToWindow();
        button.setOnButtonPressed(new Callback() {
            @Override
            public void handleCallback() {
                System.out.println("BUTTON PRESSED");
            }
        });

        UILabel label = new UILabel("name: ", 10, 100);
        label.setWidthHeight(100, 25);
        label.addToWindow();

        UITextField field = new UITextField("", 50, 100);
        field.setWidthHeight(75, 25);
        field.addToWindow();

        // Uncomment if you want to cry
        /*
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_PULSE_ENTITY, new PulseEntity() {
            @Override
            public void pulse(double deltaSeconds) {
                System.out.println("cry");
            }
        }));
        */
    }

    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {
    }
}
