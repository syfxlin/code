package me.ixk.design_pattern.template;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/3 下午 9:44
 */
class TemplateTest {

    @Test
    void template() {
        Animal cat = new Cat();
        assertEquals("猫吃食物 猫在运动", cat.adopt());
        Animal dog = new Dog();
        assertEquals("狗吃食物 狗在运动", dog.adopt());
    }
}
