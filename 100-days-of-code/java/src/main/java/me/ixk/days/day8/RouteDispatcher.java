/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路由调度器
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:51
 */
public class RouteDispatcher {
    private final RouteCollector collector;

    public RouteDispatcher(RouteCollector routeCollector) {
        this.collector = routeCollector;
    }

    public RouteResult dispatch(String httpMethod, String url) {
        final Map<String, Map<String, Method>> staticRoutes =
            this.collector.getStaticRoutes();
        final Map<String, MergeRouteData> variableRoutes =
            this.collector.getVariableRoutes();
        boolean methodInStatic = staticRoutes.containsKey(httpMethod);
        boolean methodInVariable = variableRoutes.containsKey(httpMethod);
        if (methodInStatic && staticRoutes.get(httpMethod).containsKey(url)) {
            return new RouteResult(
                RouteStatus.FOUND,
                staticRoutes.get(httpMethod).get(url),
                url
            );
        }
        if (methodInVariable) {
            MergeRouteData mergeRouteData = variableRoutes.get(httpMethod);
            RouteResult result =
                this.dispatchVariableRoute(mergeRouteData, url);
            if (result.getStatus() == RouteStatus.FOUND) {
                return result;
            }
        }

        if ("PATCH".equals(httpMethod)) {
            return this.dispatch("GET", url);
        }

        for (Map<String, Method> routeData : staticRoutes.values()) {
            if (routeData.containsKey(url)) {
                return new RouteResult(RouteStatus.METHOD_NOT_ALLOWED);
            }
        }

        for (MergeRouteData routeData : variableRoutes.values()) {
            if (
                this.dispatchVariableRoute(routeData, url).getStatus() ==
                RouteStatus.FOUND
            ) {
                return new RouteResult(RouteStatus.METHOD_NOT_ALLOWED);
            }
        }

        return new RouteResult();
    }

    private RouteResult dispatchVariableRoute(
        MergeRouteData mergeRouteData,
        String url
    ) {
        Pattern pattern = Pattern.compile(mergeRouteData.getRegex());
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            return new RouteResult();
        }
        int index;
        //noinspection StatementWithEmptyBody
        for (index = 1; matcher.group(index) == null; index++) {}
        RouteData routeData = mergeRouteData.getRouteMap().get(index - 1);
        Map<String, String> routeParams = new ConcurrentHashMap<>();
        index++;
        for (String paramName : routeData.getVariableNames()) {
            final String value = matcher.group(index++);
            routeParams.put(paramName, value == null ? "" : value);
        }
        return new RouteResult(
            RouteStatus.FOUND,
            routeData.getHandler(),
            routeParams,
            routeData.getRoute()
        );
    }

    public RouteCollector getCollector() {
        return collector;
    }
}
