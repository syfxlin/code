package me.ixk.design_pattern.singleton;

/**
 * 双重检查锁
 * <p>
 * 优点：避免每次获取实例的时候都获取锁，避免不必要的性能浪费
 * <p>
 * 缺点：第一次加载稍慢
 */
public class DclSingleton {
    private static DclSingleton instance;

    private DclSingleton() {}

    public static DclSingleton getInstance() {
        // 第一层检查避免每次 getInstance 时都获取锁，获取锁会造成性能浪费
        if (instance == null) {
            // 如果 instance 还未创建就尝试获取锁
            synchronized (DclSingleton.class) {
                // 第二次检查避免多次实例化
                // 如：A 线程获取到锁，开始实例化
                //    B 线程等待获取锁
                //    A 线程实例化单例完成，释放锁，此时 instance 不为 null
                //    B 线程获取到锁，开始实例化，如果没有第二次检查则会再次实例化单例
                //      如果有了第二次检查那么此时 instance 不为 null，则不会再次实例化
                if (instance == null) {
                    instance = new DclSingleton();
                }
            }
        }
        return instance;
    }
}
