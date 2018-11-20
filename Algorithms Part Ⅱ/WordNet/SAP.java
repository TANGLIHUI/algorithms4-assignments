import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**    
* @Title: SAP.java  
* @Package   
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2018年1月5日 下午5:14:29  
* @version V1.0  
*/

public class SAP {
    private Digraph G = null;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validate(v);
        validate(w);
        Set<Integer> vv = new HashSet<>();
        vv.add(v);
        Set<Integer> ww = new HashSet<>();
        ww.add(w);
        return length(vv, ww);
    }

    private void validate(int v) {
        if (v < 0 || v > G.V() - 1)
            throw new IllegalArgumentException();
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        validate(v);
        validate(w);
        Set<Integer> vv = new HashSet<>();
        vv.add(v);
        Set<Integer> ww = new HashSet<>();
        ww.add(w);
        return ancestor(vv, ww);
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        int[] visitV = bfs(v);
        int[] visitW = bfs(w);
        int min = Integer.MAX_VALUE;
        for (int x = 0; x < G.V(); ++x) {
            if (visitV[x] != Integer.MAX_VALUE && visitW[x] != Integer.MAX_VALUE)
                min = Math.min(min, visitV[x] + visitW[x]);
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private int[] bfs(Iterable<Integer> iterable) {
        Set<Integer> set = new HashSet<>();
        int[] visit = new int[G.V()];
        Queue<Integer> queue = new Queue<>();
        for (int i = 0; i < visit.length; ++i)
            visit[i] = Integer.MAX_VALUE;
        for (int x : iterable) {
            set.add(x);
            visit[x] = 0;
            queue.enqueue(x);
        }
        while (!queue.isEmpty()) {
            int cur = queue.dequeue();
            for (int next : G.adj(cur)) {
                if (visit[next] > visit[cur] + 1) {
                    if (!set.contains(next)) {
                        queue.enqueue(next);
                    }
                    visit[next] = visit[cur] + 1;
                }
            }
        }
        return visit;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v);
        validate(w);
        int min = Integer.MAX_VALUE;
        int ance = -1;
        int[] visitV = bfs(v);
        int[] visitW = bfs(w);
        for (int x = 0; x < G.V(); ++x) {
            if (visitV[x] != Integer.MAX_VALUE && visitW[x] != Integer.MAX_VALUE)
                if (visitV[x] + visitW[x] < min) {
                    ance = x;
                    min = visitV[x] + visitW[x];
                }
        }
        return ance;
    }

    private void validate(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException();
        for (int i : v)
            validate(i);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // String path = "E:/WorkSpace1/algorithm/wordnet/digraph2.txt";
        // In in = new In(path);
        // Digraph G = new Digraph(in);
        // SAP sap = new SAP(G);
        // while (!StdIn.isEmpty()) {
        // int v = StdIn.readInt();
        // int w = StdIn.readInt();
        // int length = sap.length(v, w);
        // int ancestor = sap.ancestor(v, w);
        // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
         In in = new In(args[0]);
         Digraph G = new Digraph(in);
         SAP sap = new SAP(G);
         while (!StdIn.isEmpty()) {
         int v = StdIn.readInt();
         int w = StdIn.readInt();
         int length = sap.length(v, w);
         int ancestor = sap.ancestor(v, w);
         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
         }
    }
}
