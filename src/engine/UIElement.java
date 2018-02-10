package engine;

/**
 * Interface implemented by all UI elements such as UIButton, UILabel, etc.
 */
public interface UIElement {
    /**
     * Adds self to the main simulation window
     */
    void addToWindow();

    /**
     * Removes self from the main simulation window
     */
    void removeFromWindow();

    void setVisible(boolean value);
}
