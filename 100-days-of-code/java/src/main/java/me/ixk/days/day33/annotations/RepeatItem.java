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
 * 重复注解项
 * <p>
 * 与 Reportable 配合使用
 * <p>
 * Reportable 标记于单项注解（值为多项注解），ReportItem 标记于多项注解（值为单项注解），如果缺失任何一个标记则会导致注解无法正常工作
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 5:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RepeatItem {
    Class<? extends Annotation> value();
}
