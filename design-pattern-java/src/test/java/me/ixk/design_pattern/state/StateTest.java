package me.ixk.design_pattern.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/6 下午 7:48
 */
class StateTest {

    @Test
    void state() {
        Context context = new Context(new Eating());
        assertEquals("已经在吃饭了", context.eat());
        assertEquals("起床，去打豆豆", context.fight());
        assertEquals("去睡觉", context.sleep());
    }
}
