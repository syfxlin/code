/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

/**
 * 软引用缓存
 *
 * @author Otstar Lin
 * @date 2020/12/2 下午 12:14
 */
public class SoftCache<K, V> extends WeakCache<K, V> {

    private static final long serialVersionUID = 9085585773514267688L;

    public SoftCache() {
        super(new SoftHashMap<>());
    }
}
