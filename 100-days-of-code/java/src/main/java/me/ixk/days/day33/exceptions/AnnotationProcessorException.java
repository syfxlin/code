/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.exceptions;

/**
 * 注解处理异常
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:01
 */
public class AnnotationProcessorException extends Exception {

    public AnnotationProcessorException() {
        super();
    }

    public AnnotationProcessorException(String message) {
        super(message);
    }

    public AnnotationProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnnotationProcessorException(Throwable cause) {
        super(cause);
    }

    protected AnnotationProcessorException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
