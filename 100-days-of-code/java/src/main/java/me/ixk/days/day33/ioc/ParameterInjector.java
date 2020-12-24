/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc;

/**
 * 参数注入器
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 12:36
 */
public interface ParameterInjector {
    /**
     * 是否匹配注入器
     *
     * @param context      参数上下文
     * @param dependencies 参数值
     *
     * @return 是否匹配
     */
    boolean supportsParameter(ParameterContext context, Object[] dependencies);

    /**
     * 处理
     *
     * @param container    容器
     * @param context      参数上下文
     * @param dependencies 参数值
     * @param dataBinder   数据绑定器
     *
     * @return 参数值
     */
    default Object[] process(
        Container container,
        Object[] dependencies,
        ParameterContext context,
        DataBinder dataBinder
    ) {
        if (this.supportsParameter(context, dependencies)) {
            return this.inject(container, dependencies, context, dataBinder);
        }
        return dependencies;
    }

    /**
     * 注入
     *
     * @param container    容器
     * @param context      参数信息
     * @param dependencies 参数值
     * @param dataBinder   数据绑定器
     *
     * @return 参数值
     */
    Object[] inject(
        Container container,
        Object[] dependencies,
        ParameterContext context,
        DataBinder dataBinder
    );
}
