import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @Title: Percolation.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月18日 下午7:59:56
 * @version V1.0
 */
public class Percolation {
    private boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf1;
    private int num;
    private int size;

    public Percolation(int n) {
        // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException();
        size = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf1 = new WeightedQuickUnionUF(n * n + 2);
        num = 0;
        grid = new boolean[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                grid[i][j] = false;
    }

    public void open(int row, int col) {
        // open site (row, col) if it is not open already
        validate(row, col);
        row--;
        col--;
        if (!grid[row][col]) {
            num++;
            grid[row][col] = true;
            int index = row * size + col;
            if (col > 0 && grid[row][col - 1]) {
                uf1.union(index, index - 1);
                uf.union(index, index - 1);
            }
            if (row > 0 && grid[row - 1][col]) {
                uf1.union(index, index - size);
                uf.union(index, index - size);
            }
            if (col < size - 1 && grid[row][col + 1]) {
                uf1.union(index, index + 1);
                uf.union(index, index + 1);
            }
            if (row < size - 1 && grid[row + 1][col]) {
                uf1.union(index, index + size);
                uf.union(index, index + size);
            }
            if (row == 0) {
                uf1.union(index, size * size);
                uf.union(index, size * size);
            }
            if (row == size - 1)
                uf.union(index, size * size + 1);
        }
    }

    private void validate(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size)
            throw new IllegalArgumentException();
    }

    public boolean isOpen(int row, int col) {
        // is site (row, col) open?
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        // is site (row, col) full?
        validate(row, col);
        int index = (row - 1) * size + col - 1;
        return uf1.connected(index, size * size);
    }

    public int numberOfOpenSites() {
        // number of open sites
        return num;
    }

    public boolean percolates() {
        // does the system percolate?
        return uf.connected(size * size + 1, size * size);
    }

    public static void main(String[] args) {
        int n = 200;
        int p = 0;
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                p++;
            }
        }
        StdOut.println(p * 1.0 / n / n);
    }
}
