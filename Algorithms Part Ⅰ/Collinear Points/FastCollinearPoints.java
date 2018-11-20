import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * @Title: FastCollinearPoints.java
 * @Package
 * @Description: TODO
 * @author tang 1101520766@qq.com
 * @date 2017年12月24日 上午11:19:46
 * @version V1.0
 */

public class FastCollinearPoints {
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

        public Comparator<IndexPoint> slopeOrder() {
            return new Comparator<IndexPoint>() {
                @Override
                public int compare(IndexPoint o1, IndexPoint o2) {
                    double d = point.slopeTo(o1.point) - point.slopeTo(o2.point);
                    if (d < 0)
                        return -1;
                    return d > 0 ? 1 : 0;
                }
            };
        }

        public double slopeTo(IndexPoint that) {
            return point.slopeTo(that.point);
        }

    }

    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null)
            throw new IllegalArgumentException();
        IndexPoint[] indexPoints = new IndexPoint[points.length];
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException();
            indexPoints[i] = new IndexPoint(points[i], i);
        }
        Arrays.sort(indexPoints);
        for (int i = 1; i < indexPoints.length; ++i) {
            if (indexPoints[i].compareTo(indexPoints[i - 1]) == 0)
                throw new IllegalArgumentException();
        }
        Line[] temp = new Line[indexPoints.length];
        int n = 0;
        for (int i = 0; i < indexPoints.length; ++i) {
            if (i + 3 < indexPoints.length) {
                Arrays.sort(indexPoints, i, indexPoints.length);
                Arrays.sort(indexPoints, i + 1, indexPoints.length, indexPoints[i].slopeOrder());
                double slope = indexPoints[i].slopeTo(indexPoints[i + 1]);
                int start = i + 1;
                int j = i + 2;
                for (; j < indexPoints.length; ++j) {
                    double curSlope = indexPoints[i].slopeTo(indexPoints[j]);
                    if (curSlope == Double.NEGATIVE_INFINITY || slope == Double.NEGATIVE_INFINITY)
                        throw new IllegalArgumentException();
                    if (curSlope != slope) {
                        if (j - start >= 3) {
                            if (n == temp.length) {
                                temp = resize(temp);
                                n = temp.length / 2;
                            }
                            temp[n++] = new Line(indexPoints[i], indexPoints[j - 1]);
                        }
                        slope = indexPoints[i].slopeTo(indexPoints[j]);
                        start = j;
                    }
                }
                if (j == indexPoints.length && j - start >= 3) {
                    if (n == temp.length) {
                        temp = resize(temp);
                        n = temp.length / 2;
                    }
                    temp[n++] = new Line(indexPoints[i], indexPoints[j - 1]);
                }
            }
        }
        Arrays.sort(temp, 0, n);
        lineSegments = new LineSegment[n];
        int cnt = 0;
        for (int i = 0; i < n; ++i) {
            if (i == 0 || temp[i - 1].slope != temp[i].slope || temp[i - 1].end != temp[i].end)
                lineSegments[cnt++] = new LineSegment(temp[i].start.point, temp[i].end.point);
        }
        LineSegment[] segments = new LineSegment[cnt];
        for (int i = 0; i < cnt; ++i)
            segments[i] = lineSegments[i];
        lineSegments = segments;
    }

    private Line[] resize(Line[] lines) {
        Arrays.sort(lines);
        Line[] temp = new Line[lines.length];
        int cnt = 0;
        for (int i = 0; i < lines.length; ++i) {
            if (i == 0 || lines[i - 1].slope != lines[i].slope || lines[i - 1].end != lines[i].end)
                temp[cnt++] = lines[i];
        }
        lines = new Line[cnt * 2];
        for (int i = 0; i < cnt; ++i)
            lines[i] = temp[i];
        return lines;
    }

    private class Line implements Comparable<Line> {
        private IndexPoint start;
        private IndexPoint end;
        private double slope;

        public Line(IndexPoint start, IndexPoint end) {
            this.start = start;
            this.end = end;
            this.slope = start.slopeTo(end);
        }

        @Override
        public int compareTo(Line o) {
            int res = Double.compare(slope, o.slope);
            if (res == 0) {
                return end.compareTo(o.end);
            }
            return res;
        }

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}