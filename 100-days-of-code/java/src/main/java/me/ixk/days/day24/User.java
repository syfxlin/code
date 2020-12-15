package me.ixk.days.day24;

import java.util.Objects;

/**
 * 测试实体类
 *
 * @author Otstar Lin
 * @date 2020/12/15 上午 8:22
 */
public class User {

    private final String name;
    private final int age;

    public User(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return age == user.age && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
