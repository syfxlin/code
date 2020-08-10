package me.ixk.design_pattern.singleton;

/**
 * 饿汉式
 * <p>
 * 优点：简单
 * <p>
 * 缺点：当类加载的时候就会实例化，会造成一些不必要的内存消耗
 */
public class SimpleSingleton {
    /**
     * 在类加载的时候就初始化
     */
    private static final SimpleSingleton instance = new SimpleSingleton();

    /**
     * 私有化构造器，使外部无法 new 实例
     */
    private SimpleSingleton() {}

    /**
     * 使用静态方法的方式获取实例
     *
     * @return 单例
     */
    public static SimpleSingleton getInstance() {
        return instance;
    }
}
