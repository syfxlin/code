package MyArrayListDemo;

import java.util.Arrays;
import java.util.Scanner;

public class MyArrayListDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        MyArrayList<Integer> list = new MyArrayList<Integer>();
        int[] arr = { 3, 0, 10, 1, 2, 4, 6, 7, 5, 9, 8 };
        System.out.println(Arrays.toString(arr));
        for (int i = 0; i < arr.length; i++) {
            list.addSort(arr[i]);
        }
        // list.remove(0);
        // list.remove(5);
        // list.remove(10);
        System.out.println(list);
        System.out.println();
        in.close();
    }
}

class MyArrayList<T extends Comparable<T>> {
    Object[] arr;

    public MyArrayList(int n) {
        arr = new Object[n];
    }

    public MyArrayList() {
        this(16);
    }

    public void add(T data) {
        this.add(-2, data);
    }

    public void add(int i, T data) {
        if (i == -2) {
            int j = 0;
            for (j = arr.length - 1; j >= 0; j--) {
                if (arr[j] != null) {
                    break;
                }
            }
            i = j + 1;
        }
        if (i >= arr.length || arr[arr.length - 1] != null) {
            Object[] old = arr;
            arr = new Object[i * 2];
            for (int j = 0; j < old.length; j++) {
                arr[j] = old[j];
            }
        }
        int index = i;
        Object tempData1 = arr[index];
        while (arr[index] != null) {
            Object tempData2 = arr[index + 1];
            arr[index + 1] = tempData1;
            tempData1 = tempData2;
            index++;
        }
        arr[i] = data;
    }

    public void set(int i, T data) {
        if (i >= arr.length) {
            this.add(i, data);
        } else {
            this.arr[i] = data;
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int i) {
        return (T) this.arr[i];
    }

    public void remove(int i) {
        if (i >= arr.length || arr[i] == null) {
            return;
        }
        if (i == arr.length - 1) {
            arr[i] = null;
        }
        while (i + 1 < arr.length) {
            arr[i] = arr[i + 1];
            i++;
        }
    }

    public void addSort(T data) {
        this.addSort(true, data);
    }

    @SuppressWarnings("unchecked")
    public void addSort(boolean lowToHigh, T data) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null || ((T) arr[i]).compareTo(data) == (lowToHigh ? 1 : -1)) {
                this.add(i, data);
                return;
            }
        }
    }

    public void resever() {
        for (int i = 0; i < arr.length / 2; i++) {
            Object tempData = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = tempData;
        }
    }

    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object var : arr) {
            if (var != null) {
                sb.append(((T) var).toString() + ", ");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public int binarySearch(T key) {
        return this.binarySearch(key, -1, true);
    }

    @SuppressWarnings("unchecked")
    public int binarySearch(T key, int index, boolean lowToHigh) {
        int start = 0;
        int end = arr.length - 1;
        while (start + 1 < end) {
            int mid = start + (end - start) / 2;
            if (arr[mid] != null && ((T) arr[mid]).compareTo(key) == 0) {
                if (index == -1) {
                    end = mid;
                } else if (index == 1) {
                    start = mid;
                }
            } else if (arr[mid] != null && ((T) arr[mid]).compareTo(key) == (lowToHigh ? -1 : 1)) {
                start = mid;
            } else {
                end = mid;
            }
        }
        if (arr[start] != null && ((T) arr[start]).compareTo(key) == 0) {
            return start;
        }
        if (arr[end] != null && ((T) arr[end]).compareTo(key) == 0) {
            return end;
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.arr.length == 0;
    }

    public int size() {
        int len = this.arr.length;
        while (len > 0 && this.arr[len - 1] == null) {
            len--;
        }
        return len;
    }

    public void clear() {
        int i = 0;
        while (i < arr.length && arr[i] != null) {
            arr[i] = null;
            i++;
        }
    }
}