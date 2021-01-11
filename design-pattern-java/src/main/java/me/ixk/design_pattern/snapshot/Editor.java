package me.ixk.design_pattern.snapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Otstar Lin
 * @date 2021/1/9 下午 5:44
 */
public class Editor {

    private final List<TextSnapshot> undoes = new ArrayList<>();
    private final Text text = new Text();

    public String getText() {
        return text.getText();
    }

    public void append(final String s) {
        text.append(s);
        undoes.add(text.snapshot());
    }

    public void undo() {
        final TextSnapshot snapshot = undoes.remove(0);
        if (snapshot != null) {
            text.restore(snapshot);
        }
    }
}
