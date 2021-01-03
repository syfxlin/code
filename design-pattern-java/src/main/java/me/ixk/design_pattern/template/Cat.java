package me.ixk.design_pattern.template;

/**
 * @author Otstar Lin
 * @date 2021/1/3 下午 9:43
 */
public class Cat extends Animal {

    @Override
    protected String eat() {
        return "猫吃食物";
    }

    @Override
    protected String run() {
        return "猫在运动";
    }
}
