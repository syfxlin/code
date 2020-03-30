package MyLinkedListDemo;

import java.util.Scanner;

public class MyLinkedListDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            if (n == 0) {
                break;
            }
            MyLinkedList<Integer> list = new MyLinkedList<Integer>();
            for (int i = 0; i < n; i++) {
                list.add(in.nextInt());
            }
            list.quickSort();
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    System.out.println(list.get(i));
                } else {
                    System.out.print(list.get(i) + " ");
                }
            }
        }
        in.close();
    }
}

class MyLinkedList<T extends Comparable<T>> {

    // 链表总长度
    int len = 0;
    // 头节点
    Node head = null;
    // 尾节点
    Node end = null;

    // 链表节点类（内部类）
    class Node {
        // 上一个节点
        Node prev;
        // 下一个节点
        Node next;
        // 数据块
        T data;

        // 空构造器，用于构造临时节点和定位节点等不需要使用数据块的节点
        Node() {
        };

        // 可填入数据的构造器，用填入数据
        Node(T data) {
            // 填入数据到数据块
            this.data = data;
        }
    }

    /**
     * 插入链表节点
     * 
     * @param i    表示要插入的位置，新节点会添加至该位置之后，当 i = -1 时新节点会被放置头位置，若 i = -2 时新节点会被放置至尾位置
     * @param data 数据
     */
    public void add(int i, T data) {
        // 递增定位
        len++;
        int j;
        // 定义定位节点
        Node indexNode = head;
        // 创建新节点
        Node newNode = new Node(data);

        // 判断链表是否为空
        if (head == null) {
            // 若是只需将新节点设为头节点和尾节点就行
            head = newNode;
            end = newNode;
            return;
        }

        // Create Mode
        if (i == -1) // 头插法
        {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            return;
        } else if (i == -2) // 尾插法
        {
            while (indexNode.next != null) {
                indexNode = indexNode.next;
            }
        }

        // Insert Mode

        // 移动定位节点至指定位置
        for (j = 0; j < i && indexNode != null; j++) {
            indexNode = indexNode.next;
        }
        // 判断是否是添加至最后一个节点
        if (indexNode.next == null) {
            // 将上一个节点next指向新节点
            indexNode.next = newNode;
            // 将新节点的prev指向上一个节点
            newNode.prev = indexNode;
            // 将新节点的next指向null
            newNode.next = null;
            // 将新节点设为尾节点
            end = newNode;
            return;
        }
        // 创建临时节点用于存储原下一个节点的地址
        Node tempNode = indexNode.next;
        // 将上一个节点的next指向新节点
        indexNode.next = newNode;
        // 将新节点的next指向原下一个节点
        newNode.next = tempNode;
        // 将新节点的prev指向上一个节点
        newNode.prev = indexNode;
        // 将原下一个节点的prev指向新节点
        tempNode.prev = newNode;
    }

    /**
     * 插入带数据的链表到指定位置,操作方式同add，只是新增的节点是已经有数据的
     * 
     * @param i        同add
     * @param haveNode 插入的节点
     */
    public void add(int i, Node haveNode) {
        len++;
        int j;
        Node indexNode = head;

        if (i == -1) {
            haveNode.next = head;
            head.prev = haveNode;
            head = haveNode;
            return;
        }

        for (j = 0; j < i && indexNode != null; j++) {
            indexNode = indexNode.next;
        }

        if (indexNode.next == null) {
            indexNode.next = haveNode;
            haveNode.prev = indexNode;
            haveNode.next = null;
            end = haveNode;
            return;
        }

        haveNode.next = indexNode.next;
        haveNode.next.prev = haveNode;
        haveNode.prev = indexNode;
        indexNode.next = haveNode;
    }

    /**
     * 删除指定节点
     * 
     * @param i 当 i = 0 时代表删除第一个节点，i = -1 时代表删除最后一个节点
     */
    public void delete(int i) {
        len--;
        // 判断是否是删除第一个节点
        if (i == 0) {
            // 将头节点指向第二个节点，然后将头节点的prev设置为null，这样就屏蔽掉了第一个节点
            head = head.next;
            if (head != null) {
                head.prev = null;
            }
            return;
        }
        // 若是删除最后一个节点，那只需将尾节点移到倒数第二个节点即可
        else if (i == -1) {
            end = end.prev;
            if (end != null) {
                end.next = null;
            }
            return;
        }

        // 递增定位
        int j;
        // 创建定位节点，将其指向头节点
        Node indexNode = head;
        // 将定位节点移至要删除节点的上一个节点
        for (j = 0; j < i - 1 && indexNode != null; j++) {
            indexNode = indexNode.next;
        }
        // 判断要删除节点是否是最后一个节点
        if (indexNode.next.next == null) {
            // 操作方式同删除头节点与
            end = end.prev;
            end.next = null;
            return;
        }
        // 要删除节点的下一个节点prev设置为要删除节点的上一个节点
        indexNode.next.next.prev = indexNode;
        // 将要删除节点的上一个节点的next设置为要删除节点的下一个节点
        indexNode.next = indexNode.next.next;
    }

    /**
     * 清空链表
     */
    public void clear() {
        len = 0;
        // 清空链表只需将头节点和尾节点设置为null即可，内存会由垃圾回收器回收
        head = null;
        end = null;
    }

    /**
     * 遍历输出链表数据
     *
     * @return String 链表数据的字符串
     */
    public String toString() {
        String out = "";
        // 创建定位节点
        Node indexNode = head;
        // 使用while循环进行遍历
        while (indexNode != null) {
            // 输出数据
            out += indexNode.data.toString() + " ";
            // 移动定位节点
            indexNode = indexNode.next;
        }
        return out;
    }

    /**
     * 链表逆置
     */
    public void resever() {
        // 创建临时节点用来临时存储指向数据
        Node tempNode = new Node();
        // 将头节点与尾节点交换
        tempNode = head;
        head = end;
        end = tempNode;
        // 创建定位节点
        Node indexNode = head;
        // 循环交换next和prev数据
        while (indexNode.prev != null && (indexNode.next != null || indexNode == head)) {
            tempNode = indexNode.next;
            indexNode.next = indexNode.prev;
            indexNode.prev = tempNode;
            indexNode = indexNode.next;
        }
        // 设置最后一个节点的next为null
        end.next = null;
        // 设置最后一个节点的prev数据
        end.prev = tempNode.next;
    }

    /**
     * 添加新节点到列表头
     * 
     * @param data 数据
     */
    public void addFirst(T data) {
        this.add(-1, data);
    }

    /**
     * 添加新节点到列表尾
     * 
     * @param data 数据
     */
    public void addLast(T data) {
        this.add(-2, data);
    }

    /**
     * 添加新节点到列表尾
     * 
     * @param data 数据
     */
    public void add(T data) {
        this.addLast(data);
    }

    /**
     * 入队
     * 
     * @param data 数据
     */
    public void qPush(T data) {
        this.pushL(data);
    }

    /**
     * 出队
     * 
     * @return T 数据
     */
    public T qPop() {
        return this.popF();
    }

    public T qPeek() {
        if (this.len <= 0) {
            return null;
        }
        return this.get(0);
    }

    public void sPush(T data) {
        this.pushL(data);
    }

    public T sPop() {
        return this.popL();
    }

    public T sPeek() {
        if (this.len <= 0) {
            return null;
        }
        return this.get(this.len - 1);
    }

    public void pushF(T data) {
        this.add(-1, data);
    }

    public void pushL(T data) {
        this.add(-2, data);
    }

    public T popF() {
        T data = this.get(0);
        this.delete(0);
        return data;
    }

    public T popL() {
        T data = this.get(len - 1);
        this.delete(len - 1);
        return data;
    }

    /**
     * 获取指定位置的数据
     * 
     * @param i 指定位置
     * @return T 数据
     */
    public T get(int i) {
        Node indexNode = head;
        for (int j = 0; j < i; j++) {
            indexNode = indexNode.next;
        }
        return indexNode.data;
    }

    /**
     * 获取指定节点
     * 
     * @param i 指定位置
     * @return Node 节点
     */
    public Node getNode(int i) {
        Node indexNode = head;
        for (int j = 0; j < i; j++) {
            indexNode = indexNode.next;
        }
        return indexNode;
    }

    public int size() {
        return this.len;
    }

    /**
     * 冒泡排序 low to high
     */
    public void bubbleSort() {
        this.bubbleSort(true);
    }

    /**
     * 冒泡排序
     * 
     * @param lowToHigh 从小到大（true）还是从大到小（false）
     */
    public void bubbleSort(boolean lowToHigh) {
        int ct = 1;
        if (!lowToHigh) {
            ct = -1;
        }
        // 定义排序个数和下标的变量
        int i, j;
        // 定义判断链表个数的链表和用来判断大小的链表
        Node temp;
        // 外层循环控制循环轮数
        for (i = 0; i < len - 1; i++) {
            // 内层循环控制每轮比较次数
            for (j = 0; j < len - i - 1; j++) {
                temp = this.getNode(j);
                if (temp.data.compareTo(temp.next.data) == ct) {
                    // 交换的方式是先删除大数据的节点，然后在添加回链表
                    // 删除大数据的节点
                    this.delete(j);
                    // 将删除后的节点添加会链表的下一个节点
                    this.add(j, temp);
                }
            }
        }
    }

    /**
     * 快速排序
     */
    public void quickSort() {
        this.quickSort(this.head, this.end, 0, this.len - 1, true);
    }

    /**
     * 快速排序
     * 
     * @param lowToHigh 从小到大（true）还是从大到小（false）
     */
    public void quickSort(boolean lowToHigh) {
        this.quickSort(this.head, this.end, 0, this.len - 1, lowToHigh);
    }

    /**
     * 快速排序
     * 
     * @param left      左节点
     * @param pivot     标记节点
     * @param l         左节点的位置
     * @param p         右节点的位置
     * @param lowToHigh 从小到大（true）还是从大到小（false）
     */
    public void quickSort(Node leftNode, Node pivotNode, int l, int p, boolean lowToHigh) {
        int r = p - 1;
        Node right = pivotNode.prev;
        int l_temp = l;
        Node left_temp = leftNode;
        T tempData;
        while (l < r) {
            while (l < r && (leftNode.data.compareTo(pivotNode.data) < 0) ^ !lowToHigh) {
                l++;
                leftNode = leftNode.next;
            }
            while (l < r && (right.data.compareTo(pivotNode.data) >= 0) ^ !lowToHigh) {
                r--;
                right = right.prev;
            }
            tempData = leftNode.data;
            leftNode.data = right.data;
            right.data = tempData;
        }
        if ((leftNode.data.compareTo(pivotNode.data) >= 0) ^ !lowToHigh) {
            tempData = leftNode.data;
            leftNode.data = pivotNode.data;
            pivotNode.data = tempData;
        }
        if (l_temp < l - 1)
            quickSort(left_temp, leftNode.prev, l_temp, l - 1, lowToHigh);
        if (l + 1 < p)
            quickSort(leftNode.next, pivotNode, l + 1, p, lowToHigh);
    }
}

/**
 * 优先队列，通过扩展MyLinkedList实现
 * 
 * @param <K> key
 * @param <V> value
 */
class PriorityQueue<K extends Comparable<K>, V extends Comparable<V>> {
    boolean keySort = true;

    /**
     * KV 真正存储到LinkedList中的数据对象
     */
    class KV implements Comparable<KV> {
        public K key;
        public V value;

        KV(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public int compareTo(KV o) {
            if (keySort) {
                return key.compareTo(o.key);
            } else {
                return value.compareTo(o.value);
            }
        }
    }

    MyLinkedList<KV> list;

    PriorityQueue() {
        this(true);
    }

    PriorityQueue(boolean keySort) {
        list = new MyLinkedList<KV>();
        this.keySort = keySort;
    }

    /**
     * 入队（暴力实现的方式，之后可能会用优雅一点的方式实现）
     * 
     * @param key   key
     * @param value value
     */
    public void push(K key, V value) {
        list.pushL(new KV(key, value));
        list.quickSort();
    }

    /**
     * 出队
     * 
     * @return KV KV对象
     */
    public KV pop() {
        return list.popF();
    }
}