package me.ixk.design_pattern.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器管理
 * <p>
 * 优点：集中式的管理单例，同时可以实现一些特性，如依赖注入等（IoC 容器）
 * <p>
 * 缺点：不知道咋写 2333
 */
public class ContainerSingleton {
    private static final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

    public static void register(Object instance) {
        if (!instances.containsKey(instance.getClass())) {
            instances.put(instance.getClass(), instance);
        }
    }

    public static <T> T get(Class<T> type) {
        return type.cast(instances.get(type));
    }
}
