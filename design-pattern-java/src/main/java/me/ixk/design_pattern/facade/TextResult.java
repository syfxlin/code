/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.design_pattern.facade;

/**
 * 文本工具类
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:11
 */
public class TextResult extends AbstractHttpResult {

    protected String text;

    public TextResult(final String text) {
        this.text = text;
    }

    public TextResult with(final String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }

    @Override
    public String render() {
        return this.text;
    }

    @Override
    public String contentType() {
        return "text/plain";
    }
}
