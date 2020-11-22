/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day5;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Native;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import me.ixk.days.day5.annotation.AliasFor;
import me.ixk.days.day5.annotation.RepeatItem;

/**
 * 注解工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:56
 */
public class AnnotationUtils {
    private static final SimpleCache<AnnotatedElement, Map<Class<? extends Annotation>, List<Annotation>>> MERGED_ANNOTATION_CACHE = new SimpleCache<>();

    public static <A extends Annotation> A getAnnotation(
        final AnnotatedElement element,
        final Class<A> annotationType
    ) {
        return getAnnotation(element).getAnnotation(annotationType);
    }

    public static MergedAnnotation getAnnotation(
        final AnnotatedElement element
    ) {
        return new MergedAnnotationImpl(element);
    }

    public static MergedAnnotation wrapAnnotation(final Annotation annotation) {
        final MergedAnnotationImpl mergedAnnotation = new MergedAnnotationImpl();
        mergedAnnotation
            .annotations()
            .put(
                annotation.annotationType(),
                Collections.singletonList(annotation)
            );
        return mergedAnnotation;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMemberValues(
        final Annotation annotation
    ) {
        try {
            final InvocationHandler invocationHandler = Proxy.getInvocationHandler(
                annotation
            );
            final Field field = invocationHandler
                .getClass()
                .getDeclaredField("memberValues");
            field.setAccessible(true);
            return (Map<String, Object>) field.get(invocationHandler);
        } catch (final Exception e) {
            throw new RuntimeException("Get annotation member values failed");
        }
    }

    private static Map<String, Object> getAndCloneMemberValues(
        final Annotation annotation
    ) {
        return new HashMap<>(getMemberValues(annotation));
    }

    public static boolean isDefaultValue(
        final Method method,
        final Map<String, Object> memberValues
    ) {
        return isDefaultValue(method, memberValues.get(method.getName()));
    }

    public static boolean isDefaultValue(
        final Method method,
        final Object value
    ) {
        final Object defaultValue = method.getDefaultValue();
        if (method.getReturnType().isArray()) {
            return Arrays.equals((Object[]) defaultValue, (Object[]) value);
        } else {
            return defaultValue.equals(value);
        }
    }

    public static boolean isJdkAnnotation(
        final Class<? extends Annotation> type
    ) {
        return (
            type == Documented.class ||
            type == Retention.class ||
            type == Inherited.class ||
            type == Native.class ||
            type == Repeatable.class ||
            type == Target.class
        );
    }

    public static Class<? extends Annotation> getRepeatItem(
        final Class<? extends Annotation> annotationType
    ) {
        final RepeatItem repeatItem = annotationType.getAnnotation(
            RepeatItem.class
        );
        if (repeatItem == null) {
            return null;
        }
        return repeatItem.value();
    }

    public static Class<? extends Annotation> getRepeatable(
        final Class<? extends Annotation> annotationType
    ) {
        final Repeatable repeatable = annotationType.getAnnotation(
            Repeatable.class
        );
        if (repeatable == null) {
            return null;
        }
        return repeatable.value();
    }

    public static boolean hasAnnotation(
        final AnnotatedElement element,
        final Class<? extends Annotation> annotationType
    ) {
        return getAnnotation(element).hasAnnotation(annotationType);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A annotationForMap(
        final Class<A> annotationType,
        final Map<String, Object> memberValues
    ) {
        return (A) Proxy.newProxyInstance(
            annotationType.getClassLoader(),
            new Class[] { annotationType },
            new AnnotationInvocationHandler(annotationType, memberValues)
        );
    }

    private static void walkAnnotation(
        final AnnotatedElement element,
        Map<Class<? extends Annotation>, List<Map<String, Object>>> map
    ) {
        for (Annotation annotation : element.getAnnotations()) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            if (isJdkAnnotation(annotationType)) {
                continue;
            }
            final List<Map<String, Object>> annotationList = map.getOrDefault(
                annotationType,
                new ArrayList<>()
            );
            for (final Annotation item : element.getAnnotationsByType(
                annotationType
            )) {
                annotationList.add(getAndCloneMemberValues(item));
            }
            map.put(annotationType, annotationList);
            final Class<? extends Annotation> repeatItem = getRepeatItem(
                annotationType
            );
            if (repeatItem != null) {
                final List<Map<String, Object>> itemList = map.getOrDefault(
                    repeatItem,
                    new ArrayList<>()
                );
                for (final Annotation item : (Annotation[]) ReflectUtil.invoke(
                    annotation,
                    "value"
                )) {
                    itemList.add(getAndCloneMemberValues(item));
                }
                map.put(repeatItem, itemList);
            }
            walkAnnotation(annotationType, map);
        }
    }

    private static void mergeAnnotation(
        final AnnotatedElement element,
        final Map<Class<? extends Annotation>, List<Map<String, Object>>> map
    ) {
        for (Annotation annotation : element.getAnnotations()) {
            final Class<? extends Annotation> annotationType = annotation.annotationType();
            if (isJdkAnnotation(annotationType)) {
                continue;
            }
            mergeAnnotationValue(annotationType, map);
            mergeAnnotation(annotationType, map);
        }
    }

    public static Map<Class<? extends Annotation>, List<Annotation>> mergeAnnotation(
        final AnnotatedElement element
    ) {
        final Map<Class<? extends Annotation>, List<Annotation>> cache = MERGED_ANNOTATION_CACHE.get(
            element
        );
        if (cache != null) {
            return cache;
        }
        final Map<Class<? extends Annotation>, List<Map<String, Object>>> map = new LinkedHashMap<>();
        walkAnnotation(element, map);
        mergeAnnotation(element, map);
        final LinkedHashMap<Class<? extends Annotation>, List<Annotation>> result = map
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Entry::getKey,
                    e ->
                        e
                            .getValue()
                            .stream()
                            .map(i -> annotationForMap(e.getKey(), i))
                            .collect(Collectors.toList()),
                    (u, v) -> {
                        throw new IllegalStateException(
                            String.format("Duplicate key %s", u)
                        );
                    },
                    LinkedHashMap::new
                )
            );
        MERGED_ANNOTATION_CACHE.put(element, result);
        return result;
    }

    private static void mergeAnnotationValue(
        final Class<? extends Annotation> annotationType,
        final Map<Class<? extends Annotation>, List<Map<String, Object>>> map
    ) {
        for (final Map<String, Object> memberValues : map.get(annotationType)) {
            final Method[] methods = annotationType.getDeclaredMethods();
            for (final Method method : methods) {
                final AliasFor aliasFor = method.getAnnotation(AliasFor.class);
                if (aliasFor == null) {
                    continue;
                }
                final String name = method.getName();
                if (isDefaultValue(method, memberValues)) {
                    final String alias = aliasFor.value();
                    if (!aliasFor.value().isEmpty()) {
                        memberValues.put(name, memberValues.get(alias));
                    }
                }
            }
            for (final Method method : methods) {
                final AliasFor aliasFor = method.getAnnotation(AliasFor.class);
                if (aliasFor == null) {
                    continue;
                }
                final String name = method.getName();
                if (aliasFor.annotation() != Annotation.class) {
                    final List<Map<String, Object>> parentList = map.get(
                        aliasFor.annotation()
                    );
                    if (parentList != null) {
                        for (Map<String, Object> parentMemberValues : parentList) {
                            parentMemberValues.put(
                                aliasFor.attribute().isEmpty()
                                    ? name
                                    : aliasFor.attribute(),
                                memberValues.get(name)
                            );
                        }
                    }
                }
            }
        }
    }
}
