package me.ixk.design_pattern.state;

/**
 * @author Otstar Lin
 * @date 2021/1/6 下午 7:44
 */
public class FightingDouDou implements State {

    @Override
    public String eat() {
        return "去吃饭";
    }

    @Override
    public String sleep() {
        return "去睡觉";
    }

    @Override
    public String fight() {
        return "已经在打豆豆了";
    }
}
