/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性解析器
 *
 * @author Otstar Lin
 * @date 2020/11/8 下午 7:36
 */
public interface PropertyResolver {
    /**
     * 是否支持解析
     *
     * @param value    对应的 Value 值
     * @param property 属性
     *
     * @return 是否支持
     */
    boolean supportsProperty(String value, ClassProperty property);

    /**
     * 解析属性
     *
     * @param value    对应的 Value 值
     * @param property 属性
     *
     * @return 解析后的结果
     */
    Object resolveProperty(String value, ClassProperty property);

    class StringMapResolver implements PropertyResolver {

        @Override
        public boolean supportsProperty(String value, ClassProperty property) {
            return value != null && value.indexOf(":") != 0;
        }

        @Override
        public Object resolveProperty(String value, ClassProperty property) {
            final String[] split = value.split(",");
            Map<String, String> map = new ConcurrentHashMap<>(split.length);
            for (String item : split) {
                if (item.isEmpty()) {
                    continue;
                }
                String[] kv = item.trim().split(":");
                map.put(kv[0], kv[1]);
            }
            return map;
        }
    }
}
