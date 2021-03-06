package me.ixk.design_pattern.interpreter;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:45
 */
public class Or implements Expression {

    private final Expression left;
    private final Expression right;

    public Or(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(final Context ctx) {
        return left.interpret(ctx) || right.interpret(ctx);
    }
}
