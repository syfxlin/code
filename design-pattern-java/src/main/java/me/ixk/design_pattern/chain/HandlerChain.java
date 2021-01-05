package me.ixk.design_pattern.chain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Otstar Lin
 * @date 2021/1/5 下午 3:15
 */
public class HandlerChain {

    private final Handler[] handlers;
    private final AtomicInteger index = new AtomicInteger(0);

    public HandlerChain(final Handler[] handlers) {
        this.handlers = handlers;
    }

    public String next(final String value) {
        final int index = this.index.getAndIncrement();
        if (index >= this.handlers.length) {
            return value;
        }
        final Handler handler = this.handlers[index];
        return handler.handle(value, this);
    }
}
