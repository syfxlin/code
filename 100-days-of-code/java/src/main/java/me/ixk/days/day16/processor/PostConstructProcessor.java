/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16.processor;

import java.lang.reflect.Method;
import me.ixk.days.day16.BeanBeforeProcessor;
import me.ixk.days.day16.Container;
import me.ixk.days.day16.InstanceContext;

/**
 * 构造器后处理器
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:07
 */
public class PostConstructProcessor implements BeanBeforeProcessor {

    @Override
    public Object process(
        Container container,
        Object instance,
        InstanceContext context
    ) {
        Method method = context.getBinding().getInitMethod();
        if (method != null) {
            container.call(instance, method, Object.class);
        }
        return instance;
    }
}
