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
    private int _maxFrameRate;
    private long _lastFrameTimeMS;
    private boolean _isRunning = false;

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
                if (!_isRunning) return;
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
        for (PulseEntity entity : _pulseEntities)
        {
            entity.pulse(deltaSeconds);
        }
    }

    public void shutdown1()
    {
        _isRunning = false;
    }

    private void _init(Stage stage)
    {
        _isRunning = true;
        _lastFrameTimeMS = System.currentTimeMillis();
        _application.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
