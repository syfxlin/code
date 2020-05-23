package quickSortDemo;

import java.util.Scanner;

public class quickSortDemo {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = in.nextInt();
            }
            quickSort(nums, false);
            // Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                if ((i + 1) % 100 == 0 || i == nums.length - 1) {
                    System.out.println(nums[i]);
                } else {
                    System.out.print(nums[i] + " ");
                }
            }
        }
        in.close();
    }

    public static void quickSort(int[] nums) {
        quickSort(0, nums.length - 1, nums, true);
    }

    public static void quickSort(int[] nums, boolean lowToHigh) {
        quickSort(0, nums.length - 1, nums, lowToHigh);
    }

    public static void quickSort(
        int left,
        int right,
        int[] nums,
        boolean lowToHigh
    ) {
        int l = left;
        int r = right;
        int p = nums[(left + right) / 2];
        while (l <= r) {
            while ((p > nums[l] && lowToHigh) || (p < nums[l] && !lowToHigh)) {
                l++;
            }
            while ((p < nums[r] && lowToHigh) || (p > nums[r] && !lowToHigh)) {
                r--;
            }
            if (l < r) {
                int temp = nums[r];
                nums[r] = nums[l];
                nums[l] = temp;
                l++;
                r--;
            } else if (l == r) {
                l++;
            }
        }
        if (left < r) {
            quickSort(left, r, nums, lowToHigh);
        }
        if (l < right) {
            quickSort(l, right, nums, lowToHigh);
        }
    }

    public static void quickSortF(int top, int tail, int[] nums) {
        if (top >= tail) return;
        int mid = nums[tail];
        int left = top, right = tail - 1;
        while (left < right) {
            while (nums[left] < mid && left < right) {
                left++;
            }
            while (nums[right] >= mid && left < right) {
                right--;
            }
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
        }
        if (nums[left] >= nums[tail]) {
            int temp = nums[left];
            nums[left] = nums[tail];
            nums[tail] = temp;
        } else {
            left++;
        }
        quickSortF(top, left - 1, nums);
        quickSortF(left + 1, tail, nums);
    }
}
