import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        validate(picture);
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        return new Picture(picture);
    }

    public int width() {
        // width of current picture
        return picture.width();
    }

    public int height() {
        // height of current picture
        return picture.height();
    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x < 0 || x > this.width() - 1 || y < 0 || y > this.height() - 1)
            throw new IllegalArgumentException();
        if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1)
            return 1000.0;
        Color up = picture.get(x, y - 1);
        Color down = picture.get(x, y + 1);
        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        double dr = down.getRed(), dg = down.getGreen(), db = down.getBlue();
        double ur = up.getRed(), ug = up.getGreen(), ub = up.getBlue();
        double lr = left.getRed(), lg = left.getGreen(), lb = left.getBlue();
        double rr = right.getRed(), rg = right.getGreen(), rb = right.getBlue();
        double ans = Math.sqrt(1.0 * (dr - ur) * (dr - ur) + (dg - ug) * (dg - ug) + (db - ub) * (db - ub)
                + (rr - lr) * (rr - lr) + (rg - lg) * (rg - lg) + (rb - lb) * (rb - lb));
        return ans;
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int[] horizon = new int[this.width()];
        double[][] dp = new double[this.width()][this.height()];
        int[][] path = new int[this.width()][this.height()];
        for (int i = 0; i < this.height(); ++i)
            dp[0][i] = 1000;
        for (int i = 1; i < this.width(); ++i) {
            // System.out.print(1000);
            for (int j = 0; j < this.height(); ++j) {
                dp[i][j] = dp[i - 1][j];
                path[i][j] = j;
                if (j > 0 && dp[i][j] > dp[i - 1][j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                    path[i][j] = j - 1;
                }
                if (j < this.height() - 1 && dp[i][j] > dp[i - 1][j + 1]) {
                    dp[i][j] = dp[i - 1][j + 1];
                    path[i][j] = j + 1;
                }
                dp[i][j] += energy(i, j);
                // StdOut.printf(" %9.5f", dp[i][j]);
            }
            // System.out.println();
        }
        int h = 0;
        double min = dp[this.width() - 1][0];
        for (int i = 1; i < this.height(); ++i)
            if (dp[this.width() - 1][i] < min) {
                min = dp[this.width() - 1][i];
                h = i;
            }
        for (int i = this.width() - 1; i >= 0; --i) {
            horizon[i] = h;
            h = path[i][h];
        }
        return horizon;
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        int[] vertical = new int[this.height()];
        double[][] dp = new double[this.width()][this.height()];
        int[][] path = new int[this.width()][this.height()];
        for (int i = 0; i < this.width(); ++i)
            dp[i][0] = 1000;
        for (int j = 1; j < this.height(); ++j) {
            // System.out.print(1000);
            for (int i = 0; i < this.width(); ++i) {
                dp[i][j] = dp[i][j - 1];
                path[i][j] = i;
                if (i > 0 && dp[i][j] > dp[i - 1][j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                    path[i][j] = i - 1;
                }
                if (i < this.width() - 1 && dp[i][j] > dp[i + 1][j - 1]) {
                    dp[i][j] = dp[i + 1][j - 1];
                    path[i][j] = i + 1;
                }
                dp[i][j] += energy(i, j);
                // StdOut.printf(" %9.5f", dp[i][j]);
            }
            // System.out.println();
        }
        int v = 0;
        double min = dp[0][this.height() - 1];
        for (int i = 1; i < this.width(); ++i)
            if (dp[i][this.height() - 1] < min) {
                min = dp[i][this.height() - 1];
                v = i;
            }
        for (int i = this.height() - 1; i >= 0; --i) {
            vertical[i] = v;
            v = path[v][i];
        }
        return vertical;
    }

    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        validate(seam, true);
        Picture temp = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); ++i) {
            boolean mark = false;
            for (int j = 0; j < height(); ++j) {
                if (!mark) {
                    if (seam[i] == j) {
                        mark = true;
                        continue;
                    }
                    temp.set(i, j, picture.get(i, j));
                } else {
                    temp.set(i, j - 1, picture.get(i, j));
                }
            }
        }
        picture = temp;
    }

    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        validate(seam, false);
        Picture temp = new Picture(width() - 1, height());
        for (int j = 0; j < height(); ++j) {
            boolean mark = false;
            for (int i = 0; i < width(); ++i) {
                if (!mark) {
                    if (seam[j] == i) {
                        mark = true;
                        continue;
                    }
                    temp.set(i, j, picture.get(i, j));
                } else {
                    temp.set(i - 1, j, picture.get(i, j));
                }
            }
        }
        picture = temp;
    }

    private void validate(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }

    private void validate(int[] seam, boolean hor) {
        validate(seam);
        if ((hor ? this.height() : this.width()) < 2)
            throw new IllegalArgumentException();
        if (seam.length != (hor ? this.width() : this.height()))
            throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= (hor ? this.height() : this.width()))
            throw new IllegalArgumentException();
        for (int i = 1; i < (hor ? this.width() : this.height()); ++i) {
            if (seam[i] < 0 || seam[i] >= (hor ? this.height() : this.width()) || Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException();
        }
    }

}