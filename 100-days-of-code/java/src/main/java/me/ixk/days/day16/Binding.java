/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import me.ixk.days.day14.SoftCache;
import me.ixk.days.day16.annotation.Autowired;
import me.ixk.days.day16.annotation.PostConstruct;
import me.ixk.days.day16.annotation.PreDestroy;
import me.ixk.days.day5.MergedAnnotation;

/**
 * Binding
 *
 * @author Otstar Lin
 * @date 2020/10/25 下午 9:02
 */
public class Binding {

    private static final SoftCache<Class<?>, BindingInfos> CACHE = new SoftCache<>();

    private final Context context;
    private volatile Wrapper wrapper;
    private final ScopeType scope;
    private final String instanceName;
    private final Class<?> instanceType;
    private volatile BindingInfos bindingInfos;

    public Binding(
        final Context context,
        final String instanceName,
        final Wrapper wrapper,
        final ScopeType scopeType
    ) {
        this.context = context;
        this.wrapper = wrapper;
        this.scope = scopeType;
        this.instanceName = instanceName;
        Class<?> type;
        try {
            type = ClassUtil.loadClass(instanceName);
        } catch (final UtilException e) {
            type = null;
        }
        this.instanceType = type;
        this.init();
    }

    public Binding(
        final Context context,
        final String instanceName,
        final Object instance,
        final ScopeType scopeType
    ) {
        this(context, instanceName, null, scopeType);
        this.setInstance(instance);
        this.setWrapper((container, with) -> this.getInstance());
    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (instanceType != null) {
            this.bindingInfos =
                CACHE.computeIfAbsent(
                    instanceType,
                    type -> {
                        BindingInfos infos = new BindingInfos();
                        // Fields
                        infos.setFieldEntries(
                            Arrays
                                .stream(instanceType.getDeclaredFields())
                                .map(AnnotatedEntry::new)
                                .toArray(AnnotatedEntry[]::new)
                        );
                        // Methods
                        infos.setMethodEntries(
                            Arrays
                                .stream(instanceType.getDeclaredMethods())
                                .map(AnnotatedEntry::new)
                                .toArray(AnnotatedEntry[]::new)
                        );
                        // InitMethod, DestroyMethod, AutowiredMethod
                        final List<Method> autowiredMethods = new ArrayList<>();
                        for (AnnotatedEntry<Method> entry : infos.getMethodEntries()) {
                            final Method method = entry.getElement();
                            final MergedAnnotation annotation = entry.getAnnotation();
                            if (annotation.hasAnnotation(PostConstruct.class)) {
                                infos.setInitMethod(method);
                            }
                            if (annotation.hasAnnotation(PreDestroy.class)) {
                                infos.setDestroyMethod(method);
                            }
                            if (annotation.hasAnnotation(Autowired.class)) {
                                autowiredMethods.add(method);
                            }
                        }
                        infos.setAutowiredMethods(autowiredMethods);
                        return infos;
                    }
                );
        } else {
            this.bindingInfos = new BindingInfos();
        }
    }

    public ScopeType getScope() {
        return scope;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public Class<?> getInstanceType() {
        return instanceType;
    }

    public Object getInstance() {
        return this.isCreated()
            ? this.context.get(
                    instanceName,
                    instanceType == null ? Object.class : instanceType
                )
            : null;
    }

    public void setInstance(final Object instance) {
        this.context.set(instanceName, instance);
    }

    public boolean isCreated() {
        return this.context.has(instanceName);
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(final Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    public Method getInitMethod() {
        return this.bindingInfos.getInitMethod();
    }

    public void setInitMethod(final Method initMethod) {
        this.bindingInfos.setInitMethod(initMethod);
    }

    public Method getDestroyMethod() {
        return this.bindingInfos.getDestroyMethod();
    }

    public void setDestroyMethod(final Method destroyMethod) {
        this.bindingInfos.setDestroyMethod(destroyMethod);
    }

    public List<Method> getAutowiredMethods() {
        return this.bindingInfos.getAutowiredMethods();
    }

    public void setAutowiredMethods(final List<Method> autowiredMethods) {
        this.bindingInfos.setAutowiredMethods(autowiredMethods);
    }

    public AnnotatedEntry<Field>[] getFieldEntries() {
        return this.bindingInfos.getFieldEntries();
    }

    public AnnotatedEntry<Method>[] getMethodEntries() {
        return this.bindingInfos.getMethodEntries();
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Binding binding = (Binding) o;
        return Objects.equals(instanceName, binding.instanceName);
    }

    private static class BindingInfos {

        private volatile Method initMethod;
        private volatile Method destroyMethod;
        private volatile List<Method> autowiredMethods;

        private volatile AnnotatedEntry<Field>[] fieldEntries;
        private volatile AnnotatedEntry<Method>[] methodEntries;

        public Method getInitMethod() {
            return initMethod;
        }

        public void setInitMethod(final Method initMethod) {
            this.initMethod = initMethod;
        }

        public Method getDestroyMethod() {
            return destroyMethod;
        }

        public void setDestroyMethod(final Method destroyMethod) {
            this.destroyMethod = destroyMethod;
        }

        public List<Method> getAutowiredMethods() {
            return autowiredMethods == null
                ? Collections.emptyList()
                : autowiredMethods;
        }

        public void setAutowiredMethods(final List<Method> autowiredMethods) {
            this.autowiredMethods = autowiredMethods;
        }

        @SuppressWarnings("unchecked")
        public AnnotatedEntry<Field>[] getFieldEntries() {
            return fieldEntries == null ? new AnnotatedEntry[0] : fieldEntries;
        }

        public void setFieldEntries(AnnotatedEntry<Field>[] fieldEntries) {
            this.fieldEntries = fieldEntries;
        }

        @SuppressWarnings("unchecked")
        public AnnotatedEntry<Method>[] getMethodEntries() {
            return methodEntries == null
                ? new AnnotatedEntry[0]
                : methodEntries;
        }

        public void setMethodEntries(AnnotatedEntry<Method>[] methodEntries) {
            this.methodEntries = methodEntries;
        }
    }
}
