/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

import cn.hutool.core.convert.Convert;
import java.util.Objects;

/**
 * 配置数据源
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 8:48
 */
public abstract class PropertySource<T> {

    protected final String name;
    protected final T source;

    public PropertySource(final String name, final T source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public T getSource() {
        return source;
    }

    public boolean containsProperty(String name) {
        return (this.getProperty(name) != null);
    }

    public abstract Object getProperty(String name);

    public Object getProperty(String name, Object defaultValue) {
        final Object value = this.getProperty(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public Object get(final String name) {
        return this.getProperty(name);
    }

    public boolean has(final String name) {
        return this.containsProperty(name);
    }

    public <R> R get(final String key, final Class<R> returnType) {
        return Convert.convert(returnType, this.getProperty(key));
    }

    @SuppressWarnings("unchecked")
    public <R> R get(final String key, final R defaultValue) {
        final Object value = this.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return (R) Convert.convert(defaultValue.getClass(), value);
    }

    public Integer getInt(final String key) {
        return this.getInt(key, null);
    }

    public Integer getInt(final String key, final Integer defaultValue) {
        final Object value = this.getProperty(key);
        if (value != null) {
            return Convert.toInt(value);
        }
        return defaultValue;
    }

    public Long getLong(final String key) {
        return this.getLong(key, null);
    }

    public Long getLong(final String key, final Long defaultValue) {
        final Object value = this.getProperty(key);
        if (value != null) {
            return Convert.toLong(value);
        }
        return defaultValue;
    }

    public Boolean getBoolean(final String key) {
        return this.getBoolean(key, null);
    }

    public Boolean getBoolean(final String key, final Boolean defaultValue) {
        final Object value = this.getProperty(key);
        if (value != null) {
            return Convert.toBool(value);
        }
        return defaultValue;
    }

    public Double getDouble(final String key) {
        return this.getDouble(key, null);
    }

    public Double getDouble(final String key, final Double defaultValue) {
        final Object value = this.getProperty(key);
        if (value != null) {
            return Convert.toDouble(value);
        }
        return defaultValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public boolean equals(Object other) {
        return (
            this == other ||
            (
                other instanceof PropertySource &&
                Objects.equals(this.name, ((PropertySource<?>) other).name)
            )
        );
    }
}
