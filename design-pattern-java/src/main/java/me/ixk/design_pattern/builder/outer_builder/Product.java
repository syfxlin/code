package me.ixk.design_pattern.builder.outer_builder;

/**
 * 一些配置或特征
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 1:03
 */
public class Product {

    private final String name;

    public Product(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
