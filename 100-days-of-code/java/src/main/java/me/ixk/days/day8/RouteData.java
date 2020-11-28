/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 路由数据
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:50
 */
public class RouteData {
    protected final boolean isStatic;
    protected final String regex;
    protected final String route;
    protected final List<String> variableNames;
    protected volatile Method handler;

    public RouteData(
        String route,
        String regex,
        boolean isStatic,
        List<String> variableNames
    ) {
        this.route = route;
        this.regex = regex;
        this.variableNames = variableNames;
        this.isStatic = isStatic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean matches(String route) {
        return route.matches(this.regex);
    }

    public String getRoute() {
        return route;
    }

    public String getRegex() {
        return regex;
    }

    public void setHandler(Method handler) {
        this.handler = handler;
    }

    public Method getHandler() {
        return handler;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    public int getVariableSize() {
        return variableNames.size();
    }

    @Override
    public String toString() {
        return (
            "RouteData{" +
            "regex='" +
            regex +
            '\'' +
            ", route='" +
            route +
            '\'' +
            ", handler=" +
            handler +
            ", variableNames=" +
            variableNames +
            '}'
        );
    }
}
