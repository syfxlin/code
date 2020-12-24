/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc;

import java.lang.reflect.Field;
import me.ixk.days.day33.utils.MergedAnnotation;

/**
 * 类属性
 *
 * @author Otstar Lin
 * @date 2020/11/8 下午 7:42
 */
public class ClassProperty {

    private final Object instance;
    private final Class<?> instanceClass;
    private final Field property;
    private final String propertyName;
    private final MergedAnnotation classAnnotation;
    private final MergedAnnotation propertyAnnotation;

    public ClassProperty(
        Object instance,
        Class<?> instanceClass,
        Field property,
        String propertyName,
        MergedAnnotation classAnnotation,
        MergedAnnotation propertyAnnotation
    ) {
        this.instance = instance;
        this.instanceClass = instanceClass;
        this.property = property;
        this.propertyName = propertyName;
        this.classAnnotation = classAnnotation;
        this.propertyAnnotation = propertyAnnotation;
    }

    public Object getInstance() {
        return instance;
    }

    public Class<?> getInstanceClass() {
        return instanceClass;
    }

    public Field getProperty() {
        return property;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getPropertyType() {
        return property.getType();
    }

    public MergedAnnotation getClassAnnotation() {
        return classAnnotation;
    }

    public MergedAnnotation getPropertyAnnotation() {
        return propertyAnnotation;
    }
}
