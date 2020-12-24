package me.ixk.days.day33.ioc.factory;

/**
 * Object 工厂
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 9:56
 */
@FunctionalInterface
public interface ObjectFactory<T> {
    /**
     * 获取对象
     *
     * @return 获取对象
     */
    T getObject();
}
