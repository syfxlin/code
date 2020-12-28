package me.ixk.design_pattern.decorator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/28 下午 1:45
 */
class DecoratorTest {

    @Test
    void decorator() {
        final Cookie normalCookie = new NormalCookie();
        final Cookie saltCookie = new SaltCookie(normalCookie);
        final Cookie sugarCookie = new SugarCookie(normalCookie);
        final Cookie cookie = new SaltCookie(new SugarCookie(normalCookie));
        assertEquals("制作了一个普通的饼干", normalCookie.make());
        assertEquals("制作了一个普通的饼干加盐", saltCookie.make());
        assertEquals("制作了一个普通的饼干加糖", sugarCookie.make());
        assertEquals("制作了一个普通的饼干加糖加盐", cookie.make());
    }
}
