package engine;

import com.sun.xml.internal.ws.resources.HandlerMessages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Generic button class which can be added and removed from the window
 */
public class UIButton implements UIElement {
    private int _x, _y;
    private Button _button;

    /**
     * Creates a new button
     * @param text text to be displayed on the button
     * @param xlocation x location on the screen
     * @param ylocation y location on the screen
     */
    public UIButton(String text, int xlocation, int ylocation)
    {
        _button = new Button();
        _button.setVisible(true);
        setXY(xlocation, ylocation);
        setText(text);
    }

    public void setVisible(boolean value)
    {
        _button.setVisible(true);
    }

    public String  toString()
    {
        return _button.toString();
    }

    public void setStyle(String style)
    {
        _button.setStyle(style);
    }

    public void setWidthHeight(int width, int height)
    {
        _button.setPrefWidth(width);
        _button.setPrefHeight(height);
    }

    public void setText(String text)
    {
        _button.setText(text);
    }

    //public void setGraphic(ImageView view) {_button.setGraphic(view);}

    public void setXY(int x, int y)
    {
        _x = x;
        _y = y;
        _button.setLayoutX(x);
        _button.setLayoutY(y);
    }

    /**
     * Determines which method to call when the button is pressed down
     * @param callback Callback object to call "handleCallback" on
     */
    public void setOnButtonPressed(Callback callback)
    {
        _button.setOnAction(e -> callback.handleCallback());
    }

    public void setDisabled(boolean value)
    {
        _button.setDisable(value);
    }

    public void addToWindow()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, _button));
        //Singleton.engine._window._stack.getChildren().add(_button);
    }

    public void removeFromWindow()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_UI_ELEMENT, _button));
    }

    public void toggleHide(boolean value)
    {
        _button.setVisible(value);
    }
}
