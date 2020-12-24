package me.ixk.days.day33.ioc.factory;

/**
 * Bean 生产者
 *
 * @author Otstar Lin
 * @date 2020/12/23 下午 9:02
 */
public interface FactoryBean<T> {
    /**
     * 获取对象
     *
     * @return 对象
     * @throws Exception 异常
     */
    T getObject() throws Exception;

    /**
     * 获取对象类型
     *
     * @return 对象类型
     */
    Class<?> getObjectType();
}
