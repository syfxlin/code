package me.ixk.days.day19;

/**
 * 事务接口
 *
 * @author Otstar Lin
 * @date 2020/12/10 上午 10:34
 */
public interface Transaction {
    /**
     * 从事务中获取值
     *
     * @param ref 事务引用
     * @param <T> 值类型
     *
     * @return 值
     */
    <T> T get(TransactionRef<T> ref);

    /**
     * 从设置值到事务
     *
     * @param ref   事务引用
     * @param value 值
     * @param <T>   值类型
     */
    <T> void set(TransactionRef<T> ref, T value);
}
