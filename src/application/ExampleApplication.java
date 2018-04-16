package application;

import engine.*;

// lol
public class ExampleApplication implements ApplicationEntryPoint {
    @Override
    public void init() {
        System.out.println("Initialized");
        new Rectangle2D(10,10,100,100,1).addToWorld();
        new Circle2D(100,100,100, 100, 1).addToWorld();
        new Text2D("test", 200, 200, 100, 55, 1).addToWorld();
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        ExampleApplication app = new ExampleApplication();
        EngineLoop loop = new EngineLoop();
        loop.start(app, args);
    }
}
