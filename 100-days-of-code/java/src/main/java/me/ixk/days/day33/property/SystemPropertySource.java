/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

/**
 * 系统配置数据源
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 9:06
 */
public class SystemPropertySource extends MapPropertySource<Object> {

    public SystemPropertySource(final String name) {
        super(name, System.getProperties());
    }
}
