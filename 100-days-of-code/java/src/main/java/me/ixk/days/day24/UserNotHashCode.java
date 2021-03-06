package me.ixk.days.day24;

import java.util.Objects;

/**
 * 测试实体类（未重写 hashCode 方法）
 *
 * @author Otstar Lin
 * @date 2020/12/15 上午 8:23
 */
public class UserNotHashCode {

    private final String name;
    private final int age;

    public UserNotHashCode(final String name, final int age) {
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
        final UserNotHashCode that = (UserNotHashCode) o;
        return age == that.age && Objects.equals(name, that.name);
    }
}
