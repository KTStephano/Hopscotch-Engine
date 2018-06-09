package application;

import engine.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class SimpleCameraApplication implements ApplicationEntryPoint {
    private class CameraController extends MouseInputComponent {
        boolean pressedDown = false;

        @Override
        public void mousePressedDown(double mouseX, double mouseY, MouseButtonTypes button) {
            pressedDown = true;
        }

        @Override
        public void mouseReleased(double mouseX, double mouseY, MouseButtonTypes button) {
            pressedDown = false;
        }

        @Override
        public void mouseMoved(double amountX, double amountY, double mouseX, double mouseY) {
            if (pressedDown) {
                Engine.getMessagePump().sendMessage(new Message(Constants.INCREMENT_CAMERA_X_OFFSET, amountX*5));
                Engine.getMessagePump().sendMessage(new Message(Constants.INCREMENT_CAMERA_Y_OFFSET, amountY*5));
            }
        }

        @Override
        public void scrolled(double direction) {

        }

        @Override
        public void processMouseCollisionResponse(ArrayList<Actor> actors) {

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
        Camera camera = new Camera();
        camera.attachToEntity(rec); // Attaching the camera allows us to follow the actor pointed to by rec
        new CameraController().enableMouseInputComponent(); // Camera still needs to be enabled
        camera.setAsMainCamera();

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
        SimpleCameraApplication app = new SimpleCameraApplication();
        EngineLoop.start(app, args);
        EngineLoop.exit(); // Make sure to call this or shutdown will hang!
    }
}
