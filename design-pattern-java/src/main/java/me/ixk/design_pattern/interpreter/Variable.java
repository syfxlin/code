package me.ixk.design_pattern.interpreter;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:40
 */
public class Variable implements Expression {

    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    public boolean interpret(final Context ctx) {
        return ctx.lookup(this);
    }

    public String getName() {
        return name;
    }
}
