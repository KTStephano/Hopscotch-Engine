package engine;

import javafx.scene.control.Label;

/**
 * A label is simply a UI element with some text attached to it - non-clickable
 */
public class UILabel implements UIElement {
    private Label _label;

    public UILabel(String text, int xlocation, int ylocation)
    {
        _label = new Label(text);
        _label.setVisible(true);
        setXY(xlocation, ylocation);
    }

    public void setText(String text)
    {
        _label.setText(text);
    }

    public void setXY(int x, int y)
    {
        _label.setLayoutX(x);
        _label.setLayoutY(y);
    }

    public void setWidthHeight(int width, int height)
    {
        _label.setPrefWidth(width);
        _label.setPrefHeight(height);
    }

    public void setVisible(boolean value)
    {
        _label.setVisible(value);
    }

    public void addToWindow()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, _label));
    }

    public void removeFromWindow()
    {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_UI_ELEMENT, _label));
    }
}
