package engine;

import application.ApplicationEntryPoint;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.LinkedList;

public class Engine extends Application implements PulseEntity {
    private HashSet<PulseEntity> _pulseEntities = new HashSet<>();
    private ApplicationEntryPoint _application = new ApplicationEntryPoint();
    private MessagePump _messageSystem = new MessagePump();
    private ConsoleVariables _cvarSystem = new ConsoleVariables();
    private Window _window = new Window();
    private int _maxFrameRate;
    private long _lastFrameTimeMS;
    private boolean _isRunning = false;

    public Window getWindow()
    {
        return _window;
    }

    /**
     * Warning! Do not call the MessagePump's dispatch method!
     *
     * This allows other systems to pass messages/register messages/
     * signal interest in message.
     * @return MessagePump for modification
     */
    public MessagePump getMessagePump()
    {
        return _messageSystem;
    }

    /**
     * Returns the console variable listing for viewing/modification
     */
    public ConsoleVariables getConsoleVariables()
    {
        return _cvarSystem;
    }

    /**
     * Registers a pulse entity, which is an entity which must be updated once
     * per engine/simulation frame.
     * @param entity entity to update every frame
     */
    public void registerPulseEntity(PulseEntity entity)
    {
        _pulseEntities.add(entity);
    }

    @Override
    public void start(Stage stage) {
        // Initialize the engine
        _init(stage);
        // Initialize the game loop
        new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                if (!_isRunning) System.exit(0); // Need to shut the system down
                long currentTimeMS = System.currentTimeMillis();
                double deltaSeconds = (currentTimeMS - _lastFrameTimeMS) / 1000.0;
                // Don't pulse faster than the maximum frame rate
                if (deltaSeconds < (1/60)) return;
                pulse(deltaSeconds);
                _lastFrameTimeMS = currentTimeMS;
            }
        }.start();
    }

    /**
     * Represents the main game/simulation loop
     */
    @Override
    public void pulse(double deltaSeconds) {
        // Make sure we keep the messages flowing
        _messageSystem.dispatchMessages();
        for (PulseEntity entity : _pulseEntities)
        {
            entity.pulse(deltaSeconds);
        }
    }

    public void shutdown()
    {
        _isRunning = false;
        _application.shutdown();
    }

    private void _init(Stage stage)
    {
        _cvarSystem.registerVariable(new ConsoleVariable("MAX_FRAMERATE", "60"));
        Singleton.engine = this; // Make sure this gets set
        _isRunning = true;
        _lastFrameTimeMS = System.currentTimeMillis();
        _application.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
