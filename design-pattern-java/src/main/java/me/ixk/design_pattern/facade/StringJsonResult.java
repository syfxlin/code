/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.design_pattern.facade;

/**
 * String JSON 响应
 *
 * @author Otstar Lin
 * @date 2020/11/1 下午 10:32
 */
public class StringJsonResult extends AbstractHttpResult {

    private String json;

    public StringJsonResult() {}

    public StringJsonResult(final String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    public StringJsonResult with(final String json) {
        this.json = json;
        return this;
    }

    @Override
    public String render() {
        return this.json;
    }

    @Override
    public String contentType() {
        return "application/json";
    }
}
