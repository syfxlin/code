package me.ixk.days.day22;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 错误使用并发工具类导致线程不安全问题
 *
 * @author Otstar Lin
 * @date 2020/12/13 下午 1:52
 */
public class ConcurrentUtilProblem {

    public static final int SIZE = 1000;
    /**
     * 虽然并发工具类里的操作都是线程安全的，但是组合起来就不是了，我们需要对这些组合操作进行另外加锁或者使用并发工具类里的对应方法进行操作，以保证线程安全
     */
    private final ConcurrentMap<Integer, Integer> map = new ConcurrentHashMap<>();

    public void problemAdd() {
        for (int i = 0; i < SIZE; i++) {
            if (map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }
    }

    public void solveAdd() {
        for (int i = 0; i < SIZE; i++) {
            map.compute(
                i,
                (k, v) -> {
                    if (v == null) {
                        return 1;
                    } else {
                        return v + 1;
                    }
                }
            );
        }
    }

    public void clear() {
        map.clear();
    }

    public ConcurrentMap<Integer, Integer> getMap() {
        return map;
    }
}
