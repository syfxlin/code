package me.ixk.days.day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWrite 容器测试
 *
 * @author Otstar Lin
 * @date 2020/12/9 下午 7:20
 */
public class CopyOnWrite {

    private final List<Integer> cow = new CopyOnWriteArrayList<>();
    private final List<Integer> sync = Collections.synchronizedList(
        new ArrayList<>()
    );

    public void addCow(final int value) {
        this.cow.add(value);
    }

    public int getCow(final int index) {
        return this.cow.get(index);
    }

    public void addSync(final int value) {
        this.sync.add(value);
    }

    public int getSync(final int index) {
        return this.sync.get(index);
    }
}
