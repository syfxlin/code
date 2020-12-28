package me.ixk.design_pattern.bridge;

/**
 * 事物抽象
 *
 * @author Otstar Lin
 * @date 2020/12/27 下午 3:59
 */
public abstract class AbstractCookie {

    private final CookieType type;

    public AbstractCookie(final CookieType type) {
        this.type = type;
    }

    public CookieType getType() {
        return type;
    }

    public abstract String make();
}
