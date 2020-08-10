package me.ixk.design_pattern.singleton;

/**
 * 静态内部类
 * <p>
 * 优点：简单易用，同时实现了懒加载
 * <p>
 * 缺点：如果构造函数带参，则无法很好的注入参数
 */
public class StaticClassSingleton {

    private StaticClassSingleton() {}

    public static StaticClassSingleton getInstance() {
        // 当第一次调用该方法的时候，JVM 会加载 Inner 内部类，此时才会实例化单例
        // 线程安全由 JVM 提供
        return Inner.instance;
    }

    private static class Inner {
        private static final StaticClassSingleton instance = new StaticClassSingleton();
    }
}
