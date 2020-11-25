/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 切面拦截器
 * <p>
 * 用于 Cglib 拦截对象
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:31
 */
public class DynamicInterceptor implements MethodInterceptor, CanGetTarget {
    protected final TargetSource targetSource;

    public DynamicInterceptor(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    @Override
    public Object intercept(
        Object object,
        Method method,
        Object[] args,
        MethodProxy methodProxy
    )
        throws Throwable {
        // 匹配切面
        List<Advice> advices = this.targetSource.getAdvices(method);
        Object target = this.targetSource.getTarget();
        if (advices != null && !advices.isEmpty()) {
            AspectHandler handler = new AspectHandler(
                target,
                method,
                methodProxy,
                args,
                new AtomicInteger(0),
                advices
            );
            return handler.invokeAspect();
        }

        return methodProxy.invoke(target, args);
    }

    @Override
    public Object getTarget() {
        return this.targetSource.getTarget();
    }
}
