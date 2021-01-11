package me.ixk.design_pattern.snapshot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/9 下午 5:46
 */
class SnapshotTest {

    @Test
    void snapshot() {
        Editor editor = new Editor();
        editor.append("123");
        editor.append("456");
        assertEquals("123456", editor.getText());
        editor.undo();
        assertEquals("123", editor.getText());
    }
}
