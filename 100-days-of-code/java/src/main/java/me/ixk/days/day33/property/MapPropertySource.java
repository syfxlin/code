/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

import static me.ixk.days.day33.utils.DataUtils.caseGet;

import java.util.Map;
import java.util.Properties;

/**
 * Map 配置数据源
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 8:53
 */
public class MapPropertySource<T> extends PropertySource<Map<String, T>> {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MapPropertySource(final String name, final Properties properties) {
        super(name, (Map) properties);
    }

    public MapPropertySource(final String name, final Map<String, T> source) {
        super(name, source);
    }

    @Override
    public Object getProperty(final String name) {
        return caseGet(name, this.source::get);
    }

    public String[] getPropertyNames() {
        return this.source.keySet().toArray(String[]::new);
    }
}
