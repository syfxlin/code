package me.ixk.days.day22;

/**
 * ThreadLocal 使用不当导致线程不安全问题
 *
 * @author Otstar Lin
 * @date 2020/12/13 下午 1:33
 */
public class ThreadLocalProblem {

    /**
     * ThreadLocal 虽然能保证在变量在线程间的隔离，但是当线程复用（最典型的场景就是线程池）的时候，如果未将 ThreadLocal
     * 清空，则会导致下一次使用的时候读取到上一次使用的值。
     * <p>
     * 解决方案其实很简单，保证在使用前或使用完后调用 ThreadLocal.remove 方法清除当前线程中的 ThreadLocal
     * 值，这样在下一次使用的时候就会重新调用 initial 初始化 ThreadLocal 或者返回空值
     */
    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(
        () -> "init"
    );

    public String problem() {
        final String before = THREAD_LOCAL.get();
        THREAD_LOCAL.set("update");
        final String after = THREAD_LOCAL.get();
        return String.format("Before: %s, After: %s", before, after);
    }

    public String solve() {
        THREAD_LOCAL.remove();
        return this.problem();
    }
}
