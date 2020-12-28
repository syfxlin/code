package me.ixk.design_pattern.decorator;

/**
 * 饼干加糖装饰器
 *
 * @author Otstar Lin
 * @date 2020/12/28 下午 1:43
 */
public class SugarCookie extends AbstractCookieDecorator {

    public SugarCookie(final Cookie cookie) {
        super(cookie);
    }

    @Override
    public String make() {
        return super.make() + "加糖";
    }
}
