/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

/**
 * Wrapper
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 12:44
 */
@FunctionalInterface
public interface Wrapper {
    /**
     * 获取实例
     *
     * @param container  容器
     * @param dataBinder 数据绑定器
     *
     * @return 实例
     * @throws Throwable 创建时抛出的异常
     */
    Object getInstance(Container container, DataBinder dataBinder)
        throws Throwable;
}
