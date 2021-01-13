package me.ixk.design_pattern.mediator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/13 下午 10:40
 */
class MediatorTest {

    @Test
    void mediator() {
        final Dialog dialog = new Dialog();
        dialog.setButton(new Button());
        dialog.setInput(new Input());
        assertEquals("click1", dialog.handle("click", 1));
        assertEquals("input", dialog.handle("input", "input"));
    }
}
