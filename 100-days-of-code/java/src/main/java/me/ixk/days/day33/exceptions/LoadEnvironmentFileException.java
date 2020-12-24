/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.exceptions;

/**
 * 读取配置异常
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:06
 */
public class LoadEnvironmentFileException extends Exception {

    public LoadEnvironmentFileException() {
        super();
    }

    public LoadEnvironmentFileException(String message) {
        super(message);
    }

    public LoadEnvironmentFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadEnvironmentFileException(Throwable cause) {
        super(cause);
    }

    protected LoadEnvironmentFileException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
