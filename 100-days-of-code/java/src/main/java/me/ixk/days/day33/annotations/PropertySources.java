/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Otstar Lin
 * @date 2020/11/29 上午 12:06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RepeatItem(PropertySource.class)
public @interface PropertySources {
    PropertySource[] value();
}
