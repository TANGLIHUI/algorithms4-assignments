import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * @Title: Permutation.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月19日 下午2:28:46
 * @version V1.0
 */

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }
        while (k-- > 0) {
            StdOut.println(queue.dequeue());
        }
    }

}
