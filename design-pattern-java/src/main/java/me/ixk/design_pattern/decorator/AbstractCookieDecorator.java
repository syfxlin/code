package me.ixk.design_pattern.decorator;

/**
 * 饼干装饰器抽象
 *
 * @author Otstar Lin
 * @date 2020/12/28 下午 1:41
 */
public abstract class AbstractCookieDecorator implements Cookie {

    private final Cookie cookie;

    public AbstractCookieDecorator(final Cookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public String make() {
        return this.cookie.make();
    }
}
