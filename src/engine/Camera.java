package engine;

import engine.math.Vector3;
import javafx.util.Pair;

/**
 * Represents a virtual camera which can be attached
 * to a RenderEntity. This will calculate the transforms
 * necessary to keep the player in focus but move the world
 * around them.
 *
 * If a virtual camera is set to be the focus of the window,
 * what it "sees" will be what is drawn on the screen.
 *
 * If no camera is set then the system will pick some location to
 * focus on.
 *
 * @author Justin Hall
 */
public class Camera {
    private Actor _attachedTo;
    private Vector3 _worldTranslate = new Vector3(0.0);
    private Vector3 _editedEntityLocation = new Vector3(0.0);
    private double _widthScalar = 2;//2.5;
    private double _heightScalar = 2;//3.0;
    private double _manualOffsetX = 0.0; // In case something like the mouse is controlling this
    private double _manualOffsetY = 0.0; // In case something like the mouse is controlling this

    /**
     * Returns the entity that this camera was attached to
     */
    public Actor getEntity()
    {
        return _attachedTo;
    }

    /**
     * Attaches this camera to the given entity
     */
    public void attachToEntity(Actor entity)
    {
        _attachedTo = entity;
    }

    public void incrementManualOffsetX(double amount) {
        _manualOffsetX += amount;
    }

    public void incrementManualOffsetY(double amount) {
        _manualOffsetY += amount;
    }

    public void setManualOffsetXY(double xAmount, double yAmount) {
        _manualOffsetX = xAmount;
        _manualOffsetY = yAmount;
    }

    public double getManualOffsetX() {
        return _manualOffsetX;
    }

    public double getManualOffsetY() {
        return _manualOffsetY;
    }

    public Pair<Double, Double> getManualOffsetXY() {
        return new Pair<>(getManualOffsetX(), getManualOffsetY());
    }

    /**
     * Sets this camera to be the main camera for
     * the entire scene, meaning what it sees will
     * be rendered on the screen
     */
    public void setAsMainCamera()
    {
        Engine.getMessagePump().sendMessage(new Message(Constants.SET_MAIN_CAMERA, this));
    }

    /**
     * Returns the modified entity location after it has been transformed
     * to sit in the center of the screen.
     */
    public Vector3 getEntityLocation()
    {
        if (_attachedTo == null)
        {
            _editedEntityLocation.setXYZ(0.0, 0.0, 0.0);
            return _editedEntityLocation;
        }
        int scrWidth = Engine.getConsoleVariables().find(Constants.SCR_WIDTH).getcvarAsInt();
        int scrHeight = Engine.getConsoleVariables().find(Constants.SCR_HEIGHT).getcvarAsInt();
        _editedEntityLocation.setXYZ(scrWidth / _widthScalar, scrHeight / _heightScalar, 0.0);
        return _editedEntityLocation;
    }

    /**
     * This returns a vector representing what every object in the world
     * needs to get translated by to still be correct in their distances
     * from the main entity's location. This is because the main entity
     * who is being followed by a camera who is in focus is _always_ drawn
     * at the center of the screen, no matter where in the world they are.
     */
    public Vector3 getWorldTranslate()
    {
        if (_attachedTo == null)
        {
            _worldTranslate.setXYZ(_manualOffsetX, _manualOffsetY, 0.0);
            return _worldTranslate;
        }
        double scrWidth = Engine.getConsoleVariables().find(Constants.SCR_WIDTH).getcvarAsInt();
        double scrHeight = Engine.getConsoleVariables().find(Constants.SCR_HEIGHT).getcvarAsInt();
        double scrWidthModified = scrWidth / _widthScalar;
        double scrHeightModified = scrHeight / _heightScalar;
        double worldWidth = Engine.getConsoleVariables().find(Constants.WORLD_WIDTH).getcvarAsFloat();
        double worldHeight = Engine.getConsoleVariables().find(Constants.WORLD_HEIGHT).getcvarAsFloat();
        double worldStartX = Engine.getConsoleVariables().find(Constants.WORLD_START_X).getcvarAsFloat();
        double worldStartY = Engine.getConsoleVariables().find(Constants.WORLD_START_Y).getcvarAsFloat();
        double worldEndX = worldWidth + worldStartX;
        double worldEndY = worldHeight + worldStartY;
        double locX = _attachedTo.getLocationX();
        double locY = _attachedTo.getLocationY();
        //double modLocX = locX;// < 0 ? -locX : locX;
        //double modLocY = locY;// < 0 ? -locY : locY;
        double newLocX = locX - scrWidthModified - _manualOffsetX;
        double newLocY = locY - scrHeightModified - _manualOffsetY;

        //if (newLocX > (worldEndX - scrWidth)) newLocX = worldEndX - scrWidth;
        //else if (newLocX < worldStartX) newLocX = worldStartX;

        //if (newLocY > (worldStartY - scrHeight)) newLocY = worldStartY - scrHeight;
        //else if (newLocY < worldStartY) newLocY = worldStartY;
        newLocX *= -1;
        newLocY *= -1;
        _worldTranslate.setXYZ(newLocX, newLocY, 1.0);
        return _worldTranslate;
    }
}
