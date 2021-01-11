package me.ixk.design_pattern.snapshot;

/**
 * @author Otstar Lin
 * @date 2021/1/9 下午 5:40
 */
public class Text {

    private final StringBuilder text = new StringBuilder();

    public String getText() {
        return text.toString();
    }

    public void append(final String s) {
        text.append(s);
    }

    public TextSnapshot snapshot() {
        return new TextSnapshot(text.toString());
    }

    public void restore(final TextSnapshot snapshot) {
        text.setLength(0);
        text.append(snapshot.getText());
    }
}
