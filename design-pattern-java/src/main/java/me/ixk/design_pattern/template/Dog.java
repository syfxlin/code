package me.ixk.design_pattern.template;

/**
 * @author Otstar Lin
 * @date 2021/1/3 下午 9:44
 */
public class Dog extends Animal {

    @Override
    protected String eat() {
        return "狗吃食物";
    }

    @Override
    protected String run() {
        return "狗在运动";
    }
}
