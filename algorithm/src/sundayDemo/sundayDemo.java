package sundayDemo;

import java.util.Arrays;

public class sundayDemo {

    public static void main(String[] args) {
        String needle = "world";
        String haystack = "This is a string (hello world)";
        System.out.println(find(haystack, needle));
        System.out.println(find(haystack, "abc"));
    }

    public static int find(String haystack, String needle) {
        int[] needleMap = new int[255];
        Arrays.fill(needleMap, -1);
        int tLen = needle.length();
        int sLen = haystack.length();
        for (int i = 0; i < tLen; i++) {
            needleMap[needle.charAt(i)] = i;
        }
        int index = 0;
        while (index + tLen <= sLen) {
            int j = 0;
            while (j < tLen && haystack.charAt(index + j) == needle.charAt(j)) {
                j++;
                if (j >= tLen) {
                    return index;
                }
            }
            index += tLen - needleMap[haystack.charAt(index + tLen)];
        }
        return -1;
    }
}
