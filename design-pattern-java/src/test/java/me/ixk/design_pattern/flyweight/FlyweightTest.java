package me.ixk.design_pattern.flyweight;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/1 下午 3:57
 */
class FlyweightTest {

    @Test
    void flyweight() {
        final Color white = Color.valueOf(255, 255, 255);
        assertEquals("rgb(255, 255, 255)", white.asString());
        final Color black = Color.valueOf(0, 0, 0);
        assertEquals("rgb(0, 0, 0)", black.asString());

        assertSame(white, Color.valueOf(255, 255, 255));
        assertSame(black, Color.valueOf("0 0 0"));
    }
}
