/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import me.ixk.days.day16.AnnotatedEntry.ChangeableEntry;
import me.ixk.days.day5.MergedAnnotation;

/**
 * 对象上下文
 *
 * @author Otstar Lin
 * @date 2020/11/9 下午 8:01
 */
public class InstanceContext {

    private final Binding binding;
    private final AnnotatedEntry<Class<?>> instanceEntry;
    private final ChangeableEntry<Method>[] methodEntries;
    private final ChangeableEntry<Field>[] fieldEntries;

    @SuppressWarnings("unchecked")
    public InstanceContext(Binding binding, Class<?> instanceType) {
        this.binding = binding;
        this.instanceEntry = new AnnotatedEntry<>(instanceType);
        final AnnotatedEntry<Field>[] fieldEntries =
            this.binding.getFieldEntries();
        this.fieldEntries = new ChangeableEntry[fieldEntries.length];
        for (int i = 0; i < fieldEntries.length; i++) {
            this.fieldEntries[i] = new ChangeableEntry<>(fieldEntries[i]);
        }
        final AnnotatedEntry<Method>[] methodEntries =
            this.binding.getMethodEntries();
        this.methodEntries = new ChangeableEntry[methodEntries.length];
        for (int i = 0; i < methodEntries.length; i++) {
            this.methodEntries[i] = new ChangeableEntry<>(methodEntries[i]);
        }
    }

    public Binding getBinding() {
        return binding;
    }

    public AnnotatedEntry<Class<?>> getInstanceEntry() {
        return instanceEntry;
    }

    public ChangeableEntry<Field>[] getFieldEntries() {
        return this.fieldEntries;
    }

    public AnnotatedEntry<Method>[] getMethodEntries() {
        return this.methodEntries;
    }

    public Class<?> getInstanceType() {
        return this.getInstanceEntry().getElement();
    }

    public MergedAnnotation getAnnotation() {
        return this.getInstanceEntry().getAnnotation();
    }

    public Field[] getFields() {
        return Arrays
            .stream(this.getFieldEntries())
            .map(AnnotatedEntry::getElement)
            .toArray(Field[]::new);
    }

    public MergedAnnotation[] getFieldAnnotations() {
        return Arrays
            .stream(this.getFieldEntries())
            .map(AnnotatedEntry::getAnnotation)
            .toArray(MergedAnnotation[]::new);
    }

    public Method[] getMethods() {
        return Arrays
            .stream(this.getMethodEntries())
            .map(AnnotatedEntry::getElement)
            .toArray(Method[]::new);
    }

    public MergedAnnotation[] getMethodAnnotations() {
        return Arrays
            .stream(this.getMethodEntries())
            .map(AnnotatedEntry::getAnnotation)
            .toArray(MergedAnnotation[]::new);
    }
}
