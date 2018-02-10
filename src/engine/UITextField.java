package engine;

import javafx.scene.control.TextField;

/**
 * Represents a text field that the user can interact with. There are
 * no listeners attached to it, so you may need to have a button
 * accompany it to let the GUI know that the user is done editing it and
 * is ready to commit their changes.
 *
 * @author Justin Hall
 */
public class UITextField implements UIElement {
    private TextField _text;

    public UITextField(String startText, int xlocation, int ylocation)
    {
        _text = new TextField(startText);
        _text.setVisible(true);
        setXY(xlocation, ylocation);
    }

    /**
     * Gets the text that has been entered into the field
     * @return current set text
     */
    public String getText()
    {
        return _text.getText();
    }

    public void setXY(int xlocation, int ylocation)
    {
        _text.setLayoutX(xlocation);
        _text.setLayoutY(ylocation);
    }

    public void setWidthHeight(int width, int height)
    {
        _text.setPrefWidth(width);
        _text.setPrefHeight(height);
    }

    @Override
    public void addToWindow() {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, _text));
    }

    @Override
    public void removeFromWindow() {
        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.REMOVE_UI_ELEMENT, _text));
    }

    @Override
    public void setVisible(boolean value) {
        _text.setVisible(value);
    }
}
