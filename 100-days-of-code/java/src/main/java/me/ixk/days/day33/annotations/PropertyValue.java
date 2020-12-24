/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.ixk.days.day33.ioc.PropertyResolver;

/**
 * 属性配置
 * <p>
 * 配合 @ConfigurationProperties 使用
 *
 * @author Otstar Lin
 * @date 2020/11/5 下午 10:29
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyValue {
    String EMPTY = "EMPTY";

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String defaultValue() default EMPTY;

    Class<? extends PropertyResolver> resolver() default PropertyResolver.class;

    boolean skip() default false;
}
