/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import cn.hutool.core.util.ReflectUtil;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import me.ixk.days.day33.annotations.Autowired;
import me.ixk.days.day33.ioc.factory.ObjectFactory;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 反射工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 5:14
 */
public class ReflectUtils {

    public static Constructor<?>[] sortConstructors(
        Constructor<?>[] constructors
    ) {
        sortExecutables(constructors);
        return constructors;
    }

    public static Method[] sortMethods(Method[] methods) {
        sortExecutables(methods);
        return methods;
    }

    private static void sortExecutables(Executable[] executable) {
        Arrays.sort(
            executable,
            (fm1, fm2) -> {
                Autowired a1 = fm1.getAnnotation(Autowired.class);
                Autowired a2 = fm2.getAnnotation(Autowired.class);
                if (a1 != null && a2 == null) {
                    return -1;
                }
                if (a1 == null && a2 != null) {
                    return 1;
                }
                boolean p1 = Modifier.isPublic(fm1.getModifiers());
                boolean p2 = Modifier.isPublic(fm2.getModifiers());
                if (p1 != p2) {
                    return (p1 ? -1 : 1);
                }
                int c1pl = fm1.getParameterTypes().length;
                int c2pl = fm2.getParameterTypes().length;
                return (Integer.compare(c2pl, c1pl));
            }
        );
    }

    public static Object proxyObjectFactory(
        Object target,
        Class<?> requiredType
    ) {
        return resolveAutowiringValue(target, requiredType);
    }

    public static Object resolveAutowiringValue(
        Object autowiringValue,
        Class<?> requiredType
    ) {
        if (autowiringValue instanceof ObjectFactory) {
            ObjectFactory<?> factory = (ObjectFactory<?>) autowiringValue;
            if (
                autowiringValue instanceof Serializable &&
                requiredType.isInterface()
            ) {
                autowiringValue =
                    Proxy.newProxyInstance(
                        requiredType.getClassLoader(),
                        new Class<?>[] { requiredType },
                        new ObjectFactoryDelegatingInterceptor(factory)
                    );
            } else {
                autowiringValue =
                    Enhancer.create(
                        requiredType,
                        new ObjectFactoryDelegatingInterceptor(factory)
                    );
            }
        }
        return autowiringValue;
    }

    public static Object getProxyTarget(Object proxy) {
        if (proxy == null) {
            return null;
        }
        if (ClassUtils.isJdkProxy(proxy)) {
            return getJdkProxyTarget(proxy);
        } else if (ClassUtils.isCglibProxy(proxy)) {
            return getCglibProxyTarget(proxy);
        }
        return proxy;
    }

    public static Object getJdkProxyTarget(Object proxy) {
        try {
            Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            Object handler = h.get(proxy);
            if (CanGetTarget.class.isAssignableFrom(handler.getClass())) {
                return ((CanGetTarget) handler).getTarget();
            }
            return null;
        } catch (Exception e) {
            return proxy;
        }
    }

    public static Object getCglibProxyTarget(Object proxy) {
        try {
            for (Callback callback : (Callback[]) ReflectUtil.invoke(
                proxy,
                "getCallbacks"
            )) {
                if (CanGetTarget.class.isAssignableFrom(callback.getClass())) {
                    return ((CanGetTarget) callback).getTarget();
                }
            }
            return proxy;
        } catch (Exception e) {
            return proxy;
        }
    }

    private static class ObjectFactoryDelegatingInterceptor
        implements
            MethodInterceptor, InvocationHandler, Serializable, CanGetTarget {

        private final ObjectFactory<?> objectFactory;

        public ObjectFactoryDelegatingInterceptor(
            ObjectFactory<?> objectFactory
        ) {
            this.objectFactory = objectFactory;
        }

        @Override
        public Object intercept(
            Object obj,
            Method method,
            Object[] args,
            MethodProxy proxy
        ) throws Throwable {
            final Object object = this.objectFactory.getObject();
            if (object == null) {
                throw new NullPointerException(
                    "ObjectFactory get object is null"
                );
            }
            switch (method.getName()) {
                case "equals":
                    return (object == args[0]);
                case "hashCode":
                    return System.identityHashCode(object);
                case "toString":
                    return object.toString();
                default:
                    return proxy.invoke(object, args);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
            final Object object = this.objectFactory.getObject();
            if (object == null) {
                throw new NullPointerException(
                    "ObjectFactory get object is null"
                );
            }
            switch (method.getName()) {
                case "equals":
                    return (object == args[0]);
                case "hashCode":
                    return System.identityHashCode(object);
                case "toString":
                    return object.toString();
                default:
                    try {
                        return method.invoke(object, args);
                    } catch (InvocationTargetException ex) {
                        throw ex.getTargetException();
                    }
            }
        }

        @Override
        public Object getTarget() {
            return this.objectFactory.getObject();
        }
    }
}
