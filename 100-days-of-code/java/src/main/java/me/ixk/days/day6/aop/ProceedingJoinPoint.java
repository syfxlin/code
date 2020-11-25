/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 连接点（可处理）
 * <p>
 * 比普通的连接点多了一个处理方法，用于调用切面链的下一步操作
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:36
 */
public class ProceedingJoinPoint extends JoinPoint {

    public ProceedingJoinPoint(
        AspectHandler handler,
        Object object,
        Method method,
        MethodProxy methodProxy,
        Object[] args
    ) {
        super(handler, object, method, methodProxy, args);
    }

    public Object proceed(Object... args) throws Throwable {
        return this.handler.invokeProcess(args);
    }
}
