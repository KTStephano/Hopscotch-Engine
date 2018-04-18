package application;

import engine.*;
import javafx.scene.paint.Color;

import java.util.Random;

// lol
public class ExampleApplication implements ApplicationEntryPoint {
    @Override
    public void init() {
        System.out.println("Initialized");
        new Rectangle2D(10,10,100,100,1).addToWorld();
        new Circle2D(100,100,100, 100, 1).addToWorld();
        new Text2D("test", 200, 200, 100, 55, 1).addToWorld();
        Rectangle2D rec = new Rectangle2D(10,10,100,100,1);
        rec.setColor(Color.BLACK);
        rec.setAccelerationXY(100.0, 0.0);
        rec.addToWorld();
        Camera camera = new Camera();
        camera.attachToEntity(rec);
        //camera.setAsMainCamera();

        Random rng = new Random();
        int worldWidth = Engine.getConsoleVariables().find(Constants.WORLD_WIDTH).getcvarAsInt();
        int worldHeight = Engine.getConsoleVariables().find(Constants.WORLD_HEIGHT).getcvarAsInt();
        for (int i = 0; i < 10000; ++i) {
            Rectangle2D rectangle = new Rectangle2D(rng.nextInt(worldWidth), rng.nextInt(worldHeight), 100, 100, 10);
            rectangle.setColor(Color.BLUE);
            rectangle.addToWorld();
            rectangle.setSpeedXY(rng.nextDouble() * 100, 0.0);
        }

        new PulseEntity() {
            {
                Engine.getMessagePump().sendMessage(new Message(Constants.ADD_PULSE_ENTITY, this));
            }

            @Override
            public void pulse(double deltaSeconds) {
                System.out.println(deltaSeconds);
            }
        };
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        ExampleApplication app = new ExampleApplication();
        EngineLoop.start(app, args);
    }
}
