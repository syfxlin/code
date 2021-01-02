package me.ixk.design_pattern.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Otstar Lin
 * @date 2021/1/2 下午 8:52
 */
public class EmailServer implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(final Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void detach(final Observer observer) {
        this.observers.remove(observer);
    }

    public void send(final String msg) {
        for (final Observer observer : this.observers) {
            observer.change(msg, this);
        }
    }
}
