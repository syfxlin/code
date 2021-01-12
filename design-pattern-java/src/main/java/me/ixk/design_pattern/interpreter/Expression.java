package me.ixk.design_pattern.interpreter;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:30
 */
public interface Expression {
    boolean interpret(Context ctx);
}
