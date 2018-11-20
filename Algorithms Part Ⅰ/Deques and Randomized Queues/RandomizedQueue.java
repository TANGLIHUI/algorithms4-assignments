import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * @Title: RandomizedQueue.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月19日 下午12:47:09
 * @version V1.0
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private class RandomizedQuqueIterator implements Iterator<Item> {
        private Item[] itArray;
        int cur = 0;

        public RandomizedQuqueIterator() {
            itArray = (Item[]) new Object[size];
            int mySize = 0;
            for (int i = 0; i < size; ++i) {
                itArray[mySize++] = array[i];
                int k = StdRandom.uniform(0, mySize);
                swap(k, mySize - 1);
            }
            cur = 0;
        }

        private void swap(int i, int j) {
            Item temp = itArray[i];
            itArray[i] = itArray[j];
            itArray[j] = temp;
        }

        @Override
        public boolean hasNext() {
            return cur != itArray.length;
        }

        @Override
        public Item next() {
            if (cur == itArray.length)
                throw new NoSuchElementException();
            return itArray[cur++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private Item[] array;
    private int size;

    public RandomizedQueue() {
        // construct an empty randomized queue
        array = (Item[]) new Object[1];
        size = 0;
    }

    private void resize(int n) {
        Item[] newArray = (Item[]) new Object[n];
        for (int i = 0; i < size; ++i) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public boolean isEmpty() {
        // is the randomized queue empty?
        return size == 0;
    }

    public int size() {
        // return the number of items on the randomized queue
        return size;
    }

    public void enqueue(Item item) {
        // add the item
        if (item == null)
            throw new IllegalArgumentException();
        if (size == array.length) {
            resize(size * 2);
        }
        array[size++] = item;
    }

    public Item dequeue() {
        // remove and return a random item
        if (isEmpty())
            throw new NoSuchElementException();
        int n = StdRandom.uniform(0, size);
        Item temp = array[n];
        array[n] = array[size - 1];
        array[size - 1] = null;
        size--;
        if (size > 0 && size == array.length / 4)
            resize(size);
        return temp;
    }

    public Item sample() {
        // return a random item (but do not remove it)
        if (isEmpty())
            throw new NoSuchElementException();
        return array[StdRandom.uniform(0, size)];
    }

    @Override
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQuqueIterator();
    }

    public static void main(String[] args) {
        // unit testing (optional)
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(2);
        System.out.println(queue.dequeue());
        queue.enqueue(3);
        System.out.println(queue.size());
        int t = 0;
        for (int i = 0; i < t; ++i) {
            int a = StdRandom.uniform(0, t);
            queue.enqueue(a);
            if (StdRandom.uniform(0, t) % 3 == 0)
                queue.dequeue();
            Iterator<Integer> it = queue.iterator();
            while (it.hasNext()) {
                StdOut.print(it.next());
            }
            StdOut.println();
            StdOut.println(queue.isEmpty());
        }
    }

}
