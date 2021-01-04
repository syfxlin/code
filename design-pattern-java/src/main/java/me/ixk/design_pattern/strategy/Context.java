package me.ixk.design_pattern.strategy;

/**
 * @author Otstar Lin
 * @date 2021/1/4 下午 6:31
 */
public class Context {

    private String string;
    private Strategy strategy;

    public Context(final String string, final Strategy strategy) {
        this.string = string;
        this.strategy = strategy;
    }

    public String getString() {
        return string;
    }

    public void setString(final String string) {
        this.string = string;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(final Strategy strategy) {
        this.strategy = strategy;
    }

    public String process() {
        return this.strategy.handle(this);
    }
}
