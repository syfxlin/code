/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

/**
 * 通知
 * <p>
 * 本框架使用的是通过方法实现来进行通知的方法设置，而不使用注解的方式声明通知方法
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:09
 */
public interface Advice {
    /**
     * 前置通知
     *
     * @param joinPoint 连接点
     */
    void before(JoinPoint joinPoint);

    /**
     * 后置通知
     *
     * @param joinPoint 连接点
     */
    void after(JoinPoint joinPoint);

    /**
     * 后置通知（正常返回）
     *
     * @param joinPoint 连接点
     */
    void afterReturning(JoinPoint joinPoint);

    /**
     * 后置通知（异常返回）
     *
     * @param exception 异常
     *
     * @throws Throwable 切面中如果没有处理异常则抛出
     */
    void afterThrowing(Throwable exception) throws Throwable;

    /**
     * 环绕通知
     *
     * @param joinPoint 连接点（可运行）
     *
     * @return 切面返回值
     */
    Object around(ProceedingJoinPoint joinPoint);
}
