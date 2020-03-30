package mergeSortDemo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class mergeSortDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = in.nextInt();
            }
            mergeSort(nums, false);
            System.out.println(Arrays.toString(nums));
        }
        in.close();
    }

    public static void mergeSort(int[] arr) {
        mergeSort(arr, true);
    }

    public static void mergeSort(int[] arr, boolean lowToHigh) {
        int[] temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, temp, lowToHigh);
    }

    private static void mergeSort(int[] arr, int left, int right, int[] temp, boolean lowToHigh) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, temp, lowToHigh);// 左边归并排序，使得左子序列有序
            mergeSort(arr, mid + 1, right, temp, lowToHigh);// 右边归并排序，使得右子序列有序
            merge(arr, left, mid, right, temp, lowToHigh);// 将两个有序子数组合并操作
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, int[] temp, boolean lowToHigh) {
        int i = left;// 左序列指针
        int j = mid + 1;// 右序列指针
        int t = 0;// 临时数组指针
        while (i <= mid && j <= right) {
            if ((arr[i] <= arr[j]) ^ !lowToHigh) {
                temp[t++] = arr[i++];
            } else {
                temp[t++] = arr[j++];
            }
        }
        while (i <= mid) {// 将左边剩余元素填充进temp中
            temp[t++] = arr[i++];
        }
        while (j <= right) {// 将右序列剩余元素填充进temp中
            temp[t++] = arr[j++];
        }
        t = 0;
        // 将temp中的元素全部拷贝到原数组中
        while (left <= right) {
            arr[left++] = temp[t++];
        }
    }
}