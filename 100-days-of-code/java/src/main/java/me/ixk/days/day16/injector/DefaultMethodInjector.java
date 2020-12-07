/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16.injector;

import java.lang.reflect.Method;
import java.util.List;
import me.ixk.days.day16.Container;
import me.ixk.days.day16.DataBinder;
import me.ixk.days.day16.InstanceContext;
import me.ixk.days.day16.InstanceInjector;

/**
 * 默认的方法注入器
 * <p>
 * 执行被 @Autowired 标注的方法
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 10:43
 */
public class DefaultMethodInjector implements InstanceInjector {

    @Override
    public boolean supportsInstance(InstanceContext context, Object instance) {
        return !context.getBinding().getAutowiredMethods().isEmpty();
    }

    @Override
    public Object inject(
        Container container,
        Object instance,
        InstanceContext context,
        DataBinder dataBinder
    ) {
        final List<Method> methods = context.getBinding().getAutowiredMethods();
        for (final Method method : methods) {
            // Set 注入
            container.call(instance, method, Object.class, dataBinder);
        }
        return instance;
    }
}
