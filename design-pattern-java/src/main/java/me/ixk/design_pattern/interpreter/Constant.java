package me.ixk.design_pattern.interpreter;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:38
 */
public class Constant implements Expression {

    private final boolean value;

    public Constant(final boolean value) {
        this.value = value;
    }

    @Override
    public boolean interpret(final Context ctx) {
        return value;
    }

    public boolean isValue() {
        return value;
    }
}
