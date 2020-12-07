/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ContextItem
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 11:35
 */
public final class ContextItem {

    private final Map<String, Binding> bindings = new ConcurrentHashMap<>(256);

    private final Map<String, String> aliases = new ConcurrentHashMap<>(256);

    private final Map<String, Object> attributes = new ConcurrentHashMap<>(256);

    public Map<String, Binding> getBindings() {
        return bindings;
    }

    public Map<String, String> getAliases() {
        return aliases;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
