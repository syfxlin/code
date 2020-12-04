package me.ixk.days.day13;

import java.util.concurrent.CountDownLatch;

/**
 * 中断解决
 *
 * @author Otstar Lin
 * @date 2020/12/4 上午 10:28
 */
public class InterruptedSolve extends Thread {

    private volatile boolean isRunning = true;
    private final CountDownLatch count;

    public InterruptedSolve(final CountDownLatch count) {
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
                // break; // 处理方法一：发生异常可以考虑直接退出
                this.interrupt(); // 处理方法二：可以重新设置 interrupt 标志位（相对推荐的方法）
                // 处理方法三：在循环外捕获 InterruptedException
            }
        }
    }

    public void setRunning(final boolean running) {
        this.isRunning = running;
    }
}
