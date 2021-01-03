package me.ixk.design_pattern.template;

/**
 * @author Otstar Lin
 * @date 2021/1/3 下午 9:41
 */
public abstract class Animal {

    protected abstract String eat();

    protected abstract String run();

    public String adopt() {
        return this.eat() + " " + this.run();
    }
}
