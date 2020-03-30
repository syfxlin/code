package MySingleLinkedListDemo;

import java.util.Scanner;

public class MySingleLinkedListDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        MySingleLinkedList<Integer> list = new MySingleLinkedList<Integer>();
        list.addLast(4);
        list.addLast(2);
        list.addLast(5);
        list.addLast(3);
        list.addLast(7);
        list.addLast(9);
        list.addLast(0);
        list.addLast(1);
        list.quickSort();
        System.out.println(list.toString());
        in.close();
    }
}

class MySingleLinkedList<T extends Comparable<T>> {
    int len = 0;
    Node head = null;
    Node end = null;

    class Node {
        Node next;
        T data;

        Node() {
        };

        Node(T data) {
            this.data = data;
        }
    }

    public Node getHead() {
        return head;
    }

    public void add(int i, T data) {
        len++;
        int j;
        Node indexNode = head;
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            end = newNode;
            return;
        }
        if (i == -1) {
            newNode.next = head;
            head = newNode;
            return;
        } else if (i == -2) {
            while (indexNode != null && indexNode.next != null) {
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
            indexNode.next = newNode;
            newNode.next = null;
            end = newNode;
            return;
        }
        Node tempNode = indexNode.next;
        indexNode.next = newNode;
        newNode.next = tempNode;
    }

    // 插入带数据的链表到指定位置,操作方式同add，只是新增的节点是已经有数据的
    public void add(int i, Node haveNode) {
        len++;
        int j;
        Node indexNode = head;

        if (i == -1) {
            haveNode.next = head;
            head = haveNode;
            return;
        }

        for (j = 0; j < i && indexNode != null; j++) {
            indexNode = indexNode.next;
        }

        if (indexNode.next == null) {
            indexNode.next = haveNode;
            haveNode.next = null;
            end = haveNode;
            return;
        }

        haveNode.next = indexNode.next;
        indexNode.next = haveNode;
    }

    public void delete(int i) {
        len--;
        int j;
        Node indexNode = head;
        if (i == 0) {
            head = head.next;
            return;
        } else if (i == -1) {
            while (indexNode != null && indexNode.next != null && indexNode.next.next != null) {
                indexNode = indexNode.next;
            }
        }
        for (j = 0; j < i - 1 && indexNode != null; j++) {
            indexNode = indexNode.next;
        }
        if (indexNode.next.next == null) {
            indexNode.next = null;
            end = indexNode;
            return;
        }
        indexNode.next = indexNode.next.next;
    }

    public void clear() {
        len = 0;
        head = null;
        end = null;
    }

    public String toString() {
        String out = "";
        Node indexNode = head;
        while (indexNode != null) {
            out += indexNode.data.toString() + " ";
            indexNode = indexNode.next;
        }
        return out;
    }

    public void resever() {
        Node left = null;
        Node mid = head;
        Node right = head;
        while (right != null) {
            right = mid.next;
            mid.next = left;
            left = mid;
            mid = right;
        }
        head = left;
    }

    public void addFirst(T data) {
        this.add(-1, data);
    }

    public void addLast(T data) {
        this.add(-2, data);
    }

    public void push(T data) {
        this.add(-1, data);
    }

    public T pop() {
        T data = this.get(len - 1);
        this.delete(len - 1);
        return data;
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

    public T get(int i) {
        Node indexNode = head;
        for (int j = 0; j < i; j++) {
            indexNode = indexNode.next;
        }
        return indexNode.data;
    }

    public Node getNode(int i) {
        Node indexNode = head;
        for (int j = 0; j < i; j++) {
            indexNode = indexNode.next;
        }
        return indexNode;
    }

    public void bubbleSort() {
        this.bubbleSort(true);
    }

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

    public void quickSort(boolean lowToHigh) {
        this.quickSort(head, null, lowToHigh);
    }

    public void quickSort() {
        this.quickSort(true);
    }

    public void quickSort(Node sNode, Node eNode, boolean lowToHigh) {
        if (sNode == eNode || sNode.next == eNode)
            return;
        int ct = lowToHigh ? -1 : 1;
        Node pivot = sNode;
        Node left = sNode;
        Node right = sNode.next;
        while (right != eNode) {
            if (right.data.compareTo(pivot.data) == ct) {
                left = left.next;
                T tempData = left.data;
                left.data = right.data;
                right.data = tempData;
            }
            right = right.next;
        }
        if (left != sNode) {
            T temp = left.data;
            left.data = sNode.data;
            sNode.data = temp;
        }
        quickSort(sNode, left, lowToHigh);
        quickSort(left.next, eNode, lowToHigh);
    }

    public int search(T data) {
        int index = 0;
        Node indexNode = head;
        while (indexNode != null) {
            if (indexNode.data.compareTo(data) == 0) {
                return index;
            }
            index++;
            indexNode = indexNode.next;
        }
        return -1;
    }

    public void set(int i, T data) {
        if (i >= this.len)
            return;
        Node indexNode = head;
        for (int j = 1; j <= i; j++) {
            indexNode = indexNode.next;
        }
        indexNode.data = data;
    }
}