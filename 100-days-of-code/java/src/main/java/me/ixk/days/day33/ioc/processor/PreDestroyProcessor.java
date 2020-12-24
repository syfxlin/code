/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc.processor;

import java.lang.reflect.Method;
import me.ixk.days.day33.annotations.BeanProcessor;
import me.ixk.days.day33.annotations.Order;
import me.ixk.days.day33.ioc.Container;
import me.ixk.days.day33.ioc.InstanceContext;

/**
 * 销毁前处理器
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:07
 */
@BeanProcessor
@Order(Order.LOWEST_PRECEDENCE)
public class PreDestroyProcessor implements BeanAfterProcessor {

    @Override
    public void process(
        Container container,
        Object instance,
        InstanceContext context
    ) {
        final Method method = context.getBinding().getDestroyMethod();
        if (method != null) {
            container.call(instance, method, Object.class);
        }
    }
}
