package engine;

import application.ApplicationEntryPoint;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedList;

public class Engine extends Application implements PulseEntity, MessageHandler {
    private HashSet<PulseEntity> _pulseEntities;
    private ApplicationEntryPoint _application;
    private MessagePump _messageSystem;
    private ConsoleVariables _cvarSystem;
    private Window _window;
    private int _maxFrameRate;
    private long _lastFrameTimeMS;
    private boolean _isRunning = false;

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

    @Override
    public void handleMessage(Message message) {
        switch(message.getMessageName())
        {
            case Singleton.ADD_PULSE_ENTITY:
                _registerPulseEntity((PulseEntity)message.getMessageData());
                break;
            case Singleton.REMOVE_PULSE_ENTITY:
                _deregisterPulseEntity((PulseEntity)message.getMessageData());
                break;
        }
    }

    public void shutdown()
    {
        _isRunning = false;
        _application.shutdown();
    }

    private void _init(Stage stage)
    {
        Singleton.engine = this; // Make sure this gets set before everything else
        _messageSystem = new MessagePump();
        // Make sure we register all of the message types
        _messageSystem.registerMessage(new Message(Singleton.ADD_PULSE_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.REMOVE_PULSE_ENTITY));
        _messageSystem.registerMessage(new Message(Singleton.ADD_UI_ELEMENT));
        _messageSystem.registerMessage(new Message(Singleton.REMOVE_UI_ELEMENT));
        _messageSystem.registerMessage(new Message(Singleton.SET_FULLSCREEN));
        _messageSystem.registerMessage(new Message(Singleton.SET_SCR_HEIGHT));
        _messageSystem.registerMessage(new Message(Singleton.SET_SCR_WIDTH));
        _messageSystem.signalInterest(Singleton.ADD_PULSE_ENTITY, this);
        _messageSystem.signalInterest(Singleton.REMOVE_PULSE_ENTITY, this);
        _pulseEntities = new HashSet<>();
        _cvarSystem = new ConsoleVariables();
        _window = new Window();
        _application = new ApplicationEntryPoint();
        _cvarSystem.registerVariable(new ConsoleVariable("MAX_FRAMERATE", "60"));
        _isRunning = true;
        _lastFrameTimeMS = System.currentTimeMillis();
        _window.init(stage);
        _application.init();
    }

    /**
     * Registers a pulse entity, which is an entity which must be updated once
     * per engine/simulation frame.
     * @param entity entity to update every frame
     */
    private void _registerPulseEntity(PulseEntity entity)
    {
        _pulseEntities.add(entity);
    }

    private void _deregisterPulseEntity(PulseEntity entity)
    {
        _pulseEntities.remove(entity);
    }

    private void _loadEngineConfig(String engineCfgFile)
    {
        try
        {
            FileReader fileReader = new FileReader(engineCfgFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null)
            {
                line = line.replaceAll(" ", "");
                System.out.println(line);
                String variable = "";
                String value = "";
                boolean isReadingValue = false;
                for (int i = 1; i < line.length(); ++i)
                {
                    char c = line.charAt(i);
                    if (c == '=')
                    {
                        isReadingValue = true;
                        continue;
                    }
                    if (isReadingValue) value += c;
                    else variable += c;
                }
                if (_cvarSystem.contains(variable)) _cvarSystem.find(variable).setValue(value);
                else _cvarSystem.registerVariable(new ConsoleVariable(variable, value));
            }
        }
        catch (Exception e)
        {
            System.out.println("Unable to load " + engineCfgFile);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
