package application;
import engine.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class SimpleZoomTest implements ApplicationEntryPoint {

    /**
     * Gives us the ability to scroll around using the mouse
     */
    private class CameraController extends MouseInputComponent implements MessageHandler {
        private double _scrollAmount = 1.0;
        private double _deltaScroll = 1.4;
        private double _lastMouseX = 0.0;
        private double _lastMouseY = 0.0;
        private double _currentXOffset = 0.0;
        private double _currentYOffset = 0.0;
        private boolean _isPressed = false;

        public CameraController() {
            Engine.getMessagePump().signalInterest(Constants.CAMERA_OFFSET_CHANGED, this);
            Engine.getMessagePump().signalInterest(Constants.CAMERA_ZOOM_CHANGED, this);
        }

        @Override
        public void mousePressedDown(double mouseX, double mouseY, MouseButtonTypes button) {
            _isPressed = true;
        }

        @Override
        public void mouseReleased(double mouseX, double mouseY, MouseButtonTypes button) {
            _isPressed = false;
        }

        @Override
        public void mouseMoved(double amountX, double amountY, double mouseX, double mouseY) {
            _lastMouseX = mouseX;
            _lastMouseY = mouseY;
            if (_isPressed) {
                Engine.getMessagePump().sendMessage(new Message(Constants.INCREMENT_CAMERA_X_OFFSET, amountX * 5.0));
                Engine.getMessagePump().sendMessage(new Message(Constants.INCREMENT_CAMERA_Y_OFFSET, amountY * 5.0));
            }
        }

        // @see https://stackoverflow.com/questions/13316481/zooming-into-a-window-based-on-the-mouse-position
        @Override
        public void scrolled(double direction) {
            boolean zoomIn = direction == 1.0;
            double scale = zoomIn ? _deltaScroll : 1 / _deltaScroll;
            double screenWidth = Engine.getConsoleVariables().find(Constants.SCR_WIDTH).getcvarAsFloat();
            double screenHeight = Engine.getConsoleVariables().find(Constants.SCR_HEIGHT).getcvarAsFloat();
            double originX = _currentXOffset;// / 2;
            double originY = _currentYOffset;// / 2;

            double oldScroll = _scrollAmount;
            _scrollAmount *= scale;
            //_scrollAmount = Math.max(0.5, Math.min(5.0, _scrollAmount));
            //if (oldScroll != _scrollAmount && !(_lastMouseX == originX || _lastMouseY == originY)) {
            double offsetX = (_lastMouseX - originX) * (1.0 - scale);
            double offsetY = (_lastMouseY - originY) * (1.0 - scale);
            //System.out.println(offsetX + " " + offsetY);
            _currentXOffset = _currentXOffset + offsetX;
            _currentYOffset = _currentYOffset + offsetY;
            Engine.getMessagePump().sendMessage(new Message(Constants.SET_CAMERA_X_OFFSET, _currentXOffset));
            Engine.getMessagePump().sendMessage(new Message(Constants.SET_CAMERA_Y_OFFSET, _currentYOffset));
            Engine.getMessagePump().sendMessage(new Message(Constants.SET_CAMERA_ZOOM, _scrollAmount));
            //}
        }

        @Override
        public void processMouseCollisionResponse(ArrayList<Actor> actors) {

        }

        @Override
        public void handleMessage(Message message) {
            switch (message.getMessageName()) {
                case Constants.CAMERA_OFFSET_CHANGED: {
                    Pair<Double, Double> offsets = (Pair<Double, Double>) message.getMessageData();
                    _currentXOffset = offsets.getKey();
                    _currentYOffset = offsets.getValue();
                    break;
                }
            }
        }
    }

    @Override
    public void init() {
        System.out.println("Initialized");
        Rectangle2D collideRect = new Rectangle2D(250,250,100,100,1);
        collideRect.addToWorld(); // Make sure to add it!
        new Circle2D(10,10,100, 100, 1).addToWorld();
        new Text2D("test", 100, 100, 100, 55, 1).addToWorld();
        Rectangle2D rec = new Rectangle2D(10,10,100,100,2);
        rec.setColor(Color.BLACK);
        rec.setAccelerationXY(100.0, 0.0);
        rec.addToWorld();
        new SimpleZoomTest.CameraController().enableMouseInputComponent(); // Camera still needs to be enabled

        Random rng = new Random();
        // Pull the width and height from their corresponding console variables
        int worldWidth = Engine.getConsoleVariables().find(Constants.WORLD_WIDTH).getcvarAsInt();
        int worldHeight = Engine.getConsoleVariables().find(Constants.WORLD_HEIGHT).getcvarAsInt();
        // Add 3500 moving rectangles to the world
        for (int i = 0; i < 3500; ++i) {
            Rectangle2D rectangle = new Rectangle2D(rng.nextInt(worldWidth), rng.nextInt(worldHeight), 2, 2, 1);
            rectangle.setColor(Color.BLUE);
            rectangle.addToWorld();
            rectangle.setSpeedXY(rng.nextDouble() * 100, 0.0);
        }
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        SimpleZoomTest app = new SimpleZoomTest();
        EngineLoop.start(app, args);
        EngineLoop.exit(); // Make sure to call this or shutdown will hang!
    }
}
