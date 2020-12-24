/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置项
 * <p>
 * 自动注入配置项
 * <p>
 * ignoreInvalidFields 是否忽略不可用的字段
 * <p>
 * ignoreUnknownFields 是否忽略Java类不存在的字段
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:42
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface ConfigurationProperties {
    @AliasFor("prefix")
    String value() default "";

    @AliasFor("value")
    String prefix() default "";

    boolean ignoreInvalidFields() default false;

    boolean ignoreUnknownFields() default true;

    @AliasFor(annotation = Bean.class, attribute = "type")
    Class<?>[] type() default {};

    @AliasFor(annotation = Bean.class, attribute = "name")
    String[] name() default {};
}
