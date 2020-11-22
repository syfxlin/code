/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置
 * <p>
 * Configuration 主要用于实例 Bean 的定义，是 Component 的一种
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:42
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component(test = "test")
public @interface Configuration {
    @AliasFor(value = "name", annotation = Component.class, attribute = "value")
    String[] value() default {  };

    @AliasFor(value = "value", annotation = Component.class, attribute = "name")
    String[] name() default {  };

    @AliasFor(annotation = Component.class, attribute = "type")
    Class<?>[] type() default {  };
}
