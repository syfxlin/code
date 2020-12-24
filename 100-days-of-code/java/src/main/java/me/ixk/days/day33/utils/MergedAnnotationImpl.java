/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合注解实现类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:10
 */
public class MergedAnnotationImpl implements MergedAnnotation {

    Map<Class<? extends Annotation>, List<Annotation>> annotations;

    public MergedAnnotationImpl() {
        this.annotations = new LinkedHashMap<>();
    }

    public MergedAnnotationImpl(final AnnotatedElement element) {
        this.annotations = AnnotationUtils.mergeAnnotation(element);
    }

    @Override
    public Map<Class<? extends Annotation>, List<Annotation>> annotations() {
        return this.annotations;
    }
}
