package me.ixk.design_pattern.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/23 上午 11:35
 */
class BuilderTest {

    @Test
    void build() {
        final UserBuilder user = UserBuilder
            .builder()
            .name("syfxlin")
            .age(20)
            .build();
        assertEquals("syfxlin", user.getName());
        assertEquals(20, user.getAge());
    }
}
