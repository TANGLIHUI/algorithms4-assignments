import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * @Title: BruteCollinearPoints.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月24日 上午10:28:02
 * @version V1.0
 */

public class BruteCollinearPoints {
    private LineSegment[] lineSegments = null;

    private class IndexPoint implements Comparable<IndexPoint> {
        private Point point;

        public IndexPoint(Point point, int index) {
            super();
            this.point = point;
        }

        public int compareTo(IndexPoint that) {
            return this.point.compareTo(that.point);
        }

        public double slopeTo(IndexPoint that) {
            return point.slopeTo(that.point);
        }

    }

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null)
            throw new IllegalArgumentException();
        IndexPoint[] indexPoints = new IndexPoint[points.length];
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            indexPoints[i] = new IndexPoint(points[i], i);
        }
        Arrays.sort(indexPoints);
        for (int i = 0; i < indexPoints.length - 1; ++i) {
            if (indexPoints[i].slopeTo(indexPoints[i + 1]) == Double.NEGATIVE_INFINITY)
                throw new IllegalArgumentException();
        }
        LineSegment[] temp = new LineSegment[indexPoints.length / 4];
        int n = 0;
        for (int i = 0; i < indexPoints.length; ++i) {
            for (int j = i + 1; j < indexPoints.length; ++j) {
                double slopeij = indexPoints[i].slopeTo(indexPoints[j]);
                for (int k = j + 1; k < indexPoints.length; ++k) {
                    double slopejk = indexPoints[j].slopeTo(indexPoints[k]);
                    if (slopeij == slopejk)
                        for (int l = k + 1; l < indexPoints.length; ++l) {
                            double slopekl = indexPoints[k].slopeTo(indexPoints[l]);
                            if (slopejk == slopekl) {
                                if (n == temp.length) {
                                    temp = resize(temp);
                                }
                                temp[n++] = new LineSegment(indexPoints[i].point, indexPoints[l].point);
                            }
                        }
                }
            }
        }
        lineSegments = new LineSegment[n];
        for (int i = 0; i < n; ++i) {
            lineSegments[i] = temp[i];
        }
    }

    private LineSegment[] resize(LineSegment[] lines) {
        LineSegment[] temp = new LineSegment[lines.length * 2];
        int cnt = 0;
        for (int i = 0; i < lines.length; ++i) {
            temp[cnt++] = lines[i];
        }
        return temp;
    }

    public int numberOfSegments() {
        // the number of line segments
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        // the line segments
        LineSegment[] temp = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; ++i)
            temp[i] = lineSegments[i];
        return temp;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}