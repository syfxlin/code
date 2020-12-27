package me.ixk.design_pattern.bridge;

/**
 * @author Otstar Lin
 * @date 2020/12/27 下午 4:01
 */
public class Cookie extends AbstractCookie {

    public Cookie(final CookieType type) {
        super(type);
    }

    @Override
    public String make() {
        return String.format("制作了一个%s的饼干", this.getType().getType());
    }
}
