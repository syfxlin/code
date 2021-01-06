package me.ixk.design_pattern.state;

/**
 * @author Otstar Lin
 * @date 2021/1/6 下午 7:43
 */
public class Sleeping implements State {

    @Override
    public String eat() {
        return "起床，去吃饭";
    }

    @Override
    public String sleep() {
        return "已经在睡觉了";
    }

    @Override
    public String fight() {
        return "起床，去打豆豆";
    }
}
