package me.ixk.days.day24;

/**
 * 测试实体类（未重写 equals 和 hashCode 方法）
 *
 * @author Otstar Lin
 * @date 2020/12/15 上午 8:21
 */
public class UserNotEqualsAndHashCode {

    private final String name;
    private final int age;

    public UserNotEqualsAndHashCode(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
