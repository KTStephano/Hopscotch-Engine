package application;

import engine.*;
import javafx.scene.paint.Color;

class SquareTest extends RenderEntity {
    SquareTest(int x, int y) {
        setWidthHeight(10, 10);
        setColor(Color.BLACK);
        setLocationXYDepth(x, y, 1);
    }

    @Override
    public void pulse(double deltaSeconds) {
    }
}

class Logic implements LogicEntity {
    private int num = 10;
    private long ms = System.currentTimeMillis();
    @Override
    public void process() {
        long newMs = System.currentTimeMillis();
        double deltaSeconds = (newMs - ms) / 1000.0;
        System.out.println(deltaSeconds);
        ms = newMs;
        if (num == 0) {
            Engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_LOGIC_ENTITY, this));
            return;
        }
        --num;
        SquareTest test = new SquareTest(num * 200, num * 200);
        test.addToWorld();
    }
}

// lol
public class ExampleApp implements ApplicationEntryPoint{
    @Override
    public void init() {
        System.out.println("Initialized");
        for (int i = 0; i < 100; ++i) {
            Logic logic = new Logic();
            Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_LOGIC_ENTITY, logic));
        }
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        ApplicationEntryPoint app = new ExampleApp();
        EngineLoop loop = new EngineLoop();
        loop.start(app, args);
    }
}