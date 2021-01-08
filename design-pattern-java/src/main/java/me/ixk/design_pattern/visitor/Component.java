package me.ixk.design_pattern.visitor;

/**
 * @author Otstar Lin
 * @date 2021/1/8 下午 7:35
 */
public interface Component {
    String accept(Visitor visitor);
}
