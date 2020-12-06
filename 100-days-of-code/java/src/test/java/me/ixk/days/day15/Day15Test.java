package me.ixk.days.day15;

import cn.hutool.core.util.ArrayUtil;
import java.util.concurrent.ForkJoinPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/6 下午 3:50
 */
@Slf4j
class Day15Test {

    @Test
    void sort() {
        final ForkJoinPool pool = new ForkJoinPool();
        final int[] arr = ArrayUtil.reverse(ArrayUtil.range(1000000));
        log.info("{}", arr);
        final MergeSortAction task = new MergeSortAction(arr);
        pool.invoke(task);
        log.info("{}", arr);
    }
}
