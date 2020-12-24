/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 组合配置源
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 10:21
 */
public class CompositePropertySource
    extends PropertySource<Map<String, PropertySource<?>>> {

    public CompositePropertySource(String name) {
        super(name, new ConcurrentHashMap<>());
    }

    public CompositePropertySource(
        final String name,
        final List<PropertySource<?>> source
    ) {
        super(
            name,
            source
                .stream()
                .collect(
                    Collectors.toConcurrentMap(PropertySource::getName, v -> v)
                )
        );
    }

    public CompositePropertySource(
        final String name,
        final Map<String, PropertySource<?>> source
    ) {
        super(name, source);
    }

    @Override
    public Object getProperty(final String name) {
        for (final PropertySource<?> propertySource : this.source.values()) {
            final Object value = propertySource.get(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public void setPropertySource(final PropertySource<?> propertySource) {
        this.source.put(propertySource.getName(), propertySource);
    }

    public void removePropertySource(final String name) {
        this.source.remove(name);
    }
}
