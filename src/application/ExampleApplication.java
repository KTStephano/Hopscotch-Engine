package application;

import engine.*;
import javafx.scene.paint.Color;

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
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        ExampleApplication app = new ExampleApplication();
        EngineLoop.start(app, args);
    }
}
