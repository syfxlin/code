/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.exceptions;

/**
 * 切面处理异常
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:02
 */
public class AspectProcessException extends Exception {

    public AspectProcessException() {
        super();
    }

    public AspectProcessException(String message) {
        super(message);
    }

    public AspectProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public AspectProcessException(Throwable cause) {
        super(cause);
    }

    protected AspectProcessException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
