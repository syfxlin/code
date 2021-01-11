package me.ixk.design_pattern.snapshot;

/**
 * @author Otstar Lin
 * @date 2021/1/9 下午 5:42
 */
public class TextSnapshot {

    private final String text;

    public TextSnapshot(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
