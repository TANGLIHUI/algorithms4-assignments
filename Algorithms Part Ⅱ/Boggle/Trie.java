import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;

public class Trie implements Iterable<String> {
    private static final int R = 26;

    private Node root = null;

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }

    public Trie() {
    }

    public boolean contains(String key) {
        StringBuilder sb=new StringBuilder(key);
        Node x = get(root, sb, 0);
        if (x == null)
            return false;
        return x.isString;
    }

    private Node get(Node x, StringBuilder key, int d) {
        if (x == null)
            return null;
        if (d == key.length())
            return x;
        char c = key.charAt(d);
        return get(x.next[(int) (c - 'A')], key, d + 1);
    }

    public void add(String key) {
        StringBuilder sb=new StringBuilder(key);
        root = add(root, sb, 0);
    }

    private Node add(Node x, StringBuilder key, int d) {
        if (x == null)
            x = new Node();
        if (d == key.length()) {
            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], key, d + 1);
        }
        return x;
    }

    public Iterator<String> iterator() {
        return keysWithPrefix("").iterator();
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, new StringBuilder(prefix), 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null)
            return;
        if (x.isString)
            results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append((char) (c + 'A'));
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

}
