/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组件
 * <p>
 * 效果同 Bean，是 Bean 的语义化别名
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Bean
public @interface Component {
    @AliasFor(value = "name", annotation = Bean.class, attribute = "value")
    String[] value() default {  };

    @AliasFor(value = "value", annotation = Bean.class, attribute = "name")
    String[] name() default {  };

    @AliasFor(annotation = Bean.class, attribute = "type")
    Class<?>[] type() default {  };

    String test() default "";
}
