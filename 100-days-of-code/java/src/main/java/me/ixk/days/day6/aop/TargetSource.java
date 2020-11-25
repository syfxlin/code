/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理源
 * <p>
 * 存储源对象的一些信息
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:39
 */
public class TargetSource {
    private final Class<?> targetType;
    private final Class<?>[] interfaces;
    private final Object target;
    private final AspectManager aspectManager;

    public TargetSource(
        AspectManager aspectManager,
        Object target,
        Class<?> targetType,
        Class<?>[] interfaces
    ) {
        this.aspectManager = aspectManager;
        this.targetType = targetType;
        this.interfaces = interfaces;
        this.target = target;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public Object getTarget() {
        return target;
    }

    public List<Advice> getAdvices(Method method) {
        if (this.aspectManager == null) {
            return new ArrayList<>();
        }
        return this.aspectManager.getAdvices(method);
    }
}
