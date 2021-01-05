package me.ixk.design_pattern.chain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/5 下午 3:20
 */
class ChainTest {

    @Test
    void chain() {
        final HandlerChain chain = new HandlerChain(
            new Handler[] { new Handler1(), new Handler2() }
        );
        assertEquals("valuehandler1handler2", chain.next("value"));
    }
}
