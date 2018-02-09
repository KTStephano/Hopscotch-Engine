package engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Represents the window for the game/simulation
 *
 * @author Justin Hall
 */
public class Window implements MessageHandler {
    private Stage _stage;
    private Canvas _canvas;
    private Scene _jfxScene;
    private GraphicsContext _gc;
    private boolean _isFullscreen = false;
    private int _width = 1024;
    private int _height = 768;
    private String _title = "";

    public int getWidth()
    {
        return _width;
    }

    public int getHeight()
    {
        return _height;
    }

    public boolean isFullscreen()
    {
        return _isFullscreen;
    }

    public Scene getJFXScene()
    {
        return _jfxScene;
    }

    public GraphicsContext init(Stage stage)
    {
        ConsoleVariables cvars = Singleton.engine.getConsoleVariables();
        _isFullscreen = Boolean.parseBoolean(cvars.find("FULLSCREEN").getcvarValue());
        _width = Integer.parseInt(cvars.find("SCR_WIDTH").getcvarValue());
        _height = Integer.parseInt(cvars.find("SCR_HEIGHT").getcvarValue());
        Singleton.engine.getMessagePump().signalInterest("SCR_WIDTH_WAS_CHANGED", this);
        Singleton.engine.getMessagePump().signalInterest("SCR_HEIGHT_WAS_CHANGED", this);
        Singleton.engine.getMessagePump().signalInterest("FULLSCREEN_WAS_CHANGED", this);
        stage.setFullScreen(_isFullscreen);
        if (_isFullscreen)
        {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            _width = (int)screenSize.getWidth();
            _height = (int)screenSize.getHeight();
        }
        stage.setResizable(false);
        stage.setTitle(_title);
        _stage = stage;
        Group root = new Group();
        _canvas = new Canvas(_width, _height);
        root.getChildren().add(_canvas);
        _jfxScene = new Scene(root);
        stage.setScene(_jfxScene);
        stage.show();
        _gc = _canvas.getGraphicsContext2D();
        return _gc;
    }

    @Override
    public void handleMessage(Message message) {
        // If the window width/height were changed then we need to deal with it
        if (message.getMessageName().equals("SCR_WIDTH_WAS_CHANGED") ||
                message.getMessageName().equals("SCR_HEIGHT_WAS_CHANGED")) {
            _width = (int) _jfxScene.getWidth();
            _height = (int) _jfxScene.getHeight();
            _canvas.setWidth(_width);
            _canvas.setHeight(_height);
        }
    }
}
