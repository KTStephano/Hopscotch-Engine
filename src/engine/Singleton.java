package engine;

/**
 * All absolutely critical global variables, such as the engine
 */
public class Singleton {
    // If you are not the Engine class, don't set this variable to be any other Engine object
    // or everything will probably break - the engine creates itself and sets this at startup
    public static Engine engine;

    /**
     * The following are message types that the message pump is
     * guaranteed to recognize
     */
    // Sets the width of the screen
    public static final String SET_SCR_WIDTH = "SET_SCR_WIDTH";
    public static final String SET_SCR_HEIGHT = "SET_SCR_WIDTH";
    public static final String SET_FULLSCREEN = "SET_FULLSCREEN";
    // Adds a UI element to the main window - make sure the data portion of the message
    // is a reference to the UI element
    public static final String ADD_UI_ELEMENT = "ADD_UI_ELEMENT";
    public static final String REMOVE_UI_ELEMENT = "REM_UI_ELEMENT";

    // Adds a Pulse Entity to the engine, which is an entity that needs to update
    // as frequently as possible. Be sure the include the object as the data portion of
    // the Message, with the Object implementing the "MessageHandler" interface.
    public static final String ADD_PULSE_ENTITY = "ADD_PULSE_ENTITY";
    // Removes the pulse entity (which should be included as the data portion of the message).
    public static final String REMOVE_PULSE_ENTITY = "REM_PULSE_ENTITY";
}
