package me.ixk.design_pattern.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import me.ixk.design_pattern.builder.outer_builder.Computer;
import me.ixk.design_pattern.builder.outer_builder.ComputerBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/23 上午 11:35
 */
class BuilderTest {

    @Test
    void inner() {
        final UserBuilder user = UserBuilder
            .builder()
            .name("syfxlin")
            .age(20)
            .build();
        assertEquals("syfxlin", user.getName());
        assertEquals(20, user.getAge());
    }

    @Test
    void outer() {
        final Computer computer = ComputerBuilder
            .builder()
            .cpu("Intel(R) Core(TM) i5-8400 CPU @ 2.80GHz")
            .mainBoard("H370")
            .memory("DDR4 8G x2")
            .disk("Samsung SM961")
            .build();
        assertNotNull(computer);
    }
}
