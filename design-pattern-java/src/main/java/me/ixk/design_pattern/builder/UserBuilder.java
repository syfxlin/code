package me.ixk.design_pattern.builder;

/**
 * 建造者模式
 *
 * @author Otstar Lin
 * @date 2020/12/23 上午 11:29
 */
public class UserBuilder {

    private String name;
    private int age;

    private UserBuilder() {}

    public UserBuilder(final String name, final int age) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final UserBuilder user = new UserBuilder();

        public Builder name(final String name) {
            user.setName(name);
            return this;
        }

        public Builder age(final int age) {
            user.setAge(age);
            return this;
        }

        public UserBuilder build() {
            return this.user;
        }
    }
}
