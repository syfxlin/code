package binarySearchDemo;

public class binarySearchDemo {
    public static void main(String[] args) {
        int[] nums = { 1, 2, 3, 3, 3, 4, 5, 6, 7, 8, 9 };
        int index = binarySearchFirst(nums, 3, true);
        System.out.println(index);
    }

    /**
     * 二分查找
     * 
     * @param nums  : 要进行搜索的数组
     * @param key   : 要寻找的数
     * @param index : 查找的是第一次出现的位置还是最后一次出现的位置 (第一次出现：-1，第二次出现：1)
     * @return : 返回在数组中与 key 值相等的数的位置，若未找到则返回 -1
     */
    public static int binarySearch(int[] nums, int key) {
        return binarySearch(nums, key, true, true);
    }

    public static int binarySearchFirst(int[] nums, int key) {
        return binarySearch(nums, key, true, true);
    }

    public static int binarySearchLast(int[] nums, int key) {
        return binarySearch(nums, key, false, true);
    }

    public static int binarySearchFirst(int[] nums, int key, boolean lowToHigh) {
        return binarySearch(nums, key, true, lowToHigh);
    }

    public static int binarySearchLast(int[] nums, int key, boolean lowToHigh) {
        return binarySearch(nums, key, false, lowToHigh);
    }

    public static int binarySearch(int[] nums, int key, boolean findFirst, boolean lowToHigh) {
        // 定位start指针
        int start = 0;
        // 定位end指针
        int end = nums.length - 1;
        // 循环二分
        while (start + 1 < end) {
            // 定位mid指针，这里不直接使用相加然后相除是为了防止溢出，实际上也不会有这么大
            int mid = start + (end - start) / 2;
            // 判断mid位置上的数据是否等于key
            if (nums[mid] == key) {
                // 若等于，并且是查找第一次出现的位置则继续向前二分
                if (findFirst ^ !lowToHigh) {
                    // 移动end指针屏蔽后面的数据
                    if (lowToHigh) {
                        end = mid;
                    } else {
                        start = mid;
                    }
                } else { // 若是查找最后一次出现的位置则继续向后二分
                    // 移动start指针屏蔽前面的数据
                    if (lowToHigh) {
                        start = mid;
                    } else {
                        end = mid;
                    }
                }
            } else if (nums[mid] < key) { // 若mid位置的数据比key小，则只有在mid后方才有可能找到key
                // 移动start指针屏蔽前面的数据
                if (lowToHigh) {
                    start = mid;
                } else {
                    end = mid;
                }
            } else {
                // 否则屏蔽后面的数据
                if (lowToHigh) {
                    end = mid;
                } else {
                    start = mid;
                }
            }
        }
        // 在经过上面的二分之后，start指针和end指针就相邻了，这时只需判断与key相等的是start所指的数据还是end所指的数据
        if (nums[start] == key) {
            return start;
        }
        if (nums[end] == key) {
            return end;
        }
        return -1;
    }
}