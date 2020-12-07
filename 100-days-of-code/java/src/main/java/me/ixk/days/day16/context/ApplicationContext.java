/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.ixk.days.day16.Context;
import me.ixk.days.day16.ScopeType;

/**
 * ApplicationContext
 * <p>
 * 保存 App 全局共享的 Bean
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 10:39
 */
public class ApplicationContext implements Context {

    private final Map<String, Object> instances = new ConcurrentHashMap<>();

    @Override
    public boolean matchesScope(final ScopeType scopeType) {
        switch (scopeType) {
            case SINGLETON:
            case PROTOTYPE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Map<String, Object> getInstances() {
        return this.instances;
    }
}
