import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    public static void transform() {
        StringBuffer sb = new StringBuffer();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }
        CircularSuffixArray sca = new CircularSuffixArray(sb.toString());
        for (int i = 0; i < sca.length(); ++i) {
            if (sca.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < sca.length(); ++i) {
            BinaryStdOut.write((byte) sb.charAt((sca.index(i) - 1 + sb.length()) % sb.length()));
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int id = BinaryStdIn.readInt();
        StringBuffer sb = new StringBuffer();
        while (!BinaryStdIn.isEmpty()) {
            sb.append(BinaryStdIn.readChar());
        }
        int n = sb.length();
        char[] fir = new char[n];
        char[] suf = sb.toString().toCharArray();
        int[] idx = new int[256];
        for (int i = 0; i < n; ++i)
            idx[suf[i]]++;
        int k = 0;
        for (int i = 0; i < 256; ++i)
            while (idx[i]-- > 0)
                fir[k++] = (char) i;
        for (int i = 0; i < 256; ++i)
            idx[i] = 0;
        List<List<Integer>> dict = new ArrayList<>();
        for (int i = 0; i < 256; ++i)
            dict.add(new ArrayList<>());
        for (int i = 0; i < n; ++i)
            dict.get(suf[i]).add(i);
        int[] next = new int[n];
        for (int i = 0; i < n; ++i) {
            int j = fir[i];
            next[i] = dict.get(j).get(idx[j]++);
        }
        for (int i = 0; i < n; ++i) {
            BinaryStdOut.write(fir[id]);
            id = next[id];
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else
            inverseTransform();
    }
}
