package me.ixk.days.day26;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cn.hutool.core.date.StopWatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/17 上午 10:45
 */
class Day26Test {

    @Test
    void asList() {
        // 注意 asList 不能传入基本类型，不然就会变成一个 item
        final List<int[]> l1 = Arrays.asList(new int[] { 1, 2, 3 });
        assertNotSame(int.class, l1.get(0).getClass());
        // Java8 后可以使用 Stream 来封装基本类型到 List
        final List<Integer> l2 = Arrays
            .stream(new int[] { 1, 2, 3 })
            .boxed()
            .collect(Collectors.toList());
        assertSame(Integer.class, l2.get(0).getClass());
        // asList 的列表不支持修改操作
        final List<Integer> l3 = Arrays.asList(1, 2, 3);
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
                l3.add(4);
            }
        );
        // 应该使用 new ArrayList 包一层
        final ArrayList<Integer> l4 = new ArrayList<>(Arrays.asList(1, 2, 3));
        l4.add(4);
        assertEquals(4, l4.size());
    }

    @Test
    void benchmark() {
        // LinkedList 随机插入的性能其实非常差，因为需要找到指定位置的指针
        final int elementSize = 100000;
        final int loopSize = 100000;
        final StopWatch watch = new StopWatch();

        final LinkedList<Integer> l1 = IntStream
            .rangeClosed(1, elementSize)
            .boxed()
            .collect(Collectors.toCollection(LinkedList::new));
        watch.start("LinkedList get start");
        ListBenchmark.linkedListGet(l1, loopSize);
        watch.stop();

        final ArrayList<Integer> l2 = IntStream
            .rangeClosed(1, elementSize)
            .boxed()
            .collect(Collectors.toCollection(ArrayList::new));
        watch.start("ArrayList get start");
        ListBenchmark.arrayListGet(l2, loopSize);
        watch.stop();

        watch.start("LinkedList set start");
        ListBenchmark.linkedListAdd(l1, loopSize);
        watch.stop();

        watch.start("ArrayList set start");
        ListBenchmark.arrayListAdd(l2, loopSize);
        watch.stop();

        System.out.println(watch.prettyPrint());
    }
}
