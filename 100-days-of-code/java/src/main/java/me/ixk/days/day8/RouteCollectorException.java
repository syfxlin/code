/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day8;

/**
 * 路由收集异常
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:06
 */
public class RouteCollectorException extends RuntimeException {

    public RouteCollectorException() {
        super();
    }

    public RouteCollectorException(String message) {
        super(message);
    }

    public RouteCollectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteCollectorException(Throwable cause) {
        super(cause);
    }

    protected RouteCollectorException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
