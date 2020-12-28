package me.ixk.design_pattern.bridge;

/**
 * 加糖类型
 *
 * @author Otstar Lin
 * @date 2020/12/27 下午 4:01
 */
public class SugarCookieType implements CookieType {

    @Override
    public String getType() {
        return "加糖";
    }
}
