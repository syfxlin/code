/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.exceptions;

/**
 * 中央 Servlet 异常
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:03
 */
public class DispatchServletException extends Exception {

    public DispatchServletException() {
        super();
    }

    public DispatchServletException(String message) {
        super(message);
    }

    public DispatchServletException(String message, Throwable cause) {
        super(message, cause);
    }

    public DispatchServletException(Throwable cause) {
        super(cause);
    }

    protected DispatchServletException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
