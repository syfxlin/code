package bucketSortDemo;

import java.util.ArrayList;
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

    public static Double[] bucketSort(double[] arr, boolean lowToHigh, int bucketCount) {
        double min = arr[0];
        double max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = max > arr[i] ? max : arr[i];
            min = min < arr[i] ? min : arr[i];
        }
        double space = (max - min + 1) / bucketCount;
        LinkedList<Double>[] buckets = new LinkedList[bucketCount];
        for (int i = 0; i < arr.length; i++) {
            int index = (int) Math.floor((arr[i] - min) / space);
            if (buckets[index] == null) {
                buckets[index] = new LinkedList<Double>();
                buckets[index].add(arr[i]);
            } else {
                LinkedList<Double> tem = buckets[index];
                for (int j = 0; j < tem.size(); j++) {
                    if (tem.get(j).compareTo(arr[i]) == (lowToHigh ? 1 : -1) || tem.get(j).compareTo(arr[i]) == 0) {
                        tem.add(j, arr[i]);
                        break;
                    }
                }
            }
        }
        LinkedList<Double> res = new LinkedList<Double>();
        for (LinkedList<Double> var : buckets) {
            res.addAll(var);
        }
        return res.toArray(new Double[0]);
    }
}