package me.ixk.design_pattern.chain;

/**
 * @author Otstar Lin
 * @date 2021/1/5 下午 3:14
 */
public interface Handler {
    String handle(String value, HandlerChain chain);
}
