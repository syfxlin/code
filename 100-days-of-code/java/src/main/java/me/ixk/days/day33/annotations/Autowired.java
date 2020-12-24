/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动装配
 * <p>
 * 注入的范围包括字段，方法，构造器。构造器注入无需使用 @Autowired 注解。
 * <p>
 * 字段注入如果有 WriteMethod 则无需使用 @Autowired 注解，否则你需要在字段上添加 @Autowired 注解。
 * <p>
 * 方法注入分为两种，一种是类似于 Spring 的 Aware 接口，用于对象实例化后立即注入，此情形下你需要添加 @Autowired 注解；
 * 还有一种是普通的方法，普通的方法如果使用 XkJava.call 的方法调用则会自动注入，无需使用 @Autowired 注解。
 * <p>
 * 构造器无需使用 @Autowired 注解，不过你可以使用 Autowired 注解提高构造器的注入优先级
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 4:20
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    Class<?> type() default Class.class;

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default false;
}
