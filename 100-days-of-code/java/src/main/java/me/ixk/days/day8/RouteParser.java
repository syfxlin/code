/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路由解析器
 *
 * @author Otstar Lin
 * @date 2020/10/14 下午 4:51
 */
public class RouteParser {
    protected static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile(
        "\\{([a-zA-Z$_][a-zA-Z0-9$_]*)(\\?)?(:[^/}]+)?}"
    );
    protected static final String ONE_CHAR_PATTERN = "{.}";
    protected static final String MULTI_CHAR_PATTERN = "{*}";
    protected static final String MULTI_PATH_PATTERN = "{**}";
    protected static final String PATH_ITEM_REPLACE = "[^/}]";
    protected static final String PATH_ITEMS_REPLACE = "[^/}]+";
    protected static final String MULTI_PATH_ITEMS_REPLACE = "[^}]+";

    public RouteData parse(String route) {
        route = route.trim();
        final Matcher matcher = PATH_VARIABLE_PATTERN.matcher(route);
        String routeRegex = route;
        final List<String> variableNames = new ArrayList<>();
        boolean isStatic = true;
        while (matcher.find()) {
            final String variableName = matcher.group(1).trim();
            final boolean isOptional = matcher.group(2) != null;
            final String customPattern = matcher.group(3);
            routeRegex =
                routeRegex.replace(
                    matcher.group(0),
                    String.format(
                        "%s(%s)%s",
                        isOptional ? "?" : "",
                        customPattern == null
                            ? PATH_ITEMS_REPLACE
                            : customPattern.trim().substring(1),
                        isOptional ? "?" : ""
                    )
                );
            variableNames.add(variableName);
            isStatic = false;
        }
        if (
            routeRegex.contains(ONE_CHAR_PATTERN) ||
            routeRegex.contains(MULTI_CHAR_PATTERN) ||
            routeRegex.contains(MULTI_PATH_PATTERN)
        ) {
            routeRegex =
                routeRegex
                    .replace(ONE_CHAR_PATTERN, PATH_ITEM_REPLACE)
                    .replace(MULTI_CHAR_PATTERN, PATH_ITEMS_REPLACE)
                    .replace(MULTI_PATH_PATTERN, MULTI_PATH_ITEMS_REPLACE);
            isStatic = false;
        }
        return new RouteData(route, routeRegex, isStatic, variableNames);
    }
}
