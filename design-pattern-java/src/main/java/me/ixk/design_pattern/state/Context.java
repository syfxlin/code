package me.ixk.design_pattern.state;

/**
 * @author Otstar Lin
 * @date 2021/1/6 下午 7:36
 */
public class Context {

    private State state;

    public Context(final State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public String eat() {
        final String eat = this.state.eat();
        this.state = new Sleeping();
        return eat;
    }

    public String sleep() {
        final String sleep = this.state.sleep();
        this.state = new FightingDouDou();
        return sleep;
    }

    public String fight() {
        final String fight = this.state.fight();
        this.state = new Eating();
        return fight;
    }
}
