package me.ixk.design_pattern.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/4 下午 6:34
 */
class StrategyTest {

    @Test
    void strategy() {
        assertEquals(
            "is a string",
            new Context("This is a string", new SubStrStrategy()).process()
        );
        assertEquals(
            "This is a string concat",
            new Context("This is a string", new ConcatStrategy()).process()
        );
    }
}
