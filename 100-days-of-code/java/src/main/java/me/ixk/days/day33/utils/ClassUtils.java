/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:02
 */
public class ClassUtils extends ClassUtil {

    private static final String CGLIB_SPLIT = "$$";

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(
        8
    );

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_WRAPPER_MAP = new IdentityHashMap<>(
        8
    );

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Void.class, void.class);

        for (Map.Entry<Class<?>, Class<?>> entry : PRIMITIVE_WRAPPER_TYPE_MAP.entrySet()) {
            PRIMITIVE_TYPE_TO_WRAPPER_MAP.put(entry.getValue(), entry.getKey());
        }
    }

    public static boolean isCglibProxy(Object object) {
        return isCglibProxyClass(object.getClass());
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

    public static boolean isJdkProxy(Object object) {
        return isJdkProxy(object.getClass());
    }

    public static boolean isJdkProxy(Class<?> clazz) {
        return Proxy.isProxyClass(clazz);
    }

    public static boolean isProxy(Object object) {
        return isProxy(object.getClass());
    }

    public static boolean isProxy(Class<?> clazz) {
        return isJdkProxy(clazz) || isCglibProxyClass(clazz);
    }

    public static Class<?> getUserClass(Object instance) {
        if (instance == null) {
            throw new RuntimeException("Instance must not be null");
        }
        return getUserClass(instance.getClass());
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz.getName().contains(CGLIB_SPLIT)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                return superClass;
            }
        }
        return clazz;
    }

    public static Executable getUserMethod(Executable method) {
        Class<?> userClass = getUserClass(method.getDeclaringClass());
        try {
            if (method instanceof Constructor) {
                return userClass.getConstructor(method.getParameterTypes());
            } else {
                return userClass.getMethod(
                    method.getName(),
                    method.getParameterTypes()
                );
            }
        } catch (NoSuchMethodException e) {
            return method;
        }
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side type must not be null");
        Assert.notNull(rhsType, "Right-hand side type must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = PRIMITIVE_WRAPPER_TYPE_MAP.get(
                rhsType
            );
            return (lhsType == resolvedPrimitive);
        } else {
            Class<?> resolvedWrapper = PRIMITIVE_TYPE_TO_WRAPPER_MAP.get(
                rhsType
            );
            return (
                resolvedWrapper != null &&
                lhsType.isAssignableFrom(resolvedWrapper)
            );
        }
    }

    public static <T> Class<?> getGenericClass(Class<T> clazz) {
        return getGenericClass(clazz, 0);
    }

    public static <T> Class<?> getGenericClass(Class<T> clazz, int index) {
        return (Class<?>) (
            (ParameterizedType) getUserClass(clazz).getGenericSuperclass()
        ).getActualTypeArguments()[index];
    }

    public static Set<Class<?>> getInterfaces(Object instance) {
        return getInterfaces(instance.getClass());
    }

    public static Set<Class<?>> getInterfaces(Class<?> clazz) {
        Set<Class<?>> set = new HashSet<>();
        getInterfaces(clazz, set);
        return set;
    }

    private static void getInterfaces(Class<?> clazz, Set<Class<?>> set) {
        clazz = getUserClass(clazz);
        Class<?>[] interfaces = clazz.getInterfaces();
        set.addAll(Arrays.asList(interfaces));
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            getInterfaces(superClass, set);
        }
    }

    public static Set<Method> getMethods(Object instance) {
        return getMethods(instance.getClass());
    }

    public static Set<Method> getMethods(Class<?> clazz) {
        clazz = getUserClass(clazz);
        Set<Method> methods = new HashSet<>();
        for (Method method : clazz.getMethods()) {
            switch (method.getName()) {
                case "getClass":
                case "hashCode":
                case "equals":
                case "clone":
                case "toString":
                case "notify":
                case "notifyAll":
                case "wait":
                case "finalize":
                    continue;
                default:
                    methods.add(method);
            }
        }
        return methods;
    }

    public static Class<?> primitiveTypeToWrapper(Class<?> type) {
        return PRIMITIVE_TYPE_TO_WRAPPER_MAP.get(type);
    }

    public static Class<?> primitiveWrapperToType(Class<?> type) {
        return PRIMITIVE_WRAPPER_TYPE_MAP.get(type);
    }

    public static Class<?> primitiveTypeToWrapper(String type) {
        for (Map.Entry<Class<?>, Class<?>> entry : PRIMITIVE_TYPE_TO_WRAPPER_MAP.entrySet()) {
            if (entry.getKey().getName().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Class<?> primitiveWrapperToType(String type) {
        for (Map.Entry<Class<?>, Class<?>> entry : PRIMITIVE_WRAPPER_TYPE_MAP.entrySet()) {
            if (entry.getKey().getName().equals(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static boolean isSkipBuildType(Class<?> clazz) {
        if (
            clazz == long.class ||
            clazz == int.class ||
            clazz == short.class ||
            clazz == char.class ||
            clazz == byte.class ||
            clazz == double.class ||
            clazz == float.class ||
            clazz == boolean.class
        ) {
            return true;
        }
        return clazz.getPackageName().startsWith("java.");
    }
}
