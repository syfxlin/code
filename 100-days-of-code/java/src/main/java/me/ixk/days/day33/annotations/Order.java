/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序
 * <p>
 * 排序注解可用于框架中所有的注解上，值越小优先级越高
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 5:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    int MEDIUM_PRECEDENCE = 0;

    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    @AliasFor("order")
    int value() default MEDIUM_PRECEDENCE;

    @AliasFor("value")
    int order() default MEDIUM_PRECEDENCE;
}
