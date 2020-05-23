package bucketSortDemo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class bucketSortDemo {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        double[] arr = { 8, 5, 6, 4, 3, 9, 7, 2, 1, 8, 5, 6, 4, 3, 9, 7, 2, 1 };
        System.out.println(Arrays.toString(bucketSort(arr, true, 5)));
        in.close();
    }

    public static Double[] bucketSort(
        double[] arr,
        boolean lowToHigh,
        int bucketCount
    ) {
        double min = arr[0];
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
            min = Math.min(min, arr[i]);
        }
        double space = (max - min + 1) / bucketCount;
        LinkedList<Double>[] buckets = new LinkedList[bucketCount];
        for (double v : arr) {
            int index = (int) Math.floor((v - min) / space);
            if (buckets[index] == null) {
                buckets[index] = new LinkedList<>();
                buckets[index].add(v);
            } else {
                LinkedList<Double> tem = buckets[index];
                for (int j = 0; j < tem.size(); j++) {
                    if (
                        tem.get(j).compareTo(v) == (lowToHigh ? 1 : -1) ||
                        tem.get(j).compareTo(v) == 0
                    ) {
                        tem.add(j, v);
                        break;
                    }
                }
            }
        }
        LinkedList<Double> res = new LinkedList<>();
        for (LinkedList<Double> var : buckets) {
            res.addAll(var);
        }
        return res.toArray(new Double[0]);
    }
}
