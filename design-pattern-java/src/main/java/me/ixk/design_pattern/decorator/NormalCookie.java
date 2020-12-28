package me.ixk.design_pattern.decorator;

/**
 * 正常的饼干，即未被装饰的实体
 *
 * @author Otstar Lin
 * @date 2020/12/28 下午 1:40
 */
public class NormalCookie implements Cookie {

    @Override
    public String make() {
        return "制作了一个普通的饼干";
    }
}
