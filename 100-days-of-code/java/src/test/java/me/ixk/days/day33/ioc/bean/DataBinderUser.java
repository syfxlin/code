package me.ixk.days.day33.ioc.bean;

/**
 * @author Otstar Lin
 * @date 2020/12/24 上午 9:13
 */
public class DataBinderUser {

    private final String name;

    public DataBinderUser(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
