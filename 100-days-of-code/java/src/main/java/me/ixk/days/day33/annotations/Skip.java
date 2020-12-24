/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 跳过注入
 *
 * @author Otstar Lin
 * @date 2020/12/19 下午 1:54
 */
@Target(
    {
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE,
        ElementType.PARAMETER,
    }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface Skip {
}
