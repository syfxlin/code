package shellSortDemo;

import java.util.Arrays;

public class shellSortDemo {
    public static void main(String[] args) {
        int[] nums = new int[] { 9, 8, 7, 6, 5, 4, 3, 3, 3, 2, 1 };
        shellSort(nums);
        System.out.println(Arrays.toString(nums));
    }

    public static void shellSort(int[] arr) {
        shellSort(arr, true);
    }

    public static void shellSort(int[] arr, boolean lowToHigh) {
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                while (j - gap >= 0 && ((arr[j] < arr[j - gap]) ^ !lowToHigh)) {
                    swap(arr, j, j - gap);
                    j -= gap;
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}