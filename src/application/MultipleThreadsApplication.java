package application;

import engine.*;
import javafx.scene.paint.Color;

import java.util.HashSet;

class SquareTest extends RenderEntity {
    SquareTest(int x, int y) {
        setWidthHeight(10, 10);
        setColor(Color.BLACK);
        setLocationXYDepth(x, y, 1);
    }

    @Override
    public void pulse(double deltaSeconds) {
    }

    @Override
    public void onActorOverlapped(Actor actor, HashSet<Actor> actors) {

    }
}

/**
 * Adds 10 SquareTest objects to the world and then removes itself from the engine
 */
class Logic implements LogicEntity {
    private int num = 10;
    @Override
    public void process(double deltaSeconds) {
        if (num == 0) {
            Engine.getMessagePump().sendMessage(new Message(Constants.REMOVE_LOGIC_ENTITY, this));
            return;
        }
        --num;
        SquareTest test = new SquareTest(num * 200, num * 200);
        test.addToWorld();
    }
}

/**
 * Spawns 100 Logic Entities (virtual threads)
 */
public class MultipleThreadsApplication implements ApplicationEntryPoint{
    @Override
    public void init() {
        System.out.println("Initialized");
        for (int i = 0; i < 100; ++i) {
            Logic logic = new Logic();
            Engine.getMessagePump().sendMessage(new Message(Constants.ADD_LOGIC_ENTITY, logic));
        }
    }

    @Override
    public void shutdown() {

    }

    public static void main(String[] args) {
        ApplicationEntryPoint app = new MultipleThreadsApplication();
        EngineLoop.start(app, args);
        EngineLoop.exit(); // Make sure to call this or shutdown will hang!
    }
}
