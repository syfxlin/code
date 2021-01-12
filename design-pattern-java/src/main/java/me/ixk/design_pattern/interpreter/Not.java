package me.ixk.design_pattern.interpreter;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:45
 */
public class Not implements Expression {

    private final Expression exp;

    public Not(Expression exp) {
        this.exp = exp;
    }

    @Override
    public boolean interpret(Context ctx) {
        return !exp.interpret(ctx);
    }
}
