package me.ixk.design_pattern.state;

/**
 * @author Otstar Lin
 * @date 2021/1/6 下午 7:42
 */
public class Eating implements State {

    @Override
    public String eat() {
        return "已经在吃饭了";
    }

    @Override
    public String sleep() {
        return "去睡觉";
    }

    @Override
    public String fight() {
        return "去打豆豆";
    }
}
