/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 连接点
 * <p>
 * 存储一些切面的信息
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:32
 */
public class JoinPoint {
    protected final AspectHandler handler;

    protected final Object[] args;

    protected final Object object;

    protected final Class<?> clazz;

    protected final Method method;

    protected final MethodProxy methodProxy;

    /**
     * 返回值
     */
    protected volatile Object returnValue;

    /**
     * 抛出的异常
     */
    protected volatile Throwable error;

    public JoinPoint(
        AspectHandler handler,
        Object object,
        Method method,
        MethodProxy methodProxy,
        Object[] args
    ) {
        this.handler = handler;
        this.args = args;
        this.object = object;
        this.clazz = object.getClass();
        this.method = method;
        this.methodProxy = methodProxy;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getObject() {
        return object;
    }

    public Class<?> getTargetClass() {
        return clazz;
    }

    public Object getReturn() {
        return returnValue;
    }

    public void setReturn(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Method getMethod() {
        return method;
    }
}
