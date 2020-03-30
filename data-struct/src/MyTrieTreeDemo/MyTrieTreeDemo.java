package MyTrieTreeDemo;

import java.util.LinkedList;
import java.util.List;

/**
 * MyTrieTreeDemo
 */
public class MyTrieTreeDemo {
    public static void main(String[] args) {
        MyTrieTree trie = new MyTrieTree();
        trie.add("abc");
        trie.add("abcd");
        trie.add("abd");
        trie.add("ab");
        System.out.println();
    }
}

class MyTrieTree {
    Node head = null;

    class Node {
        List<Node> next;
        String value;
        boolean has = true;

        Node() {
        }

        Node(String value, boolean has) {
            this.next = new LinkedList<Node>();
            this.value = value;
            this.has = has;
        }
    }

    MyTrieTree() {
        head = new Node("", false);
    }

    public void add(String str) {
        if (head.next.size() == 0) {
            head.next.add(new Node(str, true));
            return;
        }
        Node iNode = head;
        process(iNode, new Node(str, true));
    }

    public void process(Node iNode, Node sNode) {
        int i;
        for (i = 0; i < iNode.next.size(); i++) {
            Node cNode = iNode.next.get(i);
            String currStr = cNode.value;
            if (currStr.charAt(0) != sNode.value.charAt(0)) {
                continue;
            }
            int j = 1;
            while (j < sNode.value.length() && j < currStr.length() && currStr.charAt(j) == sNode.value.charAt(j)) {
                j++;
            }
            if (j >= currStr.length()) {
                // ab abc
                cNode.next.add(new Node(sNode.value.substring(j), true));
                cNode.has = true;
            } else if (j >= sNode.value.length()) {
                // abc ab
                Node nNode = new Node(currStr.substring(j), true);
                nNode.next.addAll(cNode.next);
                cNode.next = new LinkedList<Node>();
                cNode.next.add(nNode);
                cNode.value = sNode.value;
                cNode.has = true;
            } else {
                // abc abd
                Node nNode = new Node(currStr.substring(j), true);
                nNode.next.addAll(cNode.next);
                cNode.next = new LinkedList<Node>();
                cNode.next.add(nNode);
                cNode.next.add(new Node(sNode.value.substring(j), true));
                cNode.value = currStr.substring(0, j);
                cNode.has = false;
            }
            break;
        }
        if (i >= iNode.next.size()) {
            iNode.next.add(sNode);
        }
    }
}