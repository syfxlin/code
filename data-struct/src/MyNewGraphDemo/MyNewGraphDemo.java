package MyNewGraphDemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * MyGraphDemo
 */
public class MyNewGraphDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("输入节点数和边数：");
        MyGraph<Integer> gra = new MyGraph<Integer>(MyGraph.GraphType.undirected);
        int m = in.nextInt();
        int n = in.nextInt();
        for (int i = 0; i < m; i++) {
            gra.addVertex(i + 1);
        }
        System.out.println("输入边：");
        for (int i = 0; i < n; i++) {
            gra.addEdge(in.nextInt(), in.nextInt(), 1);
        }
        List<MyGraph<Integer>.Vertex> list = gra.dfsWalk(1);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("第" + (i + 1) + "个节点：" + list.get(i));
        }
        System.out.println();
        in.close();
    }
}

/**
 * MyGraph Vertex中存放顶点的信息，以及连接点列表 Edge中存放权重，和与之连接点在VertexList中的索引
 */
class MyGraph<T extends Comparable<T>> {
    enum GraphType {
        directed, undirected
    }

    GraphType type = GraphType.undirected;

    List<Vertex> vertexList;
    List<Edge> edgeList;

    public MyGraph() {
        this.vertexList = new LinkedList<Vertex>();
        this.edgeList = new LinkedList<Edge>();
    }

    public MyGraph(GraphType type) {
        this.vertexList = new LinkedList<Vertex>();
        this.edgeList = new LinkedList<Edge>();
        this.type = type;
    }

    class Vertex implements Comparable<Vertex> {
        T data;
        List<Edge> link;

        public Vertex(T data) {
            this.data = data;
            this.link = new LinkedList<Edge>();
        }

        public String toString() {
            return data.toString();
        }

        public boolean equals(Vertex o) {
            return this.data.equals(o.data);
        }

        public int hashCode() {
            return this.data.hashCode();
        }

        public int compareTo(Vertex o) {
            return this.data.compareTo(o.data);
        }
    }

    class Edge implements Comparable<Edge> {
        Vertex v1;
        Vertex v2;
        int weight;

        public Edge(Vertex v1, Vertex v2, int weight) {
            this.v1 = v1;
            this.v2 = v2;
            this.weight = weight;
        }

        public String toString() {
            return "[" + this.v1.data.toString() + "-" + this.v2.data.toString() + "-" + this.weight + "]";
        }

        public boolean equals(Edge o) {
            return this.v1.data.equals(o.v1.data) && this.v2.data.equals(o.v2.data);
        }

        public int compareTo(Edge o) {
            return Integer.valueOf(this.weight).compareTo(Integer.valueOf(o.weight));
        }
    }

    public Vertex getVertexFromData(T data) {
        for (int i = 0; i < this.vertexList.size(); i++) {
            if (this.vertexList.get(i).data.equals(data)) {
                return this.vertexList.get(i);
            }
        }
        return null;
    }

    public int getIndexFromData(T data) {
        for (int i = 0; i < this.vertexList.size(); i++) {
            if (this.vertexList.get(i).data.equals(data)) {
                return i;
            }
        }
        return -1;
    }

    public Vertex addVertex(T data) {
        Vertex v = new Vertex(data);
        this.vertexList.add(v);
        return v;
    }

    public Edge addEdge(T d1, T d2, int weight) {
        Vertex v1 = this.getVertexFromData(d1);
        Vertex v2 = this.getVertexFromData(d2);
        if (v1 == null) {
            v1 = this.addVertex(d1);
        }
        if (v2 == null) {
            v2 = this.addVertex(d2);
        }
        Edge edge = new Edge(v1, v2, weight);
        if (this.edgeList.contains(edge)) {
            Edge e = this.edgeList.get(this.edgeList.indexOf(edge));
            e.weight += weight;
            return e;
        }
        this.edgeList.add(edge);
        v1.link.add(edge);
        if (this.type == GraphType.undirected) {
            v2.link.add(new Edge(v2, v1, weight));
        }
        return edge;
    }

    public void printEdge() {
        System.out.println(Arrays.toString(this.edgeList.toArray()));
    }

    // 邻接矩阵
    public int[][] toAdjacencyMatrix() {
        int size = this.vertexList.size();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            Vertex v = this.vertexList.get(i);
            for (int j = 0; j < v.link.size(); j++) {
                Edge e = v.link.get(j);
                int k = this.getIndexFromData(e.v2.data);
                matrix[i][k] += e.weight;
            }
        }
        return matrix;
    }

    public void printAdjacencyMatrix() {
        int[][] m = this.toAdjacencyMatrix();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                if (j != m.length - 1) {
                    System.out.print(m[i][j] + " ");
                } else {
                    System.out.println(m[i][j]);
                }
            }
        }
    }

    // TODO: 关联矩阵，可达矩阵，关键路径，Prim最小生成树

    // 顶点的度
    public int getOutDegree(T data) {
        return this.getVertexFromData(data).link.size();
    }

    public int getInDegree(T data) {
        if (this.type == GraphType.undirected) {
            return this.getVertexFromData(data).link.size();
        }
        int count = 0;
        for (int i = 0; i < this.edgeList.size(); i++) {
            if (this.edgeList.get(i).v2.data.equals(data)) {
                count++;
            }
        }
        return count;
    }

    public int getAllDegree(T data) {
        if (this.type == GraphType.undirected) {
            return this.getOutDegree(data);
        } else {
            return this.getInDegree(data) + this.getOutDegree(data);
        }
    }

    // 获取所有孤立点
    public List<Vertex> getIsolatedVertex() {
        List<Vertex> list = new LinkedList<Vertex>();
        for (int i = 0; i < this.vertexList.size(); i++) {
            Vertex v = this.vertexList.get(i);
            if (this.getAllDegree(v.data) == 0) {
                list.add(v);
            }
        }
        return list;
    }

    // 获取所有悬挂点
    public List<Vertex> getSuspensionVertex() {
        List<Vertex> list = new LinkedList<Vertex>();
        for (int i = 0; i < this.vertexList.size(); i++) {
            Vertex v = this.vertexList.get(i);
            if (this.getAllDegree(v.data) == 1) {
                list.add(v);
            }
        }
        return list;
    }

    // 获取联通区域
    // TODO: 有向图未处理
    public List<Set<Vertex>> getConnectedArea() {
        List<Set<Vertex>> list = new LinkedList<Set<Vertex>>();
        for (int i = 0; i < this.vertexList.size(); i++) {
            Vertex v = this.vertexList.get(i);
            Set<Vertex> temp = new HashSet<Vertex>();
            temp.add(v);
            for (int j = 0; j < v.link.size(); j++) {
                temp.add(v.link.get(j).v2);
            }
            boolean exist = false;
            for (int j = 0; j < list.size(); j++) {
                Set<Vertex> copy = new HashSet<Vertex>(list.get(j));
                copy.removeAll(temp);
                if (list.get(j).size() != copy.size()) {
                    list.get(j).addAll(temp);
                    exist = true;
                }
            }
            if (!exist) {
                list.add(temp);
            }
        }
        return list;
    }

    // DFS遍历
    public List<Vertex> dfsWalk(T start) {
        return this.dfs(start, null, true);
    }

    public List<Vertex> dfsWalk(T start, boolean containsWalked) {
        return this.dfs(start, null, containsWalked);
    }

    public List<Vertex> dfs(T start, T end) {
        return this.dfs(start, end, false);
    }

    public List<Vertex> dfs(T start, T end, boolean containsWalked) {
        List<Vertex> list = new LinkedList<Vertex>();
        Stack<Vertex> stack = new Stack<Vertex>();
        Set<Vertex> set = new HashSet<Vertex>();
        Vertex vs = this.getVertexFromData(start);
        Vertex ve = this.getVertexFromData(end);
        stack.push(vs);
        set.add(vs);
        list.add(vs);
        while (!stack.isEmpty()) {
            Vertex v = stack.pop();
            for (int i = 0; i < v.link.size(); i++) {
                Edge e = v.link.get(i);
                if (ve != null && e.v2.equals(ve)) {
                    if (!containsWalked) {
                        list.removeAll(list);
                        list.addAll(stack);
                        list.add(v);
                    }
                    list.add(e.v2);
                    return list;
                }
                if (!set.contains(e.v2)) {
                    stack.push(v);
                    stack.push(e.v2);
                    set.add(e.v2);
                    list.add(e.v2);
                    break;
                }
            }
        }
        return list;
    }

    public List<Vertex> bfsWalk(T start) {
        return this.bfs(start, null, true);
    }

    public List<Vertex> bfsWalk(T start, boolean containsWalked) {
        return this.bfs(start, null, containsWalked);
    }

    public List<Vertex> bfs(T start, T end) {
        return this.bfs(start, end, false);
    }

    public List<Vertex> bfs(T start, T end, boolean containsWalked) {
        List<Vertex> list = new LinkedList<Vertex>();
        Queue<BfsVertex> queue = new LinkedList<BfsVertex>();
        Set<Vertex> set = new HashSet<Vertex>();
        Vertex vs = this.getVertexFromData(start);
        Vertex ve = this.getVertexFromData(end);
        queue.add(new BfsVertex(vs, null));
        set.add(vs);
        list.add(vs);
        while (!queue.isEmpty()) {
            BfsVertex curr = queue.poll();
            Vertex v = curr.v;
            for (int i = 0; i < v.link.size(); i++) {
                Edge e = v.link.get(i);
                if (ve != null && e.v2.equals(ve)) {
                    if (!containsWalked) {
                        list.removeAll(list);
                        while (curr != null) {
                            list.add(curr.v);
                            curr = curr.parent;
                        }
                        Collections.reverse(list);
                    }
                    list.add(e.v2);
                    return list;
                }
                if (!set.contains(e.v2)) {
                    queue.add(new BfsVertex(e.v2, curr));
                    set.add(e.v2);
                    list.add(e.v2);
                }
            }
        }
        return list;
    }

    class BfsVertex {
        BfsVertex parent;
        Vertex v;

        public BfsVertex(Vertex v, BfsVertex parent) {
            this.v = v;
            this.parent = parent;
        }
    }

    public boolean isConnected(T start, T end) {
        Vertex vs = this.getVertexFromData(start);
        Vertex ve = this.getVertexFromData(end);
        Queue<Vertex> queue = new LinkedList<Vertex>();
        Set<Vertex> set = new HashSet<Vertex>();
        queue.add(vs);
        set.add(vs);
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            for (int i = 0; i < v.link.size(); i++) {
                Edge e = v.link.get(i);
                if (ve != null && e.v2.equals(ve)) {
                    return true;
                }
                if (!set.contains(e.v2)) {
                    queue.add(e.v2);
                    set.add(e.v2);
                }
            }
        }
        return false;
    }

    // 最小生成树 isAllConnected true：图一定能连成一棵树，false：图会被分为多棵树
    public MyGraph<T> kruskal() {
        return this.kruskal(true);
    }

    public MyGraph<T> kruskal(boolean isAllConnected) {
        MyGraph<T> tree = new MyGraph<T>(GraphType.undirected);
        for (int i = 0; i < this.vertexList.size(); i++) {
            tree.addVertex(this.vertexList.get(i).data);
        }
        List<Edge> el = new LinkedList<Edge>(this.edgeList);
        Collections.sort(el);
        Set<Vertex> set = new HashSet<Vertex>();
        int index = 0;
        boolean last = isAllConnected;
        while (index < el.size() && (last || set.size() != this.vertexList.size())) {
            Vertex v1 = el.get(index).v1;
            Vertex v2 = el.get(index).v2;
            if (tree.isConnected(v1.data, v2.data)) {
                index++;
                continue;
            }
            if (last && set.size() == this.vertexList.size()) {
                last = false;
            }
            tree.addEdge(v1.data, v2.data, el.get(index).weight);
            index++;
            if (index >= el.size()) {
                break;
            }
            set.add(el.get(index).v1);
            set.add(el.get(index).v2);
        }
        return tree;
    }

    // 最短路径
    public List<Vertex> dijkstra(T start, T end) {
        int[][] map = dijkstraWalk(start, end);
        int startIndex = this.getIndexFromData(start);
        int endIndex = this.getIndexFromData(end);
        List<Vertex> path = new LinkedList<Vertex>();
        while (endIndex != startIndex) {
            path.add(vertexList.get(endIndex));
            endIndex = map[endIndex][0];
            if (endIndex == -1) {
                return null;
            }
        }
        path.add(vertexList.get(endIndex));
        Collections.reverse(path);
        return path;
    }

    public int dijkstraLen(T start, T end) {
        int[][] map = dijkstraWalk(start, end);
        return map[this.getIndexFromData(end)][1];
    }

    private int[][] dijkstraWalk(T start, T end) {
        int[][] map = new int[this.vertexList.size()][3];
        for (int i = 0; i < map.length; i++) {
            // 上一个节点的位置
            map[i][0] = -1;
            // 从开始到当前节点的距离
            map[i][1] = Integer.MAX_VALUE;
            // 标记
            map[i][2] = 0;
        }
        int startIndex = this.getIndexFromData(start);
        map[startIndex][1] = 0;
        map[startIndex][2] = 1;
        dijkstraWalk(this.getVertexFromData(start), this.getVertexFromData(end), map);
        return map;
    }

    private void dijkstraWalk(Vertex start, Vertex end, int[][] map) {
        if (start == end) {
            return;
        }
        int v1Index = this.vertexList.indexOf(start);
        for (int i = 0; i < start.link.size(); i++) {
            Edge e = start.link.get(i);
            int v2Index = this.vertexList.indexOf(e.v2);
            if (map[v2Index][1] > map[v1Index][1] + e.weight) {
                map[v2Index][1] = map[v1Index][1] + e.weight;
                map[v2Index][0] = v1Index;
            }
        }
        int min = -1;
        for (int i = 0; i < map.length; i++) {
            if (map[i][2] != 1) {
                if (min == -1) {
                    min = i;
                } else {
                    min = map[i][1] < map[min][1] ? i : min;
                }
            }
        }
        if (min == -1) {
            return;
        }
        map[min][2] = 1;
        dijkstraWalk(this.vertexList.get(min), end, map);
    }

}