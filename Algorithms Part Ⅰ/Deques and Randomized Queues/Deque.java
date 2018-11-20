import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item = null;
        private Node next = null;
        private Node prior = null;

        public Node(Item item) {
            this.item = item;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node cur = first;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (cur == null)
                throw new NoSuchElementException();
            Item item = cur.item;
            cur = cur.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Node first;
    private Node last;
    private int size;

    public Deque() {
        // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        // is the deque empty?
        return size == 0;
    }

    public int size() {
        // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {
        // add the item to the front
        if (item == null)
            throw new IllegalArgumentException();
        if (first == null) {
            first = new Node(item);
            last = first;
        } else {
            Node second = first;
            first = new Node(item);
            first.next = second;
            second.prior = first;
        }
        size++;
    }

    public void addLast(Item item) {
        // add the item to the end
        if (item == null)
            throw new IllegalArgumentException();
        if (last == null) {
            last = new Node(item);
            first = last;
        } else {
            Node second = last;
            last = new Node(item);
            last.prior = second;
            second.next = last;
        }
        size++;
    }

    public Item removeFirst() {
        // remove and return the item from the front
        if (isEmpty())
            throw new NoSuchElementException();
        Node ans = first;
        first = first.next;
        if (first != null)
            first.prior = null;
        size--;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        return ans.item;
    }

    public Item removeLast() {
        // remove and return the item from the end
        if (isEmpty())
            throw new NoSuchElementException();
        Node ans = last;
        last = last.prior;
        if (last != null)
            last.next = null;
        size--;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        return ans.item;
    }

    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    public static void main(String[] args) {
        // unit testing (optional)
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        // deque.removeFirst();
        // deque.removeLast();
        Iterator<Integer> it = deque.iterator();
        while (it.hasNext()) {
            StdOut.print(it.next() + " ");
        }
    }
}