package MyTreeDemo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * MyTreeDemo
 */
public class MyTreeDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String str = in.nextLine();
            MyTree<Integer, String> tree = new MyTree<Integer, String>();
            tree.stringListsToTree(str, '(', ')', ',', "^");
            System.out.println(str + "：");
            System.out.println("先序：" + tree.preOrder(false).toString().replaceAll("\\[|\\]|, ", ""));
            System.out.println("中序：" + tree.inOrder(false).toString().replaceAll("\\[|\\]|, ", ""));
            System.out.println("后序：" + tree.postOrder(false).toString().replaceAll("\\[|\\]|, ", ""));
        }
        in.close();
    }
}

/**
 * MyTree
 */
class MyTree<K extends Comparable<K>, T extends Comparable<T>> {
    // 根节点
    Node root = null;
    // Build临时存放节点
    Node build = null;

    boolean keySort = true;
    boolean isMax = true;

    // 节点类
    class Node {
        // 父节点
        Node parent;
        // 左子树
        Node left;
        // 右子树
        Node right;
        // 数据域
        K key;
        T data;

        Node() {
        }

        Node(K key, T data) {
            this.key = key;
            this.data = data;
        }
    }

    public MyTree() {
    }

    /**
     * 构造方法
     * 
     * @param keySort 通过key还是value排序
     * @param isMax   最大堆还是最小堆
     */
    public MyTree(boolean keySort, boolean isMax) {
        this.keySort = keySort;
        this.isMax = isMax;
    }

    /**
     * 添加根节点
     * 
     * @param key  key
     * @param data 数据
     * @return Node 当前的root节点
     */
    public Node addRoot(K key, T data) {
        this.root = new Node(key, data);
        return this.root;
    }

    /**
     * 添加节点
     * 
     * @param Node root 添加节点的根节点
     * @param int  LeftOrRight 添加方向 {-1:添加到左子树,1:添加到右子树}
     * @param T    data 数据
     */
    public Node add(Node root, int LeftOrRight, K key, T data) {
        Node reNode = null;
        if (root == null) {
            this.root = new Node(key, data);
            reNode = this.root;
        } else if (LeftOrRight == -1) {
            root.left = new Node(key, data);
            root.left.parent = root;
            reNode = root.left;
        } else if (LeftOrRight == 1) {
            root.right = new Node(key, data);
            root.right.parent = root;
            reNode = root.right;
        }
        return reNode;
    }

    /**
     * 添加节点
     * 
     * @param Node root 添加节点的根节点
     * @param int  LeftOrRight 添加方向 {-1:添加到左子树,1:添加到右子树}
     * @param Node havaNode 要添加的节点
     */
    public Node add(Node root, int LeftOrRight, Node havaNode) {
        Node reNode = havaNode;
        if (root == null) {
            this.root = havaNode;
            reNode = this.root;
        } else if (LeftOrRight == -1) {
            root.left = havaNode;
            root.left.parent = root;
            reNode = root.left;
        } else if (LeftOrRight == 1) {
            root.right = havaNode;
            root.right.parent = root;
            reNode = root.right;
        }
        return reNode;
    }

    /**
     * 获取树高
     * 
     * @return int 树高
     */
    public int getHeight() {
        return this.getHeight(this.root);
    }

    private int getHeight(Node root) {
        if (root == null) {
            return 0;
        } else {
            int leftHeight = this.getHeight(root.left);
            int rightHeight = this.getHeight(root.right);
            return leftHeight > rightHeight ? (leftHeight + 1) : (rightHeight + 1);
        }
    }

    /**
     * 获取节点总数
     * 
     * @return int 节点总数
     */
    public int getSize() {
        return getSize(this.root);
    }

    private int getSize(Node root) {
        if (root == null) {
            return 0;
        } else {
            return 1 + this.getSize(root.left) + this.getSize(root.right);
        }
    }

    /**
     * 获取父节点
     * 
     * @param Node child 子节点
     * @return Node 父节点
     */
    public Node getParentNode(Node child) {
        if (child == null) {
            return null;
        } else {
            return child.parent;
        }
    }

    /**
     * 获取左子树
     * 
     * @param Node root 父节点
     * @return Node 左子树
     */
    public Node getLeftChildNode(Node root) {
        if (root == null) {
            return null;
        } else {
            return root.left;
        }
    }

    /**
     * 获取右子树
     * 
     * @param Node root 父节点
     * @return Node 右子树
     */
    public Node getRightChildNode(Node root) {
        if (root == null) {
            return null;
        } else {
            return root.right;
        }
    }

    /**
     * 获取root树
     * 
     * @return Node root节点
     */
    public Node getRoot() {
        return this.root;
    }

    /**
     * 删除节点
     * 
     * @param key  key，若为null则通过data搜索
     * @param data 数据
     * @return Node 被删除的节点
     */
    public Node remove(K key, T data) {
        Node node = this.search(key, data);
        Node re = new Node(node.key, node.data);
        if (node.left == null && node.right == null) {
            if (node == this.root) {
                this.root = null;
            } else if (node.parent.left == node) {
                node.parent.left = null;
            } else if (node.parent.right == node) {
                node.parent.right = null;
            }
        } else if (node.left == null) {
            node.key = node.right.key;
            node.data = node.right.data;
        } else if (node.right == null) {
            node.key = node.left.key;
            node.data = node.left.data;
        } else {
            Node rep = node.right;
            while (rep.left != null) {
                rep = rep.left;
            }
            node.key = rep.key;
            node.data = rep.data;
            rep.parent.left = rep.right;
        }
        return re;
    }

    /**
     * 销毁整棵树
     */
    public void destroy() {
        if (root == null)
            return;
        root = null;
    }

    /**
     * 搜索节点
     * 
     * @param key  key，若为null则通过data搜索
     * @param data 数据
     * @return Node 被搜索到的节点，null则代表没有搜索到
     */
    public Node search(K key, T data) {
        return this.search(this.root, key, data);
    }

    private Node search(Node root, K key, T data) {
        if (root == null) {
            return null;
        }
        if (key != null) {
            if (root.key.compareTo(key) == 0) {
                return root;
            }
        } else {
            if (root.data.compareTo(data) == 0) {
                return root;
            }
        }
        Node temp = search(root.left, key, data);
        if (temp == null) {
            return search(root.right, key, data);
        } else {
            return temp;
        }
    }

    /**
     * 前序遍历
     */
    private void preOrder(Node tree, List<T> list) {
        if (tree != null) {
            list.add(tree.data);
            preOrder(tree.left, list);
            preOrder(tree.right, list);
        }
    }

    public List<T> preOrder(boolean print) {
        List<T> list = new LinkedList<T>();
        preOrder(root, list);
        if (print) {
            System.out.println(list);
            return null;
        } else {
            return list;
        }
    }

    /**
     * 中序遍历
     */
    private void inOrder(Node tree, List<T> list) {
        if (tree != null) {
            inOrder(tree.left, list);
            list.add(tree.data);
            inOrder(tree.right, list);
        }
    }

    public List<T> inOrder(boolean print) {
        List<T> list = new LinkedList<T>();
        inOrder(root, list);
        if (print) {
            System.out.println(list);
            return null;
        } else {
            return list;
        }
    }

    /**
     * 后序遍历
     */
    private void postOrder(Node tree, List<T> list) {
        if (tree != null) {
            postOrder(tree.left, list);
            postOrder(tree.right, list);
            list.add(tree.data);
        }
    }

    public List<T> postOrder(boolean print) {
        List<T> list = new LinkedList<T>();
        postOrder(root, list);
        if (print) {
            System.out.println(list);
            return null;
        } else {
            return list;
        }
    }

    /**
     * 层次遍历
     */
    private void layerOrder(List<T> list) {
        if (root != null) {
            List<Node> queue = new LinkedList<Node>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node node = queue.remove(0);
                list.add(node.data);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
        }
    }

    public List<T> layerOrder(boolean print) {
        List<T> list = new LinkedList<T>();
        this.layerOrder(list);
        if (print) {
            System.out.println(list);
            return null;
        } else {
            return list;
        }
    }

    /**
     * 普通二叉树转换为数组二叉树
     */
    private void toArrayTree(Object[] array, Node root, int start, T emptyData) {
        int lnode = 2 * start + 1;
        int rnode = 2 * start + 2;
        if (start >= (int) (Math.pow(2, this.getHeight()) - 1))
            return;
        if (root == null) {
            array[start] = emptyData;
            toArrayTree(array, null, lnode, emptyData);
            toArrayTree(array, null, rnode, emptyData);
        } else {
            array[start] = root.data;
            toArrayTree(array, root.left, lnode, emptyData);
            toArrayTree(array, root.right, rnode, emptyData);
        }
    }

    public Object[] toArrayTree(T emptyData) {
        Object[] array = new Object[(int) (Math.pow(2, this.getHeight()) - 1)];
        this.toArrayTree(array, this.root, 0, emptyData);
        return array;
    }

    /**
     * 将数组二叉树转化为普通二叉树
     */
    private Node arrayToTree(List<K> keyList, List<T> valueList, int start, T emptyData) {
        if (valueList.get(start).compareTo(emptyData) == 0) {
            return null;
        }
        Node root = new Node(keyList.get(start), valueList.get(start));
        int lnode = 2 * start + 1;
        int rnode = 2 * start + 2;
        if (lnode > valueList.size() - 1) {
            root.left = null;
        } else {
            root.left = arrayToTree(keyList, valueList, lnode, emptyData);
        }
        if (rnode > valueList.size() - 1) {
            root.right = null;
        } else {
            root.right = arrayToTree(keyList, valueList, rnode, emptyData);
        }
        return root;
    }

    public void arrayToTree(K[] keyArray, T[] valueArray, T emptyData) {
        List<K> keyList = new LinkedList<K>(Arrays.asList(keyArray));
        List<T> valueList = new LinkedList<T>(Arrays.asList(valueArray));
        this.root = arrayToTree(keyList, valueList, 0, emptyData);
    }

    /**
     * 将String广义表转换为二叉树 自动设置Integer key
     */
    private Node stringListsToTreeFun(Integer key, String lists, char leftMark, char rightMark, char midMark,
            String emptyData) {
        if (lists.equals(emptyData))
            return null;
        int index = lists.indexOf(leftMark);
        Node root = null;
        if (index == -1) {
            root = new Node((K) key, (T) lists);
            return root;
        }
        root = new Node((K) key, (T) lists.substring(0, index));
        List<Character> queue = new LinkedList<Character>();
        int midIndex = 0;
        int i = lists.indexOf(leftMark) + 1;
        queue.add(leftMark);
        while (!queue.isEmpty()) {
            if (lists.charAt(i) == leftMark) {
                queue.add(leftMark);
            } else if (lists.charAt(i) == rightMark) {
                if (queue.get(queue.size() - 1) == midMark && queue.get(queue.size() - 2) == leftMark) {
                    queue.remove(queue.size() - 1);
                    queue.remove(queue.size() - 1);
                } else {
                    queue.add(rightMark);
                }
            } else if (lists.charAt(i) == midMark) {
                if (queue.size() == 1) {
                    midIndex = i;
                    break;
                } else {
                    queue.add(midMark);
                    midIndex = i;
                }
            }
            i++;
        }
        root.left = stringListsToTreeFun(key + 1, lists.substring(lists.indexOf(leftMark) + 1, midIndex), leftMark,
                rightMark, midMark, emptyData);
        root.right = stringListsToTreeFun(key + 1, lists.substring(midIndex + 1, lists.lastIndexOf(rightMark)),
                leftMark, rightMark, midMark, emptyData);
        return root;
    }

    public void stringListsToTree(String lists, char leftMark, char rightMark, char midMark, String emptyData) {
        this.root = stringListsToTreeFun(0, lists, leftMark, rightMark, midMark, emptyData);
    }

    /**
     * 将二叉树转换为String广义表
     */
    private String toListsString(Node root, char leftMark, char rightMark, char midMark, String emptyData) {
        if (root != null) {
            if (root.left != null || root.right != null) {
                String leftString = emptyData;
                String rightString = emptyData;
                if (root.left != null)
                    leftString = toListsString(root.left, leftMark, rightMark, midMark, emptyData);
                if (root.right != null)
                    rightString = toListsString(root.right, leftMark, rightMark, midMark, emptyData);
                return root.data.toString() + leftMark + leftString + midMark + rightString + rightMark;
            } else {
                return root.data.toString();
            }
        }
        return "";
    }

    public String toListsString(char leftMark, char rightMark, char midMark, String emptyData) {
        return toListsString(this.root, leftMark, rightMark, midMark, emptyData);
    }

    /**
     * 判断是否是叶节点
     */
    public boolean isLeafNode(Node node) {
        if (node.left == null && node.right == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得二叉树叶节点的个数
     */
    public int LeafNodeSize() {
        if (root != null) {
            int size = 0;
            List<Node> queue = new LinkedList<Node>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node node = queue.remove(0);
                if (node.left == null && node.right == null)
                    size++;
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            return size;
        }
        return 0;
    }

    /**
     * 获得叶节点列表，层次遍历
     */
    public List<T> LeafNodeList() {
        if (root != null) {
            List<T> list = new LinkedList<T>();
            List<Node> queue = new LinkedList<Node>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node node = queue.remove(0);
                if (node.left == null && node.right == null) {
                    list.add(node.data);
                }
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            return list;
        }
        return null;
    }

    /**
     * 获得叶节点列表，中序遍历
     */
    private void inLeafNodeList(Node tree, List<T> list) {
        if (tree != null) {
            inLeafNodeList(tree.left, list);
            if (tree.left == null && tree.right == null) {
                list.add(tree.data);
            }
            inLeafNodeList(tree.right, list);
        }
    }

    public List<T> inLeafNodeList() {
        if (root != null) {
            List<T> list = new LinkedList<T>();
            this.inLeafNodeList(this.root, list);
            return list;
        }
        return null;
    }

    /**
     * 判断是否是完全二叉树
     */
    public boolean isGoodTree() {
        if (root != null) {
            List<Node> queue = new LinkedList<Node>();
            queue.add(root);
            while (!queue.isEmpty()) {
                Node node = queue.remove(0);
                if (node != null) {
                    if (node.left != null) {
                        queue.add(node.left);
                    }
                    if (node.right != null) {
                        queue.add(node.right);
                    }
                    if (node.left == null && node.right != null) {
                        return false;
                    }
                    if (node.left == null && node.right == null) {
                        while (!queue.isEmpty()) {
                            node = queue.get(0);
                            if ((node.left != null && node.right == null)
                                    || (node.left == null && node.right == null)) {
                                queue.remove(0);
                            } else {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 二叉查找树 - start
     */
    private void bstAdd(Node root, K key, T data) {
        if ((this.keySort && key.compareTo(root.key) < 0) || (!this.keySort && data.compareTo(root.data) < 0)) {
            if (root.left == null) {
                root.left = new Node(key, data);
                root.left.parent = root;
            } else {
                bstAdd(root.left, key, data);
            }
        } else if ((this.keySort && key.compareTo(root.key) > 0) || (!this.keySort && data.compareTo(root.data) > 0)) {
            if (root.right == null) {
                root.right = new Node(key, data);
                root.right.parent = root;
            } else {
                bstAdd(root.right, key, data);
            }
        }
    }

    public void bstAdd(K key, T data) {
        if (this.root == null) {
            this.root = new Node(key, data);
            return;
        }
        bstAdd(this.root, key, data);
    }

    private Node bstSearchK(Node root, K key) {
        if (root == null)
            return null;
        if (root.key.compareTo(key) == 0) {
            return root;
        }
        Node t = null;
        t = bstSearchK(root.left, key);
        t = bstSearchK(root.right, key);
        return t;
    }

    private Node bstSearchV(Node root, T data) {
        if (root == null)
            return null;
        if (data.compareTo(root.data) == 0) {
            return root;
        }
        Node t = null;
        if (data.compareTo(root.data) < 0) {
            t = bstSearchV(root.left, data);
        } else {
            t = bstSearchV(root.right, data);
        }
        return t;
    }

    public Node bstSearch(K key, T data) {
        if (key == null) {
            return bstSearchV(this.root, data);
        } else {
            return bstSearchK(this.root, key);
        }
    }

    public Node bstMin() {
        Node root = this.root;
        while (root.left == null) {
            root = root.left;
        }
        return root;
    }

    public Node bstMax() {
        Node root = this.root;
        while (root.right == null) {
            root = root.right;
        }
        return root;
    }

    public void bstRemove(K key, T data) {
        Node node = bstSearch(key, data);
        Node left = node.left;
        Node right = node.right;
        if (node.parent.left == node) {
            if (right != null) {
                node.right.parent = node.parent;
                node.parent.left = right;
            } else {
                node.left.parent = node.parent;
                node.parent.left = left;
                return;
            }
        } else {
            if (right != null) {
                node.right.parent = node.parent;
                node.parent.right = right;
            } else {
                node.left.parent = node.parent;
                node.parent.right = left;
                return;
            }
        }
        while (right.left != null) {
            right = right.left;
        }
        right.left = left;
        if (left != null) {
            left.parent = right;
        }
    }

    // 二叉查找树 - end
    // 旋转
    public void rotateLeft(Node root) {
        if (root == null) {
            return;
        }
        Node oldRight = root.right;
        if (oldRight == null) {
            return;
        }
        Node mv = oldRight.left;
        root.right = mv;
        if (mv != null) {
            mv.parent = root;
        }
        if (root.parent == null) {
            this.root = oldRight;
            oldRight.parent = null;
        } else {
            if (root.parent.left == root) {
                root.parent.left = oldRight;
            } else {
                root.parent.right = oldRight;
            }
            oldRight.parent = root.parent;
        }
        oldRight.left = root;
        root.parent = oldRight;
    }

    public void rotateRight(Node root) {
        if (root == null) {
            return;
        }
        Node oldLeft = root.left;
        if (oldLeft == null) {
            return;
        }
        Node mv = oldLeft.right;
        root.left = mv;
        if (mv != null) {
            mv.parent = root;
        }
        if (root.parent == null) {
            this.root = oldLeft;
            oldLeft.parent = null;
        } else {
            if (root.parent.left == root) {
                root.parent.left = oldLeft;
            } else {
                root.parent.right = oldLeft;
            }
            oldLeft.parent = root.parent;
        }
        oldLeft.right = root;
        root.parent = oldLeft;
    }

    // 旋转 - end
    // 堆
    /**
     * 建立层次结构的二叉树 - 不完全版 目前缺失功能：添加空节点
     */
    public void buildAdd(Node root, K key, T data) {
        // add this.root
        if (this.root == null) {
            this.root = new Node(key, data);
            this.build = this.root;
            return;
        }
        // add this.root.left or this.root.right
        if (root == this.root) {
            if (root.left == null) {
                root.left = new Node(key, data);
                root.left.parent = root;
                this.build = root.left;
                return;
            } else {
                root.right = new Node(key, data);
                root.right.parent = root;
                this.build = root.right;
                return;
            }
        }
        if (root.parent.left == root) {
            root.parent.right = new Node(key, data);
            root.parent.right.parent = root.parent;
            this.build = root.parent.right;
        } else if (root.parent.right == root) {
            while (root.parent.left != root) {
                root = root.parent;
                if (root == this.root) {
                    break;
                }
            }
            if (root != this.root) {
                root = root.parent.right;
            }
            while (root.left != null) {
                root = root.left;
            }
            root.left = new Node(key, data);
            root.left.parent = root;
            this.build = root.left;
        }
    }

    public boolean buildAdd(K key, T data) {
        if (root != null && this.build == null)
            return false;
        // if(this.build == null) {
        // root = build
        this.buildAdd(this.build, key, data);
        // }
        return true;
    }

    public void heapAdd(K key, T data) {
        if (root != null && this.build == null)
            return;
        this.buildAdd(this.build, key, data);
        Node indexNode = this.build;
        while (indexNode.parent != null) {
            if ((!this.keySort && indexNode.data.compareTo(indexNode.parent.data) == (isMax ? 1 : -1))
                    || (this.keySort && indexNode.key.compareTo(indexNode.parent.key) == (isMax ? 1 : -1))) {
                K tempKey = indexNode.key;
                T tempData = indexNode.data;
                indexNode.key = indexNode.parent.key;
                indexNode.data = indexNode.parent.data;
                indexNode.parent.key = tempKey;
                indexNode.parent.data = tempData;
            }
            indexNode = indexNode.parent;
        }
    }

    public Node heapRemove() {
        Node re = new Node(this.root.key, this.root.data);
        if (this.build == this.root) {
            this.build = null;
            this.root = null;
            return re;
        }
        this.root.key = this.build.key;
        this.root.data = this.build.data;
        if (this.build.parent.left == this.build) {
            this.build.parent.left = null;
            Node indexNode = this.build.parent;
            while (indexNode.parent.right != indexNode) {
                indexNode = indexNode.parent;
                if (indexNode == this.root) {
                    break;
                }
            }
            if (indexNode != this.root) {
                indexNode = indexNode.parent.left;
            }
            while (indexNode.right != null) {
                indexNode = indexNode.right;
            }
            this.build = indexNode;
        } else if (this.build.parent.right == this.build) {
            this.build.parent.right = null;
            this.build = this.build.parent.left;
        }
        Node indexNode = this.root;
        while ((this.keySort && indexNode.left != null
                && indexNode.key.compareTo(indexNode.left.key) == (isMax ? -1 : 1) && indexNode.right != null
                && indexNode.key.compareTo(indexNode.right.key) == (isMax ? -1 : 1))
                || (!this.keySort && indexNode.left != null
                        && indexNode.data.compareTo(indexNode.left.data) == (isMax ? -1 : 1) && indexNode.right != null
                        && indexNode.data.compareTo(indexNode.right.data) == (isMax ? -1 : 1))) {
            K tempKey = indexNode.key;
            T tempData = indexNode.data;
            if ((this.keySort && indexNode.left.key.compareTo(indexNode.right.key) == (isMax ? -1 : 1))
                    || (!this.keySort && indexNode.left.data.compareTo(indexNode.right.data) == (isMax ? -1 : 1))) {
                // left < or >(min heap) right
                indexNode.key = indexNode.right.key;
                indexNode.data = indexNode.right.data;
                indexNode.right.key = tempKey;
                indexNode.right.data = tempData;
                indexNode = indexNode.right;
            } else {
                // left >= or <=(min heap) right
                indexNode.key = indexNode.left.key;
                indexNode.data = indexNode.left.data;
                indexNode.left.key = tempKey;
                indexNode.left.data = tempData;
                indexNode = indexNode.left;
            }
        }
        return re;
    }

    // 堆 - end
    // private void preOrder(Node tree, List<T> list) {
    // if (tree != null) {
    // list.add(tree.data);
    // preOrder(tree.left, list);
    // preOrder(tree.right, list);
    // }
    // }
    public void buildByPre(String pre, char split, String emptyData) {
        String[] strs = pre.split(split + "");
        List<String> list = new LinkedList<String>(Arrays.asList(strs));
        this.root = this.buildByPre(list, emptyData);
    }

    private Node buildByPre(List<String> list, String emptyData) {
        if (list.get(0).equals(emptyData)) {
            list.remove(0);
            return null;
        }
        String data = list.remove(0);
        Node curr = new Node((K) data, (T) data);
        curr.left = buildByPre(list, emptyData);
        curr.right = buildByPre(list, emptyData);
        return curr;
    }

    // TODO: 父节点到子节点的路径，哈夫曼编码，最近的公共祖先
}