package me.ixk.design_pattern.mediator;

/**
 * @author Otstar Lin
 * @date 2021/1/13 下午 10:37
 */
public class Dialog implements Mediator {

    private Button button;
    private Input input;

    public Button getButton() {
        return button;
    }

    public void setButton(final Button button) {
        this.button = button;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(final Input input) {
        this.input = input;
    }

    @Override
    public String handle(final String event, final Object data) {
        if (event.equals("click")) {
            return button.click((Integer) data);
        } else if (event.equals("input")) {
            return input.input((String) data);
        }
        return null;
    }
}
