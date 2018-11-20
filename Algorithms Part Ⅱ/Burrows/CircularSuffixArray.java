import java.util.Arrays;

import edu.princeton.cs.algs4.StdIn;

public class CircularSuffixArray {
    private class Node implements Comparable<Node> {
        private int val;

        public Node(int v) {
            val = v;
        }

        @Override
        public int compareTo(Node o) {
            int i = val, j = o.val;
            for (int k = 0; k < n; ++k) {
                char c1 = s.charAt((i + k) % n), c2 = s.charAt((j + k) % n);
                if (c1 != c2)
                    return c1 - c2;
            }
            return 0;
        }

    }

    private String s = null;
    private Node[] strs = null;
    private int n = 0;

    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        this.s = s;
        n = s.length();
        strs = new Node[n];
        for (int i = 0; i < n; ++i) {
            strs[i] = new Node(i);
        }
        Arrays.sort(strs);
    }

    public int length() {
        return n;
    }

    public int index(int i) {
        if (i < 0 || i >= n)
            throw new IllegalArgumentException();
        return strs[i].val;
    }

    public static void main(String[] args) {
        CircularSuffixArray cs = new CircularSuffixArray(StdIn.readLine());
        System.out.println(cs.length());
        for (int i = 0; i < cs.length(); ++i)
            System.out.print(cs.index(i) + " ");
        System.out.println();
    }
}
