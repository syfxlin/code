package me.ixk.days.day33.ioc.bean;

/**
 * @author Otstar Lin
 * @date 2020/12/24 上午 9:04
 */
public class TypeUser {

    private final User user;

    public TypeUser(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
