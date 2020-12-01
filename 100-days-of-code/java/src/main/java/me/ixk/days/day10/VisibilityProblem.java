package me.ixk.days.day10;

/**
 * 可见问题触发
 *
 * @author Otstar Lin
 * @date 2020/11/30 下午 9:32
 */
public class VisibilityProblem {

    private boolean stop = false;
    private int count = 0;

    public void stop() {
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
