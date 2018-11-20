import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**    
* @Title: PercolationStats.java  
* @Package   
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2017年12月18日 下午9:48:25  
* @version V1.0  
*/

/**
 * @Title: PercolationStats.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月18日 下午9:48:25
 * @version V1.0
 */
public class PercolationStats {
    private int times;
    private double mu = -1;
    private double sigma = -1;
    private double low = -1;
    private double high = -1;
    private double[] data;

    public PercolationStats(int n, int trials) {
        // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        data = new double[trials];
        times = trials;
        for (int i = 0; i < trials; ++i) {
            Percolation percolation = new Percolation(n);
            double p = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    p++;
                }
            }
            data[i] = p / n / n;
        }
    }

    public double mean() {
        // sample mean of percolation threshold
        if (mu == -1) {
            mu = StdStats.mean(data);
        }
        return mu;
    }

    public double stddev() {
        // sample standard deviation of percolation threshold
        if (sigma == -1) {
            sigma = StdStats.stddev(data);
        }
        return sigma;
    }

    public double confidenceLo() {
        // low endpoint of 95% confidence interval
        if (low == -1)
            low = mean() - 1.96 * stddev() / Math.sqrt(times);
        return low;
    }

    public double confidenceHi() {
        // high endpoint of 95% confidence interval
        if (high == -1)
            high = mean() + 1.96 * stddev() / Math.sqrt(times);
        return high;
    }

    public static void main(String[] args) {
        // test client (described below)
        int n = 200, t = 100;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        PercolationStats pStats = new PercolationStats(n, t);
        StdOut.println("mean                    = " + pStats.mean());
        StdOut.println("stddev                  = " + pStats.stddev());
        StdOut.println("95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]");
    }
}
