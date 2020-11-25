/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import me.ixk.days.day6.exception.AspectProcessException;

/**
 * 通知（抽象）
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:09
 */
public abstract class AbstractAdvice implements Advice {

    @Override
    public void before(JoinPoint joinPoint) {}

    @Override
    public void after(JoinPoint joinPoint) {}

    @Override
    public void afterReturning(JoinPoint joinPoint) {}

    @Override
    public void afterThrowing(Throwable exception) throws Throwable {
        throw exception;
    }

    @Override
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new AspectProcessException(
                "Around process has errors not captured",
                e
            );
        }
    }
}
