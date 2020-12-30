/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.design_pattern.facade;

/**
 * 响应门面
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:11
 */
public class ResultFacade {

    public static EmptyResult empty() {
        return new EmptyResult();
    }

    public static HtmlResult html(final String html) {
        return new HtmlResult(html);
    }

    public static StringJsonResult stringJson(final String json) {
        return new StringJsonResult(json);
    }

    public static TextResult text(final String text) {
        return new TextResult(text);
    }
}
