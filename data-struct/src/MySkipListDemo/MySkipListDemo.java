package MySkipListDemo;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.Scanner;

/**
 * MySkipListDemo
 */
public class MySkipListDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // SkipList list = new SkipList();
        MySkipList<Integer, Integer> list = new MySkipList<Integer, Integer>();
        list.insert(3, 3);
        list.insert(1, 1);
        list.insert(2, 2);
        list.insert(4, 4);
        list.insert(15, 15);
        list.insert(5, 5);
        list.insert(6, 6);
        list.insert(16, 16);
        list.insert(17, 17);
        list.insert(7, 7);
        list.insert(8, 8);
        list.insert(9, 9);
        list.insert(13, 13);
        list.insert(10, 10);
        list.insert(11, 11);
        list.insert(20, 20);
        list.insert(12, 12);
        list.insert(15, 15);
        list.insert(18, 18);
        list.insert(19, 19);
        list.remove(1, 1);
        list.remove(10, 10);
        list.remove(20, 20);
        list.printAll();
        in.close();
    }
}

/**
 * MySkipList
 */
class MySkipList<K extends Comparable<K>, V extends Comparable<V>> {
    int maxLevel = 16;
    int currLevel = 0;
    boolean keySort = true;
    boolean isMax = false;

    Node head = null;

    private Random r = new Random();

    class Node {
        Node[] next;
        K key;
        V value;
        int level;

        Node() {
        }

        @SuppressWarnings("unchecked")
        Node(K key, V value, int level) {
            this.next = (Node[]) Array.newInstance(Node.class, level);
            this.key = key;
            this.value = value;
            this.level = level;
        }

        public String toString() {
            return "[" + key + "," + value + "](" + level + ")";
        }
    }

    MySkipList() {
    }

    MySkipList(int maxLevel, boolean keySort, boolean isMax) {
        this.maxLevel = maxLevel;
        this.isMax = isMax;
        this.keySort = keySort;
    }

    public int randomLevel() {
        int level = 1;
        for (int i = 1; i < this.maxLevel; ++i) {
            if (this.r.nextInt() % 2 == 1) {
                level++;
            }
        }
        return level;
    }

    public void insert(K key, V value) {
        this.add(key, value);
    }

    @SuppressWarnings("unchecked")
    public void add(K key, V value) {
        if (this.head == null) {
            head = new Node(key, value, maxLevel);
            return;
        }
        if (this.head.key.compareTo(key) == (this.isMax ? -1 : 1)) {
            K tempKey = head.key;
            V tempValue = head.value;
            head.value = value;
            head.key = key;
            value = tempValue;
            key = tempKey;
        }
        int level = this.randomLevel();
        Node p = head;
        Node update[] = (Node[]) Array.newInstance(Node.class, level);
        Node newNode = new Node(key, value, level);
        for (int i = level - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].key.compareTo(key) == (this.isMax ? 1 : -1)) {
                p = p.next[i];
            }
            update[i] = p;
        }
        for (int i = 0; i < level; ++i) {
            newNode.next[i] = update[i].next[i];
            update[i].next[i] = newNode;
        }

        if (currLevel < level)
            currLevel = level;
    }

    @SuppressWarnings("unchecked")
    public void remove(K key, V value) {
        Node p = head;
        if (p.key.compareTo(key) == 0) {
            head.key = head.next[0].key;
            head.value = head.next[0].value;
            Node rNode = head.next[0];
            for (int i = 0; i < rNode.level; i++) {
                head.next[i] = rNode.next[i];
            }
            return;
        }
        Node update[] = (Node[]) Array.newInstance(Node.class, p.level);
        for (int i = p.level - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].key.compareTo(key) == (this.isMax ? 1 : -1)) {
                p = p.next[i];
            }
            update[i] = p;
        }
        if (p.next[0] != null && p.next[0].value.compareTo(value) == 0) {
            Node rNode = p.next[0];
            for (int i = 0; i < rNode.level; i++) {
                update[i].next[i] = rNode.next[i];
            }
        }
    }

    public Node search(K key, V value) {
        Node p = head;
        if (p.key.compareTo(key) == (this.isMax ? -1 : 1)) {
            return null;
        } else if (p.key.compareTo(key) == 0) {
            return p;
        }
        for (int i = p.level - 1; i >= 0; i--) {
            while (p.next[i] != null && p.next[i].key.compareTo(key) == (this.isMax ? 1 : -1)) {
                p = p.next[i];
            }
        }
        if (p.next[0] != null && p.next[0].value.compareTo(value) == 0) {
            return p.next[0];
        } else {
            return null;
        }
    }

    public void printAll() {
        Node p = head;
        while (p != null) {
            System.out.println(p);
            p = p.next[0];
        }
    }
}