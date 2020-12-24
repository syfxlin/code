package me.ixk.days.day33.ioc.bean;

import me.ixk.days.day33.annotations.Autowired;

/**
 * @author Otstar Lin
 * @date 2020/12/24 上午 1:02
 */
public class AutoWiredTest {

    @Autowired
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
