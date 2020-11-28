/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 路由收集器
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:49
 */
public class RouteCollector {
    private static final Logger log = LoggerFactory.getLogger(
        RouteCollector.class
    );

    private final Map<String, Map<String, Method>> staticRoutes;
    private final Map<String, List<RouteData>> variableRoutes;

    private final RouteParser routeParser;
    private final RouteGenerator routeGenerator;

    private volatile String routeGroupPrefix = "";

    public RouteCollector(
        RouteParser routeParser,
        RouteGenerator routeGenerator
    ) {
        this.staticRoutes = new ConcurrentHashMap<>();
        this.variableRoutes = new ConcurrentHashMap<>();
        this.routeParser = routeParser;
        this.routeGenerator = routeGenerator;
    }

    public void addRoute(String httpMethod, String route, Method handler) {
        this.addRoute(new String[] { httpMethod }, route, handler);
    }

    public void addRoute(String[] httpMethods, String route, Method handler) {
        route = this.routeGroupPrefix + route;
        RouteData routeData = this.routeParser.parse(route);
        log.debug("Add Route: {}", routeData);
        for (String method : httpMethods) {
            if (routeData.isStatic()) {
                this.addStaticRoute(method, routeData, handler);
            } else {
                this.addVariableRoute(method, routeData, handler);
            }
        }
    }

    public RouteCollector addGroup(
        String prefix,
        RouteDefinition routeDefinition
    ) {
        String prevGroupPrefix = this.routeGroupPrefix;
        this.routeGroupPrefix = prevGroupPrefix + prefix;
        routeDefinition.routes(this);
        this.routeGroupPrefix = prevGroupPrefix;
        return this;
    }

    public void get(String route, Method handler) {
        this.addRoute("GET", route, handler);
    }

    public void post(String route, Method handler) {
        this.addRoute("POST", route, handler);
    }

    public void put(String route, Method handler) {
        this.addRoute("PUT", route, handler);
    }

    public void delete(String route, Method handler) {
        this.addRoute("DELETE", route, handler);
    }

    public void patch(String route, Method handler) {
        this.addRoute("PATCH", route, handler);
    }

    public void head(String route, Method handler) {
        this.addRoute("HEAD", route, handler);
    }

    public void options(String route, Method handler) {
        this.addRoute("OPTIONS", route, handler);
    }

    public void match(String[] httpMethods, String route, Method handler) {
        this.addRoute(httpMethods, route, handler);
    }

    public void any(String route, Method handler) {
        this.addRoute(
                new String[] {
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "PATCH",
                    "HEAD",
                    "OPTIONS",
                },
                route,
                handler
            );
    }

    public RouteCollector prefix(String prefix) {
        this.routeGroupPrefix = prefix;
        return this;
    }

    public RouteCollector group(RouteDefinition routeDefinition) {
        return this.addGroup(this.routeGroupPrefix, routeDefinition);
    }

    public RouteCollector group(
        String prefix,
        RouteDefinition routeDefinition
    ) {
        return this.addGroup(prefix, routeDefinition);
    }

    private void addStaticRoute(
        String httpMethod,
        RouteData routeData,
        Method handler
    ) {
        Map<String, Method> methodMap =
            this.staticRoutes.getOrDefault(
                    httpMethod,
                    new ConcurrentHashMap<>()
                );
        methodMap.put(routeData.getRegex(), handler);
        this.staticRoutes.put(httpMethod, methodMap);
    }

    private void addVariableRoute(
        String httpMethod,
        RouteData routeData,
        Method handler
    ) {
        List<RouteData> routeList =
            this.variableRoutes.getOrDefault(httpMethod, new ArrayList<>());
        routeData.setHandler(handler);
        routeList.add(routeData);
        this.variableRoutes.put(httpMethod, routeList);
    }

    public Map<String, MergeRouteData> getMergeVariableRoutes() {
        Map<String, MergeRouteData> map = new ConcurrentHashMap<>();
        for (Map.Entry<String, List<RouteData>> entry : this.variableRoutes.entrySet()) {
            map.put(
                entry.getKey(),
                this.routeGenerator.mergeVariableRoutes(entry.getValue())
            );
        }
        return map;
    }

    public Map<String, Map<String, Method>> getStaticRoutes() {
        return staticRoutes;
    }

    public Map<String, MergeRouteData> getVariableRoutes() {
        return this.getMergeVariableRoutes();
    }

    public Map<String, List<RouteData>> getOriginVariableRoutes() {
        return this.variableRoutes;
    }

    public RouteGenerator getRouteGenerator() {
        return routeGenerator;
    }

    public RouteParser getRouteParser() {
        return routeParser;
    }
}
