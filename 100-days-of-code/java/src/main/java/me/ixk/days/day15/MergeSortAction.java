package me.ixk.days.day15;

import java.util.concurrent.RecursiveAction;

/**
 * 归并任务
 *
 * @author Otstar Lin
 * @date 2020/12/6 下午 3:39
 */
public class MergeSortAction extends RecursiveAction {

    private static final int THRESHOLD = 6000;
    private final int[] arr;
    private final int[] temp;
    private final int left;
    private final int right;

    public MergeSortAction(int[] arr) {
        this.arr = arr;
        this.temp = new int[this.arr.length];
        this.left = 0;
        this.right = this.arr.length - 1;
    }

    public MergeSortAction(int[] arr, int[] temp, int left, int right) {
        this.arr = arr;
        this.temp = temp;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        // 低于一定数量则不再 fork，避免创建过多的线程，直接归并
        if (right - left < THRESHOLD) {
            sort(arr, temp, left, right);
        } else if (left < right) {
            int mid = (left + right) / 2;
            final MergeSortAction worker1 = new MergeSortAction(
                arr,
                temp,
                left,
                mid
            );
            final MergeSortAction worker2 = new MergeSortAction(
                arr,
                temp,
                mid + 1,
                right
            );
            invokeAll(worker1, worker2);
            merge(arr, temp, left, right);
        }
    }

    public static void sort(
        final int[] array,
        final int[] tmp,
        final int left,
        final int right
    ) {
        if (left >= right) {
            return;
        }

        final int middle = left + (right - left) / 2;

        sort(array, tmp, left, middle);
        sort(array, tmp, middle + 1, right);

        merge(array, tmp, left, right);
    }

    public static void merge(
        final int[] array,
        final int[] tmp,
        int left,
        final int right
    ) {
        System.arraycopy(array, left, tmp, left, right + 1 - left);

        int k = left;
        final int mid = left + (right - left) / 2;
        int j = mid + 1;
        while (k <= right) {
            if (left > mid) {
                array[k++] = tmp[j++];
            } else if (j > right) {
                array[k++] = tmp[left++];
            } else if (tmp[left] < tmp[j]) {
                array[k++] = tmp[left++];
            } else {
                array[k++] = tmp[j++];
            }
        }
    }
}
