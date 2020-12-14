package me.ixk.days.day23;

/**
 * @author Otstar Lin
 * @date 2020/12/14 下午 1:53
 */
public interface CallInProxy {
    /**
     * 方法一
     *
     * @return 返回值
     */
    String call1();

    /**
     * 方法二
     *
     * @return 返回值
     */
    String call2();

    /**
     * 设置代理对象
     *
     * @param self 代理对象
     */
    default void setSelf(CallInProxy self) {}
}
