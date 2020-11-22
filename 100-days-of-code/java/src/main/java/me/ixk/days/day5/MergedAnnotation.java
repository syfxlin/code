/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day5;

import cn.hutool.core.util.ReflectUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组合注解
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:04
 */
public interface MergedAnnotation {
    String VALUE = "value";
    Logger log = LoggerFactory.getLogger(MergedAnnotation.class);

    /**
     * 获取所有注解
     *
     * @return 注解 Map
     */
    Map<Class<? extends Annotation>, List<Annotation>> annotations();

    /**
     * 获取注解
     *
     * @param annotationType 注解类型
     * @param <A>            注解类型
     *
     * @return 注解
     */
    default <A extends Annotation> A getAnnotation(
        final Class<A> annotationType
    ) {
        return this.getAnnotation(annotationType, 0);
    }

    /**
     * 获取注解
     *
     * @param annotationType 注解类型
     * @param index          索引
     * @param <A>            注解类型
     *
     * @return 注解
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(
        final Class<A> annotationType,
        final int index
    ) {
        final List<Annotation> annotations =
            this.annotations().get(annotationType);
        if (
            annotations == null ||
            annotations.isEmpty() ||
            index < 0 ||
            index >= annotations.size()
        ) {
            return null;
        }
        if (annotations.size() > 1) {
            log.warn(
                "Annotation [{}] is multi, but only get one",
                annotationType.getName()
            );
        }
        return (A) annotations.get(index);
    }

    /**
     * 获取指定类型的注解列表
     *
     * @param annotationType 注解类型
     * @param <A>            注解类型
     *
     * @return 注解列表
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> List<A> getAnnotations(
        Class<A> annotationType
    ) {
        return (List<A>) this.annotations()
            .getOrDefault(annotationType, new ArrayList<>());
    }

    /**
     * 是否存在注解
     *
     * @param annotationType 注解类型
     *
     * @return 是否存在
     */
    default boolean hasAnnotation(
        final Class<? extends Annotation> annotationType
    ) {
        return this.annotations().containsKey(annotationType);
    }

    /**
     * 是否不存在注解
     *
     * @param annotationType 注解类型
     *
     * @return 是否不存在
     */
    default boolean notAnnotation(Class<? extends Annotation> annotationType) {
        return !this.hasAnnotation(annotationType);
    }

    /**
     * 是否包含多个相同类型的注解
     *
     * @param annotationType 注解类型
     *
     * @return 是否包含
     */
    default boolean hasMultiAnnotation(
        final Class<? extends Annotation> annotationType
    ) {
        return (
            this.annotations().containsKey(annotationType) &&
            this.annotations().get(annotationType).size() != 1
        );
    }

    /**
     * 添加注解
     *
     * @param annotation 注解
     */
    default void addAnnotation(Annotation annotation) {
        throw new UnsupportedOperationException(
            "Unsupported add annotation to merge annotation"
        );
    }

    /**
     * 删除注解
     *
     * @param annotationType 注解类型
     */
    default void removeAnnotation(Class<? extends Annotation> annotationType) {
        throw new UnsupportedOperationException(
            "Unsupported remove annotation to merge annotation"
        );
    }

    /**
     * 删除注解
     *
     * @param annotationType 注解类型
     * @param index          索引
     */
    default void removeAnnotation(
        Class<? extends Annotation> annotationType,
        int index
    ) {
        throw new UnsupportedOperationException(
            "Unsupported add annotation to merge annotation"
        );
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param <T>            注解值类型
     *
     * @return 注解值
     */
    default <T> T get(Class<? extends Annotation> annotationType) {
        return this.get(annotationType, VALUE);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param name           方法名称
     * @param <T>            注解值类型
     *
     * @return 注解值
     */
    default <T> T get(Class<? extends Annotation> annotationType, String name) {
        return this.get(annotationType, name, 0);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param name           方法名称
     * @param index          索引
     * @param <T>            注解值类型
     *
     * @return 注解值
     */
    default <T> T get(
        Class<? extends Annotation> annotationType,
        String name,
        int index
    ) {
        final Annotation annotation = this.getAnnotation(annotationType, index);
        final Method method = ReflectUtil.getMethodByName(annotationType, name);
        if (annotation == null || method == null) {
            return null;
        }
        return ReflectUtil.invoke(annotation, method);
    }
}
