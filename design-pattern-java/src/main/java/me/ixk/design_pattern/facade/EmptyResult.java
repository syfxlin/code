/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.design_pattern.facade;

/**
 * 空响应
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 9:09
 */
public class EmptyResult extends AbstractHttpResult {

    @Override
    public String render() {
        return "";
    }
}
