package me.ixk.days.day10;

/**
 * 可见性问题解决方案
 *
 * @author Otstar Lin
 * @date 2020/11/30 下午 9:59
 */
public class VisibilitySolve {

    /**
     * 只要添加 volatile 使 Java 禁用 CPU 缓存，这样 loop 方法就能实时读取到 stop 变量的变化
     */
    private volatile boolean stop = false;
    private int count = 0;

    public void start() {
        stop = true;
    }

    public void loop() {
        while (!stop) {
            count++;
        }
    }

    @Override
    public String toString() {
        return "VisibilityProblem{" + "stop=" + stop + ", count=" + count + '}';
    }
}
