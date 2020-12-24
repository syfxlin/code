/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc.injector;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import me.ixk.days.day33.annotations.Autowired;
import me.ixk.days.day33.annotations.Injector;
import me.ixk.days.day33.annotations.Order;
import me.ixk.days.day33.ioc.AnnotatedEntry.ChangeableEntry;
import me.ixk.days.day33.ioc.Container;
import me.ixk.days.day33.ioc.DataBinder;
import me.ixk.days.day33.ioc.InstanceContext;
import me.ixk.days.day33.ioc.InstanceInjector;
import me.ixk.days.day33.utils.MergedAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认字段注入器
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:01
 */
@Injector
@Order(Order.LOWEST_PRECEDENCE)
public class DefaultPropertyInjector implements InstanceInjector {

    private static final Logger log = LoggerFactory.getLogger(
        DefaultPropertyInjector.class
    );

    @Override
    public boolean supportsInstance(InstanceContext context, Object instance) {
        return context.getFieldEntries().length > 0;
    }

    @Override
    public Object inject(
        Container container,
        Object instance,
        InstanceContext context,
        DataBinder dataBinder
    ) {
        for (ChangeableEntry<Field> entry : context.getFieldEntries()) {
            if (entry.isChanged()) {
                continue;
            }
            final Field field = entry.getElement();
            final MergedAnnotation annotation = entry.getAnnotation();
            Autowired autowired = annotation.getAnnotation(Autowired.class);
            if (autowired == null) {
                PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(
                    context.getInstanceType(),
                    field.getName()
                );
                if (propertyDescriptor == null) {
                    continue;
                }
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (writeMethod == null) {
                    continue;
                }
                Object dependency = dataBinder.getObject(
                    field.getName(),
                    field.getType(),
                    annotation
                );
                if (dependency == null) {
                    dependency = ReflectUtil.getFieldValue(instance, field);
                }
                ReflectUtil.invoke(instance, writeMethod, dependency);
            } else {
                Object dependency;
                String name = autowired.name();
                Class<?> type = autowired.type();
                if (!"".equals(name)) {
                    dependency = container.make(name, field.getType());
                } else {
                    Class<?> autowiredClass;
                    if (type == Class.class) {
                        autowiredClass = field.getType();
                    } else {
                        autowiredClass = type;
                    }
                    dependency =
                        dataBinder.getObject(
                            field.getName(),
                            autowiredClass,
                            annotation
                        );
                }
                if (dependency == null) {
                    dependency = ReflectUtil.getFieldValue(instance, field);
                }
                // 如果必须注入，但是为 null，则抛出错误
                if (dependency == null && autowired.required()) {
                    final NullPointerException exception = new NullPointerException(
                        "Target [" +
                        context.getInstanceType().getName() +
                        "::" +
                        field.getName() +
                        "] is required, but inject value is null"
                    );
                    log.error(
                        "Target [{}::{}] is required, but inject value is null",
                        context.getInstanceType().getName(),
                        field.getName()
                    );
                    throw exception;
                }
                ReflectUtil.setFieldValue(instance, field, dependency);
            }
            entry.setChanged(true);
        }
        return instance;
    }
}
