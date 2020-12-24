/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.ioc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public boolean matchesScope(final String scopeType) {
        return (
            ScopeType.SINGLETON.is(scopeType) ||
            ScopeType.PROTOTYPE.is(scopeType)
        );
    }

    @Override
    public Map<String, Object> getInstances() {
        return this.instances;
    }

    @Override
    public boolean isShared(String scopeType) {
        return !ScopeType.PROTOTYPE.is(scopeType);
    }
}
