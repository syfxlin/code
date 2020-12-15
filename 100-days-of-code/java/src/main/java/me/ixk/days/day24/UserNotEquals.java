package me.ixk.days.day24;

import java.util.Objects;

/**
 * 测试实体类（未重写 equals）
 *
 * @author Otstar Lin
 * @date 2020/12/15 上午 8:52
 */
public class UserNotEquals {

    private final String name;
    private final int age;

    public UserNotEquals(final String name, final int age) {
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
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
