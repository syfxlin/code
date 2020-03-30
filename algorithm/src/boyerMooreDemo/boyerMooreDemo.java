package boyerMooreDemo;

import java.util.Arrays;
import java.util.Scanner;

/**
 * boyerMooreDemo
 */
public class boyerMooreDemo {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println(boyerMoore("abcacabdc", "abd"));
        System.out.println("你好世界");
        in.close();
    }

    private static final int SIZE = 256;

    private static void generateBC(String str, int[] bc) {
        char[] b = str.toCharArray();
        for (int i = 0; i < SIZE; ++i) {
            bc[i] = -1;
        }
        for (int i = 0; i < b.length; i++) {
            int ascii = (int) b[i];
            bc[ascii] = i;
        }
    }

    private static void generateGS(String find, int[] suffix, boolean[] prefix) {
        char[] b = find.toCharArray();
        for (int i = 0; i < b.length; i++) {
            suffix[i] = -1;
            prefix[i] = false;
        }
        for (int i = 0; i < b.length - 1; i++) {
            int j = i;
            int k = 0;
            while (j >= 0 && b[j] == b[b.length - 1 - k]) {
                j--;
                k++;
                suffix[k] = j + 1;
            }
            if (j == -1)
                prefix[k] = true;
        }
    }

    private static int moveByGS(int i, int m, int[] suffix, boolean[] prefix) {
        int k = m - 1 - i;
        if (suffix[k] != -1)
            return i - suffix[k] + 1;
        for (int r = i + 2; r <= m - 1; ++r) {
            if (prefix[m - r] == true) {
                return r;
            }
        }
        return m;
    }

    public static int boyerMoore(String str, String find) {
        int[] bc = new int[SIZE];
        int fLen = find.length();
        int sLen = str.length();
        int[] suffix = new int[fLen];
        boolean[] prefix = new boolean[fLen];
        generateBC(find, bc);
        generateGS(find, suffix, prefix);
        int index = 0;
        while (index + fLen <= sLen) {
            int i;
            for (i = fLen - 1; i >= 0; i--) {
                if (str.charAt(index + i) != find.charAt(i)) {
                    break;
                }
            }
            if (i < 0) {
                return index;
            }
            int x = i - bc[(int) str.charAt(index + i)];
            int y = 0;
            if (i < fLen - 1) {
                y = moveByGS(i, fLen, suffix, prefix);
            }
            index = index + (x > y ? x : y);
        }
        return -1;
    }
}
