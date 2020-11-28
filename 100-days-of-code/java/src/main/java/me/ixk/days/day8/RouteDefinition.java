/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

/**
 * 路由定义
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:50
 */
@FunctionalInterface
public interface RouteDefinition {
    /**
     * 路由
     *
     * @param routeCollector 路由收集
     */
    void routes(RouteCollector routeCollector);
}
