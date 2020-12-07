/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import me.ixk.days.day5.MergedAnnotation;

/**
 * 数据绑定器
 * <p>
 * 用于获取对应名称或类型的 Bean
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:36
 */
public interface DataBinder {
    /**
     * 获取实例
     *
     * @param name       实例名
     * @param type       实例类型
     * @param annotation 注解
     * @param <T>        实例类型
     *
     * @return 实例
     */
    <T> T getObject(String name, Class<T> type, MergedAnnotation annotation);

    interface Converter {
        /**
         * 前置转换器
         *
         * @param object     实例
         * @param name       名称
         * @param type       类型
         * @param annotation 注解
         *
         * @return 实例
         */
        default Object before(
            Object object,
            String name,
            Class<?> type,
            MergedAnnotation annotation
        ) {
            return object;
        }

        /**
         * 后置转换器
         *
         * @param object     实例
         * @param name       名称
         * @param type       类型
         * @param annotation 注解
         *
         * @return 实例
         */
        default <T> T after(
            T object,
            String name,
            Class<T> type,
            MergedAnnotation annotation
        ) {
            return object;
        }
    }
}
