/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day16;

/**
 * 作用域类型
 *
 * @author Otstar Lin
 * @date 2020/10/13 下午 5:48
 */
public enum ScopeType {
    /**
     * 单例
     */
    SINGLETON("singleton"),
    /**
     * 多例
     */
    PROTOTYPE("prototype"),
    /**
     * 请求
     */
    REQUEST("request"),
    /**
     * 会话
     */
    SESSION("session");

    private final String name;

    ScopeType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public boolean isShared() {
        return this != PROTOTYPE;
    }

    public boolean isRequest() {
        return this == REQUEST;
    }

    public boolean isSession() {
        return this == SESSION;
    }

    public boolean isSingleton() {
        return this == SINGLETON;
    }

    public boolean isPrototype() {
        return this == PROTOTYPE;
    }
}
