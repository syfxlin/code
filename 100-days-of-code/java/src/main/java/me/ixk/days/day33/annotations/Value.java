/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表达式注入
 * <p>
 * 使用 SpringEL 作为表达式解析
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 5:52
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    @AliasFor("expression")
    String value() default "";

    @AliasFor("value")
    String expression() default "";
}
