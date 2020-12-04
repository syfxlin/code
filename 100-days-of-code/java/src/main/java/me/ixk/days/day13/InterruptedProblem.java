package me.ixk.days.day13;

import java.util.concurrent.CountDownLatch;

/**
 * 中断问题
 *
 * @author Otstar Lin
 * @date 2020/12/4 上午 10:28
 */
public class InterruptedProblem extends Thread {

    private volatile boolean isRunning = true;
    private final CountDownLatch count;

    public InterruptedProblem(final CountDownLatch count) {
        this.count = count;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (this.isInterrupted()) {
                // 如果检测到中断信号，则直接退出循环
                count.countDown();
                break;
            }
            // do something
            try {
                Thread.sleep(5000L);
            } catch (final InterruptedException e) {
                // 处理异常
                // Java 在发生 InterruptedException 的时候会重置 interrupted 标志，此时如果未做好处理则会发生中断失败的问题
            }
        }
    }

    public void setRunning(final boolean running) {
        this.isRunning = running;
    }
}
