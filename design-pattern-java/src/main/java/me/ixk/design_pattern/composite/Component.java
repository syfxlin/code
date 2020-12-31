package me.ixk.design_pattern.composite;

/**
 * @author Otstar Lin
 * @date 2020/12/31 下午 12:59
 */
public interface Component {
    /**
     * 获取名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 获取子元素
     *
     * @return 子元素
     */
    Component[] getSubs();
}
