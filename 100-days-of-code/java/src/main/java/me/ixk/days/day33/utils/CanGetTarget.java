/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

/**
 * 被代理对象获取
 * <p>
 * 当对象被 Cglib 或者 Proxy 代理的时候，会导致 Validate 出现问题，所以需要获取到源对象
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:28
 */
public interface CanGetTarget {
    /**
     * 获取源对象
     *
     * @return 源对象
     */
    Object getTarget();
}
