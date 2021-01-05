package me.ixk.design_pattern.chain;

/**
 * @author Otstar Lin
 * @date 2021/1/5 下午 3:19
 */
public class Handler2 implements Handler {

    @Override
    public String handle(final String value, final HandlerChain chain) {
        return chain.next(value + "handler2");
    }
}
