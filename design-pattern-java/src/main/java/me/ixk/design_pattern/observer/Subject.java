package me.ixk.design_pattern.observer;

/**
 * 发布者
 *
 * @author Otstar Lin
 * @date 2021/1/2 下午 8:49
 */
public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);
}
