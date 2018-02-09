package application;

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
    }

    /**
     * Tells the application we need to shutdown
     */
    public void shutdown()
    {
    }
}
