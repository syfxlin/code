/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 别名注解
 * <p>
 * value 在当前注解中使用，若别名的一项为默认值，则值会被修改成另一项（另一项不为默认值时）
 * <p>
 * annotation 和 attribute 需要互相配合，用于将当前注解的值设置到父注解中，来达到类似于注解继承的效果。
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AliasFor {
    String value() default "";

    Class<? extends Annotation> annotation() default Annotation.class;

    String attribute() default "";
}
