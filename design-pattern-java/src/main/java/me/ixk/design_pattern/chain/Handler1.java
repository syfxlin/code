package me.ixk.design_pattern.chain;

/**
 * @author Otstar Lin
 * @date 2021/1/5 下午 3:18
 */
public class Handler1 implements Handler {

    @Override
    public String handle(final String value, final HandlerChain chain) {
        return chain.next(value + "handler1");
    }
}
