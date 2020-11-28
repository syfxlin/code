/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.util.Map;

/**
 * 合并后的路由数据
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:49
 */
public class MergeRouteData {
    protected final String regex;

    protected final Map<Integer, RouteData> routeMap;

    public MergeRouteData(String regex, Map<Integer, RouteData> routeMap) {
        this.regex = regex;
        this.routeMap = routeMap;
    }

    public String getRegex() {
        return regex;
    }

    public Map<Integer, RouteData> getRouteMap() {
        return routeMap;
    }
}
