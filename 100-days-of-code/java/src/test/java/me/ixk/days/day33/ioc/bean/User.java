package me.ixk.days.day33.ioc.bean;

/**
 * @author Otstar Lin
 * @date 2020/12/24 上午 1:01
 */
public class User {

    private String name;
    private int age;

    public User(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }
}
