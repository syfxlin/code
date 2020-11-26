/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day7;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 异步调用返回值
 *
 * @author Otstar Lin
 * @date 2020/11/26 上午 11:21
 */
public class AsyncResult<V> implements Future<V> {
    private final V value;
    private final Throwable exception;

    public AsyncResult(V value) {
        this(value, null);
    }

    public AsyncResult(V value, Throwable exception) {
        this.value = value;
        this.exception = exception;
    }

    public static <V> AsyncResult<V> of(V value) {
        return new AsyncResult<>(value);
    }

    public static <V> AsyncResult<V> of(Throwable exception) {
        return new AsyncResult<>(null, exception);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (this.exception != null) {
            throw (
                this.exception instanceof ExecutionException
                    ? (ExecutionException) this.exception
                    : new ExecutionException(this.exception)
            );
        }
        return this.value;
    }

    @Override
    public V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
        return get();
    }
}
