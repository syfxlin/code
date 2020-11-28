/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由结果
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:52
 */
public class RouteResult {
    protected volatile RouteStatus status = RouteStatus.NOT_FOUND;

    protected volatile String route = null;

    protected volatile Method handler = null;

    protected Map<String, String> params = null;

    public RouteResult() {}

    public RouteResult(RouteStatus status) {
        this.status = status;
    }

    public RouteResult(RouteStatus status, Method handler, String route) {
        this(status, handler, new ConcurrentHashMap<>(), route);
    }

    public RouteResult(
        RouteStatus status,
        Method handler,
        Map<String, String> params,
        String route
    ) {
        this.status = status;
        this.handler = handler;
        this.params = params;
        this.route = route;
    }

    public RouteStatus getStatus() {
        return status;
    }

    public void setStatus(RouteStatus status) {
        this.status = status;
    }

    public Method getHandler() {
        return handler;
    }

    public void setHandler(Method handler) {
        this.handler = handler;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
