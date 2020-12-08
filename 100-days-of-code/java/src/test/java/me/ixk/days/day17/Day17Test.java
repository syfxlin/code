package me.ixk.days.day17;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/8 上午 8:34
 */
class Day17Test {

    @Test
    void make() throws ExecutionException, InterruptedException {
        final Future<Void> make = Tea.make();
        make.get();
    }
}
