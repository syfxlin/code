/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean
 * <p>
 * 被该注解标记的方法或类，会在容器启动的时候自动绑定到容器
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:21
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    @AliasFor("name")
    String[] value() default {  };

    @AliasFor("value")
    String[] name() default {  };

    Class<?>[] type() default {  };

    boolean overwrite() default false;

    String initMethod() default "";

    String destroyMethod() default "";

    BindType bindType() default BindType.BIND;

    enum BindType {
        /**
         * 绑定类型
         */
        BIND,
        /**
         * 不绑定类型
         */
        NO_BIND,
    }
}
