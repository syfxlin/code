package me.ixk.design_pattern.singleton;

/**
 * 懒汉式
 * <p>
 * 优点：懒加载，减少内存的使用
 * <p>
 * 缺点：锁的粒度太粗了，造成了不必要的性能浪费
 */
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    public static synchronized LazySingleton getInstance() {
        // 当第一次调用的时候再初始化类，实现懒加载
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
