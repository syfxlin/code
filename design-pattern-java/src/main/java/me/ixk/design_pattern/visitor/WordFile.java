package me.ixk.design_pattern.visitor;

/**
 * @author Otstar Lin
 * @date 2021/1/8 下午 7:38
 */
public class WordFile implements Component {

    @Override
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }
}
