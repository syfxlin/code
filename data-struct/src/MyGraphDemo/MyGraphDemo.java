package MyGraphDemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * MyGraphDemo
 */
public class MyGraphDemo {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        MyGraph<Character> gra = new MyGraph<Character>();
        gra.add(null, 'A', 0);
        gra.add('A', 'B', 12);
        gra.add('A', 'F', 16);
        gra.add('A', 'G', 14);
        gra.add('B', 'F', 7);
        gra.add('G', 'F', 9);
        gra.add('B', 'C', 10);
        gra.add('F', 'C', 6);
        gra.add('F', 'E', 2);
        gra.add('G', 'E', 8);
        gra.add('C', 'E', 5);
        gra.add('C', 'D', 3);
        gra.add('E', 'D', 4);
        gra.testString();
        // List<MyGraph<Integer>.Vertex> list = gra.BFS(0, 3);
        // for (MyGraph<Integer>.Vertex var : list) {
        // System.out.print(var.data);
        // }
        // int[][] a = gra.toAssociationMatrix();
        // for (int i = 0; i < 4; i++) {
        // System.out.println(Arrays.toString(a[i]));
        // }
        in.close();
    }
}

/**
 * MyGraph Vertex中存放顶点的信息，以及连接点列表 Edge中存放权重，和与之连接点在VertexList中的索引
 */
class MyGraph<T extends Comparable<T>> {
    List<Vertex> vertexList;
    List<EdgeNoPath> edgeList;

    MyGraph() {
        this.vertexList = new LinkedList<Vertex>();
        this.edgeList = new LinkedList<EdgeNoPath>();
    }

    // 顶点类
    class Vertex {
        T data;
        List<Edge> link;
        int listIndex;

        Vertex() {
        }

        Vertex(T data, int listIndex) {
            link = new LinkedList<Edge>();
            this.data = data;
            this.listIndex = listIndex;
        }

        public String toString() {
            return data.toString();
        }
    }

    // 边类
    class Edge {
        int prev;
        int index;
        int weight;

        Edge(int index, int weight, int prev) {
            this.prev = prev;
            this.index = index;
            this.weight = weight;
        }
    }

    class EdgeNoPath implements Comparable<EdgeNoPath> {
        int[] index;
        int weight;

        EdgeNoPath(int index1, int index2, int weight) {
            boolean is = true;
            for (int i = 0; i < edgeList.size(); i++) {
                if ((edgeList.get(i).index[0] == index1 && edgeList.get(i).index[1] == index2)
                        || (edgeList.get(i).index[0] == index2 && edgeList.get(i).index[1] == index1)) {
                    is = false;
                    break;
                }
            }
            if (is) {
                this.weight = weight;
                this.index = new int[2];
                this.index[0] = index1;
                this.index[1] = index2;
            }
        }

        public int compareTo(EdgeNoPath o) {
            if (this.weight > o.weight) {
                return 1;
            } else if (this.weight < o.weight) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * 添加顶点
     * 
     * @param prevData 连接的上一个顶点的数据
     * @param nextData 连接的上一个顶点的数据
     * @param weight   边的权重
     */
    public void add(T prevData, T nextData, int weight) {
        if (prevData == null && vertexList.size() == 0) {
            vertexList.add(new Vertex(nextData, vertexList.size()));
            return;
        } else if (prevData == null && vertexList.size() != 0) {
            return;
        }
        // 定位上一个节点
        int prev_index = this.getIndexFromData(prevData);
        int next_index = this.getIndexFromData(nextData);
        Vertex new_vertex;
        if (next_index == -1) {
            new_vertex = new Vertex(nextData, vertexList.size());
            // 将新节点添加到顶点列表中
            vertexList.add(new_vertex);
            next_index = vertexList.size() - 1;
        } else {
            new_vertex = vertexList.get(next_index);
        }
        // 将边的信息添加到新节点
        new_vertex.link.add(new Edge(prev_index, weight, next_index));
        // 将边的信息添加到上一个节点
        vertexList.get(prev_index).link.add(new Edge(next_index, weight, prev_index));
        edgeList.add(new EdgeNoPath(prev_index, next_index, weight));
    }

    /**
     * 获取制定data的顶点在顶点列表中的索引
     * 
     * @param data 要进行搜索的数据
     * @return 返回当前数据的顶点在顶点邻接表的索引，若未找到就返回 -1
     */
    public int getIndexFromData(T data) {
        int i = -1;
        while (vertexList.size() > i + 1) {
            if (vertexList.get(i + 1).data.compareTo(data) == 0) {
                i++;
                return i;
            }
            i++;
        }
        return -1;
    }
    // public int vertexOfEdge(T data) {

    // }
    public void testString() {
        for (Vertex var : vertexList) {
            System.out.print(var.data.toString() + ":");
            for (Edge v : var.link) {
                System.out.print("(" + vertexList.get(v.index).data.toString() + "," + v.weight + ")");
            }
            System.out.println();
        }
    }

    // 深度优先搜索 - 递归隐藏方法
    private boolean DFS(Vertex vertex, T data, List<Vertex> list, boolean[] visited) {
        if (vertex == null)
            return false;
        if (vertex.data.compareTo(data) == 0) {
            list.add(vertex);
            return true;
        }
        for (int i = vertex.link.size() - 1; i >= 0; i--) {
            int ver_index = vertex.link.get(i).index;
            if (!visited[ver_index]) {
                Vertex tempVertex = vertexList.get(ver_index);
                visited[ver_index] = true;
                boolean is = DFS(tempVertex, data, list, visited);
                if (is) {
                    list.add(vertex);
                    return true;
                }
                visited[ver_index] = false;
            }
        }
        return false;
    }

    /**
     * 深度优先搜索进行路径查找
     * 
     * @param startData 起点
     * @param Enddata   终点
     * @return 返回路径的点列表
     */
    public List<Vertex> DFS(T startData, T endData) {
        int start_index = this.getIndexFromData(startData);
        boolean[] visited = new boolean[vertexList.size()];
        visited[start_index] = true;
        List<Vertex> list = new ArrayList<Vertex>();
        DFS(vertexList.get(start_index), endData, list, visited);
        Collections.reverse(list);
        return list;
    }

    /**
     * 广度优先搜索进行路径查找
     * 
     * @param startData 起点
     * @param Enddata   终点
     * @return 返回路径的点列表
     */
    public List<Vertex> BFS(T startData, T endData) {
        int start_index = this.getIndexFromData(startData);
        BFS_Node endNode = null;
        List<BFS_Node> queue = new LinkedList<BFS_Node>();
        queue.add(new BFS_Node(vertexList.get(start_index), null));
        while (!queue.isEmpty()) {
            BFS_Node currentNode = queue.remove(0);
            if (currentNode.vertex.data.compareTo(endData) == 0) {
                endNode = currentNode;
                break;
            }
            for (int i = 0; i < currentNode.vertex.link.size(); i++) {
                queue.add(new BFS_Node(vertexList.get(currentNode.vertex.link.get(i).index), currentNode));
            }
        }
        List<Vertex> list = new LinkedList<Vertex>();
        while (endNode != null) {
            list.add(endNode.vertex);
            endNode = endNode.parent;
        }
        Collections.reverse(list);
        return list;
    }

    // 广度优先搜索进行路径追踪需要的类
    class BFS_Node {
        Vertex vertex;
        BFS_Node parent;

        BFS_Node(Vertex vertex, BFS_Node parent) {
            this.vertex = vertex;
            this.parent = parent;
        }
    }

    /**
     * 将顶点列表转换为顶点数组
     * 
     * @return 顶点数组
     */
    public Object[] toArraysVertex() {
        Object[] vertexs = vertexList.toArray();
        return vertexs;
    }

    /**
     * 将邻接表的图转换为邻接矩阵
     * 
     * @return 邻接矩阵数组
     */
    public int[][] toAdjacencyMatrix() {
        int vertexSize = vertexList.size();
        int[][] matrix = new int[vertexSize][vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            Vertex currentVertex = vertexList.get(i);
            for (int j = 0; j < currentVertex.link.size(); j++) {
                Edge currentEdge = currentVertex.link.get(j);
                matrix[i][currentEdge.index] = currentEdge.weight;
            }
        }
        return matrix;
    }

    /**
     * 将邻接表图转换为关联矩阵
     * 
     * @return 关联矩阵数组
     */
    public int[][] toAssociationMatrix() {
        int vertexSize = vertexList.size();
        int edgeSize = edgeList.size();
        int[][] matrix = new int[vertexSize][edgeSize];
        for (int j = 0; j < edgeSize; j++) {
            EdgeNoPath currentEdge = edgeList.get(j);
            matrix[currentEdge.index[0]][j]++;
            matrix[currentEdge.index[1]][j]++;
        }
        return matrix;
    }

    /**
     * 获取最短路径的列表（Dijkstra，Vertex）
     * 
     * @param start 起点的顶点对象
     * @param end   终点的顶点对象
     * @return 路径的顶点数组
     */
    public List<Vertex> minPath(Vertex start, Vertex end) {
        int[][] map = minPathMap(start, end);
        int index = end.listIndex;
        List<Vertex> path = new LinkedList<Vertex>();
        while (index != start.listIndex) {
            path.add(vertexList.get(index));
            index = map[index][0];
        }
        path.add(vertexList.get(index));
        Collections.reverse(path);
        return path;
    }

    /**
     * 获取最短路径的长度（Dijkstra，Vertex）
     * 
     * @param start 起点的顶点对象
     * @param end   终点的顶点对象
     * @return 中间的最短路径长度
     */
    public int minPathLen(Vertex start, Vertex end) {
        int[][] map = minPathMap(start, end);
        return map[end.listIndex][1];
    }

    /**
     * 获取最短路径的列表（Dijkstra，T）
     * 
     * @param start 起点的数据
     * @param end   终点的数据
     * @return 路径的顶点数组
     */
    public List<Vertex> minPath(T start, T end) {
        Vertex sVertex = vertexList.get(this.getIndexFromData(start));
        Vertex eVertex = vertexList.get(this.getIndexFromData(end));
        return minPath(sVertex, eVertex);
    }

    /**
     * 获取最短路径的列表（Dijkstra，T）
     * 
     * @param start 起点的数据
     * @param end   终点的数据
     * @return 路径的顶点数组
     */
    public int minPathLen(T start, T end) {
        Vertex sVertex = vertexList.get(this.getIndexFromData(start));
        Vertex eVertex = vertexList.get(this.getIndexFromData(end));
        return minPathLen(sVertex, eVertex);
    }

    private int[][] minPathMap(Vertex start, Vertex end) {
        int[][] map = new int[this.vertexList.size()][3];
        for (int i = 0; i < map.length; i++) {
            // 上一个节点的位置
            map[i][0] = -1;
            // 从开始到当前节点的距离
            map[i][1] = Integer.MAX_VALUE;
            // 标记
            map[i][2] = 0;
        }
        map[start.listIndex][1] = 0;
        map[start.listIndex][2] = 1;
        minPathMap(start, end, map);
        return map;
    }

    private void minPathMap(Vertex start, Vertex end, int[][] map) {
        if (start == end && map[start.listIndex][2] == 1) {
            return;
        }
        int[] min = null;
        // min[0] 离start距离最短的节点位置，min[1] 上一个节点的位置, min[2] 从开始到当前节点的距离
        for (int i = 0; i < start.link.size(); i++) {
            Edge edge = start.link.get(i);
            if (map[edge.index][2] == 1)
                continue;
            map[edge.index][0] = start.link.get(i).prev;
            // map[edge.index][1] = map[edge.index][1] == Integer.MAX_VALUE ? edge.weight
            // : map[map[edge.index][0]][1] + edge.weight;
            if (map[edge.index][1] > map[map[edge.index][0]][1] + edge.weight) {
                map[edge.index][1] = map[map[edge.index][0]][1] + edge.weight;
            }
            if (min == null) {
                min = new int[] { edge.index, map[edge.index][0], map[edge.index][1] };
            } else {
                min = min[2] <= map[edge.index][1] ? min
                        : new int[] { edge.index, map[edge.index][0], map[edge.index][1] };
                ;
            }
        }
        if (min == null) {
            return;
        }
        map[min[0]][2] = 1;
        minPathMap(vertexList.get(min[0]), end, map);
    }
}