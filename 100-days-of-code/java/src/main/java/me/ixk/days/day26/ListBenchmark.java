package me.ixk.days.day26;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * @author Otstar Lin
 * @date 2020/12/17 上午 11:10
 */
public class ListBenchmark {

    public static void linkedListGet(LinkedList<Integer> list, int loopCount) {
        IntStream
            .rangeClosed(1, loopCount)
            .forEach(
                i -> list.get(ThreadLocalRandom.current().nextInt(list.size()))
            );
    }

    public static void arrayListGet(ArrayList<Integer> list, int loopCount) {
        IntStream
            .rangeClosed(1, loopCount)
            .forEach(
                i -> list.get(ThreadLocalRandom.current().nextInt(list.size()))
            );
    }

    public static void linkedListAdd(LinkedList<Integer> list, int loopCount) {
        IntStream
            .rangeClosed(1, loopCount)
            .forEach(
                i ->
                    list.add(
                        ThreadLocalRandom.current().nextInt(list.size()),
                        1
                    )
            );
    }

    public static void arrayListAdd(ArrayList<Integer> list, int loopCount) {
        IntStream
            .rangeClosed(1, loopCount)
            .forEach(
                i ->
                    list.add(
                        ThreadLocalRandom.current().nextInt(list.size()),
                        1
                    )
            );
    }
}
