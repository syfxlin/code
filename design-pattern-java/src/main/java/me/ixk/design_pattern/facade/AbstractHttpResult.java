/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.design_pattern.facade;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP 响应
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:10
 */
public abstract class AbstractHttpResult {

    private int status = 200;
    private final Map<String, String> headers = new ConcurrentHashMap<>();
    private String content;

    public final AbstractHttpResult status(final int sc) {
        this.status = sc;
        return this;
    }

    public final AbstractHttpResult header(
        final String name,
        final String value
    ) {
        this.headers.put(name, value);
        return this;
    }

    public final AbstractHttpResult headers(final Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public abstract String render();

    public String contentType() {
        return "text/plain";
    }
}
