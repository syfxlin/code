package me.ixk.design_pattern.observer;

/**
 * 观察者
 *
 * @author Otstar Lin
 * @date 2021/1/2 下午 8:46
 */
public interface Observer {
    void change(Object data, Subject subject);
}
