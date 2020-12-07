/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import java.util.Map;

/**
 * Context
 *
 * @author Otstar Lin
 * @date 2020/10/25 下午 9:27
 */
public interface Context {
    /**
     * 是否匹配作用域类型
     *
     * @param scopeType 作用域类型
     *
     * @return 是否匹配
     */
    boolean matchesScope(ScopeType scopeType);

    /**
     * 该 Context 是否启动，一般的 Context 只要 new 后就会启动 但是如果是 ThreadLocal 则需要另行启动
     *
     * @return 是否启动
     */
    default boolean isCreated() {
        return true;
    }

    /**
     * 获取所有实例
     *
     * @return 所有实例
     */
    Map<String, Object> getInstances();

    /**
     * 获取实例
     *
     * @param name 实例名称
     *
     * @return 实例
     */
    default Object get(final String name) {
        return this.get(name, Object.class);
    }

    /**
     * 获取实例
     *
     * @param name       实例名称
     * @param returnType 类型
     *
     * @return 实例
     */
    default Object get(final String name, final Class<?> returnType) {
        return this.getInstances().get(name);
    }

    /**
     * 删除实例
     *
     * @param name 实例名称
     */
    default void remove(final String name) {
        this.getInstances().remove(name);
    }

    /**
     * 设置实例
     *
     * @param name     名称
     * @param instance 实例
     */
    default void set(final String name, final Object instance) {
        this.getInstances().put(name, instance);
    }

    /**
     * 是否存在实例
     *
     * @param name 实例名称
     *
     * @return 是否存在
     */
    default boolean has(final String name) {
        return this.getInstances().containsKey(name);
    }
}
