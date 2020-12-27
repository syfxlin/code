package me.ixk.design_pattern.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/27 下午 4:02
 */
class BridgeTest {

    @Test
    void bridge() {
        assertEquals(
            "制作了一个加盐的饼干",
            new Cookie(new SaltCookieType()).make()
        );
        assertEquals(
            "制作了一个加糖的饼干",
            new Cookie(new SugarCookieType()).make()
        );
    }
}
