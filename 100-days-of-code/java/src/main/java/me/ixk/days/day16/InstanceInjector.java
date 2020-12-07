/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

/**
 * 实例注入器
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:38
 */
public interface InstanceInjector {
    /**
     * 是否匹配注入器
     *
     * @param context  实例上下文
     * @param instance 实例
     *
     * @return 是否匹配
     */
    boolean supportsInstance(final InstanceContext context, Object instance);

    /**
     * 处理
     *
     * @param container  容器
     * @param instance   实例
     * @param context    参数上下文
     * @param dataBinder 数据绑定器
     *
     * @return 实例
     */
    default Object process(
        final Container container,
        final Object instance,
        final InstanceContext context,
        final DataBinder dataBinder
    ) {
        if (this.supportsInstance(context, instance)) {
            return this.inject(container, instance, context, dataBinder);
        }
        return instance;
    }

    /**
     * 注入
     *
     * @param container  容器
     * @param instance   实例
     * @param context    参数上下文
     * @param dataBinder 数据绑定器
     *
     * @return 实例
     */
    Object inject(
        Container container,
        Object instance,
        InstanceContext context,
        DataBinder dataBinder
    );
}
