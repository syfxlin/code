package me.ixk.design_pattern.singleton;

/**
 * 枚举单例
 * <p>
 * 优点：只会加载一次不会因为反射而破坏单例
 * <p>
 * 缺点：难以理解
 */
public enum EnumSingleton {
    INSTANCE,;

    private String name = "syfxlin";

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
