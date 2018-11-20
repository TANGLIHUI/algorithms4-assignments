import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    public static void encode() {
        int[] idx = new int[256];
        int[] str = new int[256];
        for (int i = 0; i < 256; ++i) {
            idx[i] = i;
            str[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write((byte) str[c]);
            for (int i = str[c] - 1; i >= 0; --i) {
                idx[i + 1] = idx[i];
                str[idx[i]]++;
            }
            str[c] = 0;
            idx[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        int[] str = new int[256];
        int[] idx = new int[256];
        for (int i = 0; i < 256; ++i) {
            str[i] = i;
            idx[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int c = idx[BinaryStdIn.readChar()];
            BinaryStdOut.write((char) c);
            for (int i = str[c] - 1; i >= 0; --i) {
                idx[i + 1] = idx[i];
                str[idx[i]]++;
            }
            str[c] = 0;
            idx[0] = c;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else
            decode();
    }
}
